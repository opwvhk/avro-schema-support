package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.TreeUtil;
import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static opwvhk.intellij.avro_idl.language.AvroIdlUtil.ifType;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.NULL;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.QUESTION_MARK;

public class AvroIdlAvoidNullableShorthandInspection extends BaseAvroIdlInspection<AvroIdlNullableType> {
	public AvroIdlAvoidNullableShorthandInspection() {
		super(AvroIdlNullableType.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlNullableType element, @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		if (ReplaceWithUnionQuickFix.isAvailableFor(element)) {
			ReplaceWithUnionQuickFix replaceWithUnion = new ReplaceWithUnionQuickFix(element);
			holder.registerProblem(element, TextBundle.message("inspection.avoid.nullable.shorthand.problem"), replaceWithUnion);
		}
	}

	private static class ReplaceWithUnionQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlNullableType> {
		public ReplaceWithUnionQuickFix(@NotNull AvroIdlNullableType element) {
			super(element, TextBundle.message("inspection.avoid.nullable.shorthand.fix"));
		}

		private static boolean isAvailableFor(@NotNull AvroIdlNullableType element) {
			return element.isOptional();
		}

		@Override
		protected boolean isAvailable(@NotNull Project project, @NotNull PsiFile file,
		                              @NotNull AvroIdlNullableType element) {
			return isAvailableFor(element);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
		                      @NotNull AvroIdlNullableType element) {
			final ASTNode questionMarkNode = TreeUtil.findChildBackward(element.getNode(), QUESTION_MARK);
			if (questionMarkNode == null) {
				// Not an optional type. We could have used AvroIdlType#isOptional(), but we need a reference to the question mark to remove it.
				return;
			}

			// Check if the type element is a variable type (i.e., not nested in an array/map/union),
			// and find the first default value (if any).
			final Optional<AvroIdlJsonValue> firstDefaultValue = ifType(element.getParent(),
					AvroIdlFieldDeclaration.class)
					.map(AvroIdlFieldDeclaration::getVariableDeclaratorList)
					.flatMap(List::stream)
					.map(AvroIdlVariableDeclarator::getJsonValue).filter(Objects::nonNull)
					.findFirst();
			// If the first default value is not null, put the null last in the union (we assume all default values will be non-null)
			final boolean shouldHaveNullLast = firstDefaultValue.map(PsiElement::getNode)
					.map(n -> n.findChildByType(NULL) == null).orElse(false);

			final PsiElement questionMark = questionMarkNode.getPsi();
			questionMark.delete();
			final AvroIdlUnionType unionType = new AvroIdlElementFactory(project).unionWithNull(element,
					shouldHaveNullLast);
			element.replace(unionType);
		}
	}
}
