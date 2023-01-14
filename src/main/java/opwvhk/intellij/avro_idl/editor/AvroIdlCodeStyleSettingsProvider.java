package opwvhk.intellij.avro_idl.editor;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.application.options.codeStyle.OptionTableWithPreviewPanel;
import com.intellij.application.options.codeStyle.WrappingAndBracesPanel;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
	@Override
	public @Nullable CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
		return new AvroIdlCodeStyleSettings(settings);
	}

	@Override
	public String getConfigurableDisplayName() {
		return AvroIdlLanguage.INSTANCE.getDisplayName();
	}

	@NotNull
	public CodeStyleConfigurable createConfigurable(@NotNull CodeStyleSettings settings, @NotNull CodeStyleSettings modelSettings) {
		return new CodeStyleAbstractConfigurable(settings, modelSettings, this.getConfigurableDisplayName()) {
			@Override
			protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
				return new AvroIdlCodeStyleMainPanel(getCurrentSettings(), settings);
			}
		};
	}

	private static class AvroIdlCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
		public AvroIdlCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
			super(AvroIdlLanguage.INSTANCE, currentSettings, settings);
		}

		@Override
		protected void initTabs(CodeStyleSettings settings) {
			super.initTabs(settings);
			addTab(new OptionTableWithoutPreviewPanel(SettingsType.LANGUAGE_SPECIFIC, getDefaultLanguage(), settings));
		}
	}
}
