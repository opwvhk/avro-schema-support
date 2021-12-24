package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import opwvhk.intellij.avro_idl.psi.AvroIdlElementFactory;
import opwvhk.intellij.avro_idl.psi.AvroIdlNullableType;
import opwvhk.intellij.avro_idl.psi.AvroIdlType;
import opwvhk.intellij.avro_idl.psi.AvroIdlUnionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AvroIdlUseNullableShorthandInspection extends BaseAvroIdlInspection<AvroIdlUnionType> {
	public AvroIdlUseNullableShorthandInspection() {
		super(AvroIdlUnionType.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlUnionType element, @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		final List<AvroIdlType> unionTypes = element.getTypeList();
		if (unionTypes.size() != 2) {
			return;
		}
		final AvroIdlType type1 = unionTypes.get(0);
		final AvroIdlType type2 = unionTypes.get(1);
		// Check if one of the two types is null, and set nonNullType to the other
		final AvroIdlType nonNullType = type1.isNull() ? !type2.isNull() ? type2 : null : type2.isNull() ? type1 : null;
		if (nonNullType instanceof AvroIdlNullableType) {
			final LocalQuickFix replaceWithShorthand = createQuickFix(element, "Replace union with shorthand notation",
				(project, file, elem) -> {
					final AvroIdlNullableType optionalType = new AvroIdlElementFactory(project).makeOptional((AvroIdlNullableType)nonNullType);
					elem.replace(optionalType);
				});
			holder.registerProblem(element, "Union can be simplified", replaceWithShorthand);
		}
	}
}
