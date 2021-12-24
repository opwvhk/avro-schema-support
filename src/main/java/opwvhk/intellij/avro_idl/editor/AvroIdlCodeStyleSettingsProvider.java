package opwvhk.intellij.avro_idl.editor;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import org.jetbrains.annotations.NotNull;

public class AvroIdlCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
    public @NotNull CodeStyleConfigurable createConfigurable(@NotNull CodeStyleSettings settings, @NotNull CodeStyleSettings modelSettings) {
        return new CodeStyleAbstractConfigurable(settings, modelSettings, this.getConfigurableDisplayName()) {
            @Override
            protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
                return new AvroIdlCodeStyleMainPanel(getCurrentSettings(), settings);
            }
        };
    }

    @Override
    public String getConfigurableDisplayName() {
        return AvroIdlLanguage.INSTANCE.getDisplayName();
    }

    private static class AvroIdlCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
        public AvroIdlCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
            super(AvroIdlLanguage.INSTANCE, currentSettings, settings);
        }
    }
}
