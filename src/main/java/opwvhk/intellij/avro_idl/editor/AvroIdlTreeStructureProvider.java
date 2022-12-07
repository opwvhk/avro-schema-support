package opwvhk.intellij.avro_idl.editor;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.BasePsiNode;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class AvroIdlTreeStructureProvider implements TreeStructureProvider {
	@Override
    @NotNull
    public Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent, @NotNull Collection<AbstractTreeNode<?>> children,
                                                  ViewSettings settings) {
		boolean showMembers = settings.isShowMembers();
		if (!showMembers) {
			return children;
		}

		Collection<AbstractTreeNode<?>> result = new ArrayList<>();
		for (AbstractTreeNode<?> child : children) {
			if (child.getValue() instanceof AvroIdlFile) {
				result.add(new AvroIdlNode((PsiElement)child.getValue(), settings));
			} else {
				result.add(child);
			}
		}
		return result;
	}

	@Override
    @Nullable
    public Object getData(@NotNull Collection<AbstractTreeNode<?>> selected, @NotNull String dataId) {
		return TreeStructureProvider.super.getData(selected, dataId);
	}

	private static class AvroIdlNode extends BasePsiNode<PsiElement> {
		private final AvroIdlStructureViewElement structureViewElement;

		private AvroIdlNode(PsiElement psiElement, ViewSettings settings) {
			super(psiElement.getProject(), psiElement, settings);
			this.structureViewElement = new AvroIdlStructureViewElement(psiElement);
		}

		private AvroIdlNode(@NotNull Project project, @NotNull AvroIdlStructureViewElement structureViewElement, ViewSettings settings) {
			super(project, requireNonNull(structureViewElement.getValue()), settings);
			this.structureViewElement = structureViewElement;
		}

		@Override
        @Nullable
        protected Collection<AbstractTreeNode<?>> getChildrenImpl() {
			@NotNull List<StructureViewTreeElement> children = structureViewElement.getChildrenWithoutCustomRegions();
			if (children.size() == 0) {
				return null;
			}

			Collection<AbstractTreeNode<?>> result = new ArrayList<>(children.size());
			for (StructureViewTreeElement child : children) {
				AvroIdlStructureViewElement treeElement = (AvroIdlStructureViewElement)child;
				result.add(new AvroIdlNode(getProject(), treeElement, getSettings()));
			}
			return result;
		}

		@Override
		protected void updateImpl(@NotNull PresentationData data) {
			data.setPresentableText(structureViewElement.getPresentableText());
			data.setIcon(structureViewElement.getIcon(true));
			data.setLocationString(structureViewElement.getLocationString());
		}
	}
}
