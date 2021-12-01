package opwvhk.intellij.avro_idl.psi;

import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class AvroIdlFile extends PsiFileBase {
    public AvroIdlFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, AvroIdlLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return AvroIdlFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Avro IDL file";
    }
}
