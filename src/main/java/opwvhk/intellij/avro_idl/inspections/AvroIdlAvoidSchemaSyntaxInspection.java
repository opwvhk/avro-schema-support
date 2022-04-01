package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlAvoidSchemaSyntaxInspection extends BaseAvroIdlInspection<PsiElement> {
	private static final TokenSet SCHEMA_SYNTAX_TOPLEVEL_ELEMENTS = TokenSet.create(NAMESPACE_DECLARATION, MAIN_SCHEMA_DECLARATION, IMPORT_DECLARATION,
		RECORD_DECLARATION, ENUM_DECLARATION, FIXED_DECLARATION);
	private static final TokenSet SCHEMA_SYNTAX_TOPLEVEL_KEYWORDS = TokenSet.create(NAMESPACE, SCHEMA, IMPORT, RECORD, ERROR, ENUM, FIXED);

	public AvroIdlAvoidSchemaSyntaxInspection() {
		super(PsiElement.class);
	}

	@Override
	protected void visitElement(@NotNull PsiElement element,
	                            @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		if (ReplaceSchemaSyntaxWithProtocolQuickFix.isAvailableFor(element)) {
			ReplaceSchemaSyntaxWithProtocolQuickFix replaceWithShorthand = new ReplaceSchemaSyntaxWithProtocolQuickFix(element);
			holder.registerProblem(element, "Use of schema syntax", replaceWithShorthand);
		}
	}


	private static class ReplaceSchemaSyntaxWithProtocolQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<PsiElement> {
		public ReplaceSchemaSyntaxWithProtocolQuickFix(@NotNull PsiElement element) {
			super(element, "Replace with protocol");
		}

		private static boolean isAvailableFor(@NotNull PsiElement element) {
			ASTNode node = element.getNode();
			if (!SCHEMA_SYNTAX_TOPLEVEL_KEYWORDS.contains(node.getElementType())) {
				return false;
			}
			final ASTNode previousToplevelElement = TreeUtil.findSiblingBackward(node.getTreeParent().getTreePrev(), SCHEMA_SYNTAX_TOPLEVEL_ELEMENTS);
			return !(element.getParent().getParent() instanceof AvroIdlProtocolBody) && previousToplevelElement == null;
		}

		@Override
		protected boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement element) {
			return isAvailableFor(element);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor, @NotNull PsiElement element) {
			// Find the parent of the declaration this element is a keyword of.
			PsiElement parent = element.getParent().getParent();

			// Find the header declarations that are available (both are optional)
			AvroIdlNamespaceDeclaration namespaceDeclaration = PsiTreeUtil.findChildOfType(parent, AvroIdlNamespaceDeclaration.class);
			AvroIdlMainSchemaDeclaration mainSchemaDeclaration = PsiTreeUtil.findChildOfType(parent, AvroIdlMainSchemaDeclaration.class);

			// Create the protocol
			String namespace = namespaceDeclaration != null ? namespaceDeclaration.getName() : null;
			AvroIdlProtocolDeclaration protocol = new AvroIdlElementFactory(project).createDummyProtocol(namespace);

			// Remove the header (also removes comments between the header declarations)
			PsiElement firstChildToMove = findFirstCodeElement(parent.getFirstChild(), PsiElement::getNextSibling);
			PsiElement lastChildToMove = findFirstCodeElement(parent.getLastChild(), PsiElement::getPrevSibling);

			final PsiElement firstOfHeader = coalesce(namespaceDeclaration, mainSchemaDeclaration);
			final PsiElement lastOfHeader = coalesce(mainSchemaDeclaration, namespaceDeclaration);
			if (firstOfHeader != null) {
				assert lastOfHeader != null;
				firstChildToMove = findFirstVisibleElement(lastOfHeader.getNextSibling(), PsiElement::getNextSibling);
				parent.deleteChildRange(firstOfHeader, lastOfHeader);
			}

			// Move all visible content
			requireNonNull(protocol.getProtocolBody()).addRange(firstChildToMove, lastChildToMove);
			parent.addBefore(protocol, firstChildToMove);
			parent.deleteChildRange(firstChildToMove, lastChildToMove);

			Optional.ofNullable(PsiTreeUtil.findChildOfType(file, AvroIdlProtocolDeclaration.class))
				.map(AvroIdlProtocolDeclaration::getNameIdentifier)
				.ifPresent(elem -> Optional.ofNullable(editor).ifPresent(ed -> selectElement(ed, elem)));

			// Place the cursor at the protocol name
			selectElement(editor, Optional.ofNullable(PsiTreeUtil.findChildOfType(file, AvroIdlProtocolDeclaration.class))
				.map(AvroIdlProtocolDeclaration::getNameIdentifier)
				.orElse(null));

			// The default indent change breaks formatting of doc (and block?) comments. Triggering a reformat keeps them intact.
			CodeStyleManager.getInstance(project).reformat(parent);
		}

		private PsiElement coalesce(PsiElement... items) {
			for (PsiElement item : items) {
				if (item != null) {
					return item;
				}
			}
			return null;
		}

		private PsiElement findFirstVisibleElement(PsiElement start, Function<PsiElement, PsiElement> nextFunction) {
			return AvroIdlPsiUtil.skipMatching(start, nextFunction, PsiWhiteSpace.class::isInstance, false);
		}

		private PsiElement findFirstCodeElement(PsiElement start, Function<PsiElement, PsiElement> nextFunction) {
			return AvroIdlPsiUtil.skipMatching(start, nextFunction, element ->
				element instanceof PsiWhiteSpace || element instanceof PsiComment && ((PsiComment)element).getTokenType() != DOC_COMMENT, false);
		}
	}
}
