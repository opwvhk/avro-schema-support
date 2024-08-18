package opwvhk.intellij.avro_idl;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.ui.IconManager;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * The set of all icons used by the plugin for representing Apache Avro™ IDL.
 */
public class AvroIdlIcons {
	// For newer platform versions, we must use the classloader instead of the class.
	//private static final ClassLoader MY_CLASS_LOADER = AvroIdlIcons.class.getClassLoader();
	private static final Class<?> MY_CLASS_LOADER = AvroIdlIcons.class;
	private static final IconManager ICON_MANAGER = IconManager.getInstance();
	public static final Icon PLUGIN_ICON = ICON_MANAGER.getIcon("/icons/pigeon.svg", MY_CLASS_LOADER);
	public static final Icon AVRO_LOGO = ICON_MANAGER.getIcon("/icons/avroLogo16.svg", MY_CLASS_LOADER);
	public static final Icon AVDL_FILE = AVRO_LOGO;
	public static final Icon AVSC_FILE = AVRO_LOGO;
	public static final Icon AVPR_FILE = PLUGIN_ICON;

	@Nullable
	public static Icon getAvroIdlIcon(PsiElement element) {
		if (element instanceof AvroIdlFile) {
			return AVDL_FILE;
		}
		if (element instanceof AvroIdlRecordDeclaration) {
			return ((AvroIdlRecordDeclaration) element).isErrorType() ? Nodes.AVRO_ERROR : Nodes.AVRO_RECORD;
		}
		if (element instanceof AvroIdlEnumDeclaration) {
			return Nodes.AVRO_ENUM;
		}
		if (element instanceof AvroIdlEnumConstant) {
			return Nodes.AVRO_SYMBOL;
		}
		if (element instanceof AvroIdlNamedSchemaDeclaration) { // Fixed (record, error & enum are matched above)
			return Nodes.AVRO_FIXED;
		}
		if (element instanceof AvroIdlVariableDeclarator) {
			boolean isField = element.getParent() instanceof AvroIdlFieldDeclaration;
			return isField ? Nodes.AVRO_FIELD : null;
		}
		if (element instanceof AvroIdlNamespaceDeclaration) {
			return AllIcons.Nodes.CustomRegion;
		}
		if (element instanceof AvroIdlImportDeclaration) {
			return AllIcons.Nodes.Include;
		}
		if (element instanceof AvroIdlProtocolDeclaration) {
			return Nodes.AVRO_PROTOCOL;
		}
		if (element instanceof AvroIdlMessageDeclaration) {
			return Nodes.AVRO_MESSAGE;
		}
		return null;
	}

	public static final class Nodes {
		public static final Icon AVRO_RECORD = AVRO_LOGO;
		public static final Icon AVRO_ERROR = ICON_MANAGER.getIcon("/icons/nodes/error.svg", MY_CLASS_LOADER);
		public static final Icon AVRO_FIELD = ICON_MANAGER.getIcon("/icons/nodes/field.svg", MY_CLASS_LOADER);
		public static final Icon AVRO_FIXED = ICON_MANAGER.getIcon("/icons/nodes/fixed.svg", MY_CLASS_LOADER);
		public static final Icon AVRO_ENUM = ICON_MANAGER.getIcon("/icons/nodes/enum.svg", MY_CLASS_LOADER);
		public static final Icon AVRO_SYMBOL = ICON_MANAGER.getIcon("/icons/nodes/enum_symbol.svg", MY_CLASS_LOADER);
		public static final Icon AVRO_PROTOCOL = ICON_MANAGER.getIcon("/icons/nodes/protocol.svg", MY_CLASS_LOADER);
		public static final Icon AVRO_MESSAGE = ICON_MANAGER.getIcon("/icons/nodes/message.svg", MY_CLASS_LOADER);
	}
}
