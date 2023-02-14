package opwvhk.intellij.avro_idl.editor;

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AvroIdlStructureAwareNavbar extends StructureAwareNavBarModelExtension {
	@Override
	@NotNull
	protected Language getLanguage() {
		return AvroIdlLanguage.INSTANCE;
	}

	@Override
	@Nullable
	public String getPresentableText(Object object) {
		if (object instanceof AvroIdlFile) {
			return ((AvroIdlFile) object).getName();
		}
		if (object instanceof AvroIdlNameIdentifierOwner) {
			final String name = ((AvroIdlNameIdentifierOwner) object).getName();
			return name;
		}
		return null;
	}

	@Override
	@Nullable
	public Icon getIcon(Object object) {
		if (object instanceof PsiElement) {
			return AvroIdlIcons.getAvroIdlIcon((PsiElement) object);
		}
		return null;
	}
}
