package opwvhk.intellij.avro_idl;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.ScalableIcon;
import com.intellij.psi.PsiElement;
import com.intellij.ui.IconManager;
import com.intellij.ui.RowIcon;
import com.intellij.ui.icons.DarkIconProvider;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * The set of all icons used by the plugin for representing Apache Avro™ IDL.
 */
public class AvroIdlIcons {
	public static final Icon LOGO = IconManager.getInstance().getIcon("/META-INF/avroLogo16.svg", AvroIdlIcons.class);
	public static final Icon FILE = IconManager.getInstance().getIcon("/META-INF/fileIconAvro.svg", AvroIdlIcons.class);

	public static @Nullable Icon getAvroIdlIcon(PsiElement element) {
		if (element instanceof AvroIdlFile) {
			return FILE;
		}
		if (element instanceof AvroIdlEnumDeclaration) {
			return Nodes.AVRO_ENUM;
		}
		if (element instanceof AvroIdlRecordDeclaration && ((AvroIdlRecordDeclaration)element).isErrorType()) {
			return Nodes.AVRO_EXCEPTION;
		}
		if (element instanceof AvroIdlNamedSchemaDeclaration) { // Record & fixed (error & enum are matched above)
			return Nodes.AVRO_CLASS;
		}
		if (element instanceof AvroIdlVariableDeclarator) {
			boolean isField = element.getParent() instanceof AvroIdlFieldDeclaration;
			return isField ? Nodes.AVRO_FIELD : null;
		}
		if (element instanceof AvroIdlEnumConstant) {
			// Previously used: new LayeredIcon(AllIcons.Nodes.Field, AllIcons.Nodes.FinalMark, AllIcons.Nodes.StaticMark);
			return Nodes.AVRO_CONSTANT;
		}
		if (element instanceof AvroIdlImportDeclaration) {
			return AllIcons.Nodes.Include;
		}
		if (element instanceof AvroIdlProtocolDeclaration) {
			return Nodes.AVRO_INTERFACE;
		}
		if (element instanceof AvroIdlMessageDeclaration) {
			return Nodes.AVRO_METHOD;
		}
		return null;
	}

	public static final class Nodes {
		// Records & Fixed
		public static final Icon AVRO_CLASS = IconManager.getInstance().getIcon("/icons/nodes/class.svg", AvroIdlIcons.class);
		// Errors
		public static final Icon AVRO_EXCEPTION = IconManager.getInstance().getIcon("/icons/nodes/exception.svg", AvroIdlIcons.class);
		// Class fields
		public static final Icon AVRO_FIELD = IconManager.getInstance().getIcon("/icons/nodes/field.svg", AvroIdlIcons.class);
		// Enums
		public static final Icon AVRO_ENUM = IconManager.getInstance().getIcon("/icons/nodes/enum.svg", AvroIdlIcons.class);
		// Enum constants
		public static final Icon AVRO_CONSTANT = IconManager.getInstance().getIcon("/icons/nodes/constant.svg", AvroIdlIcons.class);
		// Protocols
		public static final Icon AVRO_INTERFACE = IconManager.getInstance().getIcon("/icons/nodes/interface.svg", AvroIdlIcons.class);
		// Protocol messages
		public static final Icon AVRO_METHOD = IconManager.getInstance().getIcon("/icons/nodes/method.svg", AvroIdlIcons.class);
	}

	public static @NotNull RowIcon addAvroMark(@NotNull Icon baseIcon) {
		Icon avroMark = IconLoader.getIcon("/icons/avroMark.svg", AvroIdlIcons.class);
		ScalableIcon avroMarkMask = (ScalableIcon)IconLoader.getIcon("/icons/avroMarkMask.svg", AvroIdlIcons.class);
		MaskedIcon maskedIcon = new MaskedIcon(baseIcon, avroMarkMask);
		return new RowIcon(maskedIcon, avroMark);
	}

	private static class MaskedIcon implements Icon, DarkIconProvider {
		private final Icon baseIcon;
		private final ScalableIcon mask;

		public MaskedIcon(@NotNull Icon baseIcon, @NotNull ScalableIcon mask) {
			this.baseIcon = baseIcon;
			this.mask = mask;
		}

		@Override
		public @NotNull Icon getDarkIcon(boolean isDark) {
			return new MaskedIcon(IconLoader.getDarkIcon(baseIcon, isDark), mask);
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D)g;
			mask.scale(1);
			float scale = Math.min(baseIcon.getIconHeight()/(float)mask.getIconHeight(), baseIcon.getIconWidth()/(float)mask.getIconWidth());
			mask.scale(scale);
			mask.paintIcon(c, g2, x, y);

			Composite saveComposite = g2.getComposite();
			// "Source In" effectively adds the alpha channel that's already there to the image we're painting: transparency can only increase.
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
			baseIcon.paintIcon(c, g2, x, y);
			g2.setComposite(saveComposite);
		}

		@Override
		public int getIconWidth() {
			return baseIcon.getIconWidth();
		}

		@Override
		public int getIconHeight() {
			return baseIcon.getIconHeight();
		}
	}
}
