package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.naming.AbstractNamingConventionInspection;
import com.intellij.codeInspection.naming.NamingConvention;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import opwvhk.intellij.avro_idl.naming.RenameFix;
import opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlNamingConventionInspection extends AbstractNamingConventionInspection<AvroIdlNameIdentifierOwner> {
	public static final ExtensionPointName<NamingConvention<AvroIdlNameIdentifierOwner>> EP_NAME =
		ExtensionPointName.create("opwvhk.avro.idl.naming.convention");

	public AvroIdlNamingConventionInspection() {
		super(EP_NAME.getExtensionList(), "AvroIdlNamingConvention");
		registerConventionsListener(EP_NAME);
	}

	@Override
	public void setEnabled(boolean value, String conventionShortName) {
		super.setEnabled(value, conventionShortName);
	}

	@Override
    @Nullable
    protected LocalQuickFix createRenameFix() {
		return new RenameFix();
	}

	@Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
		return new PsiElementVisitor() {
			@Override
			public void visitElement(@NotNull PsiElement element) {
				if (element instanceof AvroIdlNameIdentifierOwner) {
					AvroIdlNameIdentifierOwner owner = (AvroIdlNameIdentifierOwner)element;
					String name = owner.getName();
					if (name != null) {
						checkName(owner, name, holder);
					}
				}
			}
		};
	}
}
