package opwvhk.intellij.avro_idl.editor;

import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlPairedBraceMatcher implements PairedBraceMatcher {
    @Override
    public BracePair[] getPairs() {
        return new BracePair[] {
                new BracePair(AvroIdlTypes.LEFT_BRACE, AvroIdlTypes.RIGHT_BRACE, true),
                new BracePair(AvroIdlTypes.LEFT_BRACKET, AvroIdlTypes.RIGHT_BRACKET, false),
                new BracePair(AvroIdlTypes.LEFT_PAREN, AvroIdlTypes.LEFT_PAREN, false),
                new BracePair(AvroIdlTypes.LEFT_ANGLE, AvroIdlTypes.RIGHT_ANGLE, false)
        };
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return 0;
    }
}
