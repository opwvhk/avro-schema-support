package opwvhk.intellij.avro_idl.syntax;

import com.intellij.lexer.FlexAdapter;

public class AvroIdlLexer extends FlexAdapter {
    public AvroIdlLexer() {
        super(new _AvroIdlLexer(null));
    }
}
