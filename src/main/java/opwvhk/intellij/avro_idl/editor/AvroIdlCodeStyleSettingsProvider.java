package opwvhk.intellij.avro_idl.editor;

import javax.swing.*;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.application.options.codeStyle.CustomizableLanguageCodeStylePanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import org.jetbrains.annotations.NonNls;
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
			addTab(new AvroIdlLanguageSpecificPanel0(settings));
	    }

	    protected class AvroIdlLanguageSpecificPanel0 extends MyWrappingAndBracesPanel {
		    public AvroIdlLanguageSpecificPanel0(CodeStyleSettings settings) {
			    super(settings);
		    }

		    @Override
		    protected @NotNull String getTabTitle() {
			    return "Code Generation";
		    }

		    @Override
		    public LanguageCodeStyleSettingsProvider.SettingsType getSettingsType() {
			    return LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC;
		    }
	    }
	}
}
