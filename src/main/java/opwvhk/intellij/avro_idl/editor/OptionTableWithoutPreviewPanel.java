package opwvhk.intellij.avro_idl.editor;

import com.intellij.application.options.codeStyle.WrappingAndBracesPanel;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
public abstract class OptionTableWithoutPreviewPanel extends WrappingAndBracesPanel {
	private JPanel myPanel;

	protected OptionTableWithoutPreviewPanel(@Nullable CodeStyleSettings settings) {
		super(settings);
	}

	protected void init() {
		// Hack: this next line is copied from CustomizableLanguageCodeStylePanel, skipping our superclass
		customizeSettings();

		// We must initialize this field here, as this method is called by our superclass constructor (i.e., before our constructor/initializers run).
		myPanel = new JPanel(new BorderLayout());

		initTables();

		myTreeTable = createOptionsTree(getSettings());
		myTreeTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		JBScrollPane scrollPane = new JBScrollPane(myTreeTable) {
			@Override
			public Dimension getMinimumSize() {
				return super.getPreferredSize();
			}
		};
		myPanel.add(scrollPane);
		addPanelToWatch(myPanel);

		isFirstUpdate = false;
		customizeSettings();
	}

	@Override
	public JComponent getPanel() {
		return myPanel;
	}

	/**
	 * @return the settings type; called from the superclass constructor, so cannot be a field
	 */
	@Override
	public abstract @NotNull SettingsType getSettingsType();

	/**
	 * @return the tab title; called from the superclass constructor, so cannot be a field
	 */
	@Override
	protected abstract @NotNull String getTabTitle();

	/**
	 * @return the language this tab is for; called from the superclass constructor, so cannot be a field
	 */
	@Override
	public abstract @Nullable Language getDefaultLanguage();
}
