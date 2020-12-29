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

	@Nullable
	@Override
	public StructureViewBuilder getStructureViewBuilder(@NotNull final PsiFile psiFile) {
		return new TreeBasedStructureViewBuilder() {
			@NotNull
			@Override
			public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
				return new AvroIdlStructureViewModel(psiFile);
			}
		};
	}

}
