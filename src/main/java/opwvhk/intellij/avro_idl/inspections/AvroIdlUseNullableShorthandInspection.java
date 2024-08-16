package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.AvroIdlElementFactory;
import opwvhk.intellij.avro_idl.psi.AvroIdlNullableType;
import opwvhk.intellij.avro_idl.psi.AvroIdlType;
import opwvhk.intellij.avro_idl.psi.AvroIdlUnionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AvroIdlUseNullableShorthandInspection extends BaseAvroIdlInspection<AvroIdlUnionType> {
	public AvroIdlUseNullableShorthandInspection() {
		super(AvroIdlUnionType.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlUnionType element, @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		if (ReplaceWithShorthandQuickFix.isAvailableFor(element)) {
			ReplaceWithShorthandQuickFix replaceWithShorthand = new ReplaceWithShorthandQuickFix(element);
			holder.registerProblem(element, TextBundle.message("inspection.use.nullable.shorthand"), replaceWithShorthand);
		}
	}

	private static class ReplaceWithShorthandQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlUnionType> {
		public ReplaceWithShorthandQuickFix(@NotNull AvroIdlUnionType element) {
			super(element, TextBundle.message("inspection.use.nullable.shorthand.fix"));
		}

		@Nullable
		private static AvroIdlType getNonNullType(@NotNull AvroIdlUnionType element) {
			final List<AvroIdlType> unionTypes = element.getTypeList();
			if (unionTypes.size() != 2) {
				return null;
			}
			final AvroIdlType type1 = unionTypes.get(0);
			final AvroIdlType type2 = unionTypes.get(1);
			// Check if one of the two types is the null type, and return the other
			return type1.isNull() ? !type2.isNull() ? type2 : null : type2.isNull() ? type1 : null;
		}

		private static boolean isAvailableFor(@NotNull AvroIdlUnionType element) {
			return getNonNullType(element) instanceof AvroIdlNullableType;
		}

		@Override
		protected boolean isAvailable(@NotNull Project project, @NotNull PsiFile file,
		                              @NotNull AvroIdlUnionType element) {
			return isAvailableFor(element);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
		                      @NotNull AvroIdlUnionType element) {
			AvroIdlType nonNullType = getNonNullType(element);
			if (nonNullType instanceof AvroIdlNullableType) {
				final AvroIdlNullableType optionalType = new AvroIdlElementFactory(project).makeOptional(
						(AvroIdlNullableType) nonNullType);
				element.replace(optionalType);
			}
		}
	}
}
