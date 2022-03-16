package opwvhk.intellij.avro_idl.psi;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AvroIdlAnnotatedNameIdentifierOwner extends AvroIdlNameIdentifierOwner {
    @NotNull
    List<AvroIdlSchemaProperty> getSchemaPropertyList();
}
