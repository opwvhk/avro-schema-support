package opwvhk.intellij.avro_idl.editor;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlStructureViewFactory implements PsiStructureViewFactory {

	@Override
    public @Nullable StructureViewBuilder getStructureViewBuilder(final @NotNull PsiFile psiFile) {
		return new TreeBasedStructureViewBuilder() {
			@Override
            public @NotNull StructureViewModel createStructureViewModel(@Nullable Editor editor) {
				return new AvroIdlStructureViewModel(psiFile);
			}
		};
	}

}
