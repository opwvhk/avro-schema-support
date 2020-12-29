package opwvhk.intellij.avro_idl.editor;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import org.jetbrains.annotations.NotNull;

public class AvroIdlStructureViewModel extends StructureViewModelBase implements
	StructureViewModel.ElementInfoProvider {

	public AvroIdlStructureViewModel(PsiFile psiFile) {
		super(psiFile, new AvroIdlStructureViewElement(psiFile));
	}

	@NotNull
	public Sorter[] getSorters() {
		return new Sorter[]{Sorter.ALPHA_SORTER};
	}


	@Override
	public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
		return false;
	}

	@Override
	public boolean isAlwaysLeaf(StructureViewTreeElement element) {
		return element instanceof AvroIdlFile;
	}

}
