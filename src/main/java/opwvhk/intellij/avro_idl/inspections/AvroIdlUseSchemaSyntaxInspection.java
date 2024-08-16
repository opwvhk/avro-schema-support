package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.language.AvroIdlUtil;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class AvroIdlUseSchemaSyntaxInspection extends BaseAvroIdlInspection<AvroIdlProtocolDeclaration> {
	public AvroIdlUseSchemaSyntaxInspection() {
		super(AvroIdlProtocolDeclaration.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlProtocolDeclaration element,
	                            @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		if (ReplaceProtocolWithSchemaSyntaxQuickFix.isAvailableFor(element)) {
			ASTNode protocolKeywordNode = element.getNode().findChildByType(AvroIdlTypes.PROTOCOL);
			if (protocolKeywordNode != null) {
				PsiElement protocolKeyword = protocolKeywordNode.getPsi();
				ReplaceProtocolWithSchemaSyntaxQuickFix replaceWithShorthand = new ReplaceProtocolWithSchemaSyntaxQuickFix(
						element);
				holder.registerProblem(protocolKeyword, TextBundle.message("inspection.use.schema.syntax"),
						replaceWithShorthand);
			}
		}
	}


	private static class ReplaceProtocolWithSchemaSyntaxQuickFix
			extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlProtocolDeclaration> {
		public ReplaceProtocolWithSchemaSyntaxQuickFix(@NotNull AvroIdlProtocolDeclaration element) {
			super(element, TextBundle.message("inspection.use.schema.syntax.fix"));
		}

		private static boolean isAvailableFor(@NotNull AvroIdlProtocolDeclaration element) {
			return !AvroIdlUtil.hasMessages(element);
		}

		@Override
		protected boolean isAvailable(@NotNull Project project, @NotNull PsiFile file,
		                              @NotNull AvroIdlProtocolDeclaration element) {
			return isAvailableFor(element);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
		                      @NotNull AvroIdlProtocolDeclaration element) {
			if (!isAvailableFor(element)) {
				return;
			}
			PsiElement parent = element.getParent();
			Optional<PsiElement> docComment = Optional.ofNullable(getDocumentationElement(element));
			AvroIdlProtocolBody protocolBody = requireNonNull(element.getProtocolBody(),
					"Inconsistency with isAvailableFor()");

			AvroIdlElementFactory elementFactory = new AvroIdlElementFactory(project);
			AvroIdlFile schemaSyntaxHeader = elementFactory.createSchemaSyntaxHeader(element);
			if (schemaSyntaxHeader.getFirstChild() != null) {
				parent.addRangeBefore(schemaSyntaxHeader.getFirstChild(), schemaSyntaxHeader.getLastChild(), element);
			}
			AvroIdlMainSchemaDeclaration mainSchemaDeclaration = PsiTreeUtil.getPrevSiblingOfType(element,
					AvroIdlMainSchemaDeclaration.class);
			if (protocolBody.getFirstChild() != null) {
				Optional.ofNullable(getDocumentationElement(protocolBody.getFirstChild()))
						.ifPresent(e -> parent.addBefore(e, element));
				parent.addRangeBefore(protocolBody.getFirstChild(), protocolBody.getLastChild(), element);
			}
			docComment.ifPresent(e -> parent.deleteChildRange(e, e));
			parent.deleteChildRange(element, element);

			// Place the cursor at the main schema declaration
			Optional.ofNullable(mainSchemaDeclaration)
					.map(AvroIdlMainSchemaDeclaration::getType)
					.ifPresent(e -> selectElement(editor, e));
		}

		private PsiElement getDocumentationElement(PsiElement declaration) {
			PsiElement docComment = AvroIdlPsiUtil.prevNonCommentLeaf(declaration);
			return AvroIdlPsiUtil.isDocComment(docComment) ? docComment : null;
		}
	}
}
