package opwvhk.intellij.avro_idl.editor;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlStructureViewModel extends StructureViewModelBase
		implements StructureViewModel.ElementInfoProvider {

	public AvroIdlStructureViewModel(@NotNull PsiFile psiFile, @Nullable Editor editor) {
		super(psiFile, editor, new AvroIdlStructureViewElement(psiFile));
	}

	public Sorter @NotNull [] getSorters() {
		return new Sorter[]{Sorter.ALPHA_SORTER};
	}


	@Override
	public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
		return false;
	}

	@Override
	public boolean isAlwaysLeaf(StructureViewTreeElement element) {
		return false;
	}

	@Override
	protected Class<?> @NotNull [] getSuitableClasses() {
		return new Class<?>[]{
				AvroIdlFile.class,
				AvroIdlNamedSchemaDeclaration.class, AvroIdlVariableDeclarator.class, AvroIdlEnumConstant.class,
				AvroIdlProtocolDeclaration.class, AvroIdlMessageDeclaration.class
		};
	}

	@Override
	protected boolean isSuitable(PsiElement element) {
		if (super.isSuitable(element)) {
			return element.getParent() instanceof AvroIdlFieldDeclaration;
		}
		return false;
	}
}
