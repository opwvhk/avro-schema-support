package opwvhk.intellij.avro_idl.editor;

import javax.swing.*;
import java.awt.*;

import com.intellij.application.options.codeStyle.WrappingAndBracesPanel;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptionTableWithoutPreviewPanel extends WrappingAndBracesPanel {
	private JPanel myPanel;
	private final @Nullable Language defaultLanguage;
	private final @NotNull String tabTitle;
	private final @NotNull SettingsType settingsType;

	protected OptionTableWithoutPreviewPanel(@NotNull SettingsType settingsType, @Nullable Language defaultLanguage, @Nullable CodeStyleSettings settings) {
		this(settingsType, defaultLanguage, null, settings);
	}

	protected OptionTableWithoutPreviewPanel(@NotNull SettingsType settingsType, @Nullable Language defaultLanguage, @Nullable String tabTitle,
	                                         @Nullable CodeStyleSettings settings) {
		super(settings);
		this.defaultLanguage = defaultLanguage;
		this.tabTitle = tabTitle == null ? ApplicationBundle.message("settings.code.style.tab.title.other") : tabTitle;
		this.settingsType = settingsType;
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

	@Override
	public @NotNull SettingsType getSettingsType() {
		return settingsType;
	}

	@Override
	protected @NotNull String getTabTitle() {
		return tabTitle;
	}

	@Override
	public @Nullable Language getDefaultLanguage() {
		return defaultLanguage;
	}
}
