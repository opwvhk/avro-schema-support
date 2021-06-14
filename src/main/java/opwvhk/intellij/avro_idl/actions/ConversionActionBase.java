package opwvhk.intellij.avro_idl.actions;

import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.*;
import com.intellij.project.ProjectKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

abstract class ConversionActionBase extends AnAction implements DumbAware {

	private final LanguageFileType sourceFileType;
	private final LanguageFileType destinationFileType;

	protected ConversionActionBase(@NotNull @NlsActions.ActionText String text, LanguageFileType sourceFileType, LanguageFileType destinationFileType) {
		super(text);
		this.sourceFileType = sourceFileType;
		this.destinationFileType = destinationFileType;
	}

	protected abstract void convertFile(Project project, VirtualFile file);

	protected VirtualFile writeSiblingFile(@NotNull VirtualFile siblingFile, byte[] content) throws IOException {
		return writeSiblingFile(siblingFile, null, content);
	}

	protected VirtualFile writeSiblingFile(@NotNull VirtualFile siblingFile, @Nullable String nameWithoutExtension, byte[] content) throws IOException {
		final VirtualFile file = createSiblingFile(siblingFile, nameWithoutExtension);
		file.setBinaryContent(content);
		return file;
	}

	protected VirtualFile createSiblingFile(@NotNull VirtualFile siblingFile) throws IOException {
		return createSiblingFile(siblingFile, null);
	}

	protected VirtualFile createSiblingFile(@NotNull VirtualFile siblingFile, @Nullable String nameWithoutExtension) throws IOException {
		if (nameWithoutExtension == null) {
			nameWithoutExtension = siblingFile.getNameWithoutExtension();
		}
		return VfsUtil.createChildSequent(this, siblingFile.getParent(), nameWithoutExtension, destinationFileType.getDefaultExtension());
	}

	@Override
	public void update(AnActionEvent e) {
		// This action is available in projects if any of the selected files is of the specified type.
		final DataContext dataContext = e.getDataContext();
		final Presentation presentation = e.getPresentation();

		final Project project = CommonDataKeys.PROJECT.getData(dataContext);
		if (project != null) {
			final VirtualFile[] virtualFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
			presentation.setEnabledAndVisible(virtualFiles != null && canProcessAny(project, virtualFiles));
		} else {
			presentation.setEnabledAndVisible(false);
		}
	}

	public boolean canProcessAny(@NotNull Project project, VirtualFile[] virtualFiles) {
		final Ref<Boolean> canProcessAny = Ref.create(false);
		final VirtualFileVisitor<Void> fileVisitor = createFileVisitor(project, file -> {
			if (canConvert(file)) {
				canProcessAny.set(true);
			}
		});
		for (VirtualFile file : virtualFiles) {
			if (file != null) {
				VfsUtilCore.visitChildrenRecursively(file, fileVisitor);
			}
		}
		return canProcessAny.get();
	}

	public boolean canConvert(@NotNull VirtualFile file) {
		// We can process any file in a writable directory (not injected) that is of the right type.
		return !file.isDirectory() && isInWritableDirectory(file) && !(file instanceof VirtualFileWindow) && file.getFileType() == sourceFileType;
	}

	private boolean isInWritableDirectory(@NotNull VirtualFile file) {
		return file.getParent() != null && file.getParent().isWritable();
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		// Using the event, implement an action. For example, create and show a dialog.
		final Project project = getEventProject(e);
		if (project == null) {
			return;
		}

		final VirtualFile[] virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
		if (virtualFiles == null) {
			return;
		}

		final VirtualFileVisitor<Void> fileVisitor = createFileVisitor(project, file -> {
			if (canConvert(file)) {
				WriteAction.run(() -> convertFile(project, file));
			}
		});
		for (VirtualFile file : virtualFiles) {
			if (file != null) {
				VfsUtilCore.visitChildrenRecursively(file, fileVisitor);
			}
		}
	}

	/**
	 * Create a VirtualFileVisitor that visits files in the project recursively, skipping ignored files and project directories.
	 *
	 * @param project   the current project
	 * @param processor the processor; returns to skip further processing
	 */
	private VirtualFileVisitor<Void> createFileVisitor(Project project, Consumer<VirtualFile> processor) {
		Path directoryStorePath = ProjectKt.getStateStore(project).getDirectoryStorePath();
		VirtualFile projectVirtualDirectory = directoryStorePath == null ? null : StandardFileSystems.local()
			.findFileByPath(FileUtil.toSystemIndependentName(directoryStorePath.toString()));
		FileTypeRegistry fileTypeManager = FileTypeRegistry.getInstance();

		return new VirtualFileVisitor<>() {
			@NotNull
			@Override
			public Result visitFileEx(@NotNull VirtualFile file) {
				if (file.isDirectory()) {
					return (!file.equals(projectVirtualDirectory) && !fileTypeManager.isFileIgnored(file)) ? CONTINUE : SKIP_CHILDREN;
				} else {
					processor.accept(file);
					return CONTINUE;
				}
			}
		};
	}

}
