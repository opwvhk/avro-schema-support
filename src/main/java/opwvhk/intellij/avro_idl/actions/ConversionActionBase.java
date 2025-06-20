package opwvhk.intellij.avro_idl.actions;

import com.intellij.build.BuildContentManager;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.ide.IdeBundle;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.UIBundle;
import com.intellij.ui.content.Content;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.*;

abstract class ConversionActionBase extends DumbAwareAction {
	private final LanguageFileType sourceFileType;
	private final LanguageFileType destinationFileType;
	protected final String actionTitle;
	private final String consoleTitle;

	protected ConversionActionBase(@NotNull @NlsActions.ActionText String actionTitle,
	                               @NotNull LanguageFileType sourceFileType,
	                               @NotNull LanguageFileType destinationFileType) {
		super((String) null);
		this.actionTitle = actionTitle;
		this.consoleTitle = sourceFileType.getDisplayName() + " to " + destinationFileType.getDisplayName();
		this.sourceFileType = sourceFileType;
		this.destinationFileType = destinationFileType;
	}

	@Override
	public @NotNull ActionUpdateThread getActionUpdateThread() {
		return ActionUpdateThread.BGT;
	}

	protected void convertFile(@NotNull Project project, @NotNull ConsoleView console, @NotNull VirtualFile file) {
		throw new UnsupportedOperationException(
				"Cannot convert file; please override convertFile(Project, ConsoleView, VirtualFile) or convertFiles(Project, ConsoleView, List<VirtualFile>)");
	}

	protected void convertFiles(@NotNull Project project, @NotNull ConsoleView console,
	                            @NotNull List<VirtualFile> files) {
		for (VirtualFile file : files) {
			convertFile(project, console, file);
		}
		console.print("\nAction complete.\n", ConsoleViewContentType.SYSTEM_OUTPUT);
	}

	@TestOnly
	public static VirtualFile targetDirectory = null;

	@Nullable
	protected VirtualFile askForTargetDirectory(@NotNull Project project,
	                                            @SuppressWarnings("SameParameterValue") @Nullable String title,
	                                            @SuppressWarnings("SameParameterValue") @Nullable String description,
	                                            @Nullable VirtualFile suggestedTargetDirectory) {
		if (ApplicationManager.getApplication().isUnitTestMode()) {
			// Tests have no UI, and we don't want to manually fill in a dialog during tests anyway.
			return targetDirectory;
		}
		final String nonNullTitle = title == null ? UIBundle.message("file.chooser.default.title") : title;
		final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
				.withTitle(nonNullTitle);
		if (description != null) {
			descriptor.withDescription(description);
		}
		return FileChooser.chooseFile(descriptor, project, suggestedTargetDirectory);
	}

	@TestOnly
	public static VirtualFileWrapper targetFile = null;

	@Nullable
	protected VirtualFileWrapper askForTargetFile(@NotNull Project project, @Nullable String title,
	                                              @Nullable String description,
	                                              @NotNull FileType fileType,
	                                              @Nullable VirtualFile suggestedTargetDirectory,
	                                              @NotNull String suggestedBaseName) {
		if (ApplicationManager.getApplication().isUnitTestMode()) {
			// Tests have no UI, and we don't want to manually fill in a dialog during tests anyway.
			return targetFile;
		}
		//noinspection DialogTitleCapitalization: from IDE message bundle, so correct enough
		final String nonNullTitle = title == null ? IdeBundle.message("dialog.title.save.as") : title;
		final String nonNullDescription =
				description == null ? IdeBundle.message("label.choose.target.file") : description;
		final String fileName = suggestedBaseName + "." + destinationFileType.getDefaultExtension();

		// Replacement for getVirtualFilesByName is not available in 2020.3.4
		GlobalSearchScope contentScope = ProjectScope.getContentScope(project);
		Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(fileName, contentScope);
		VirtualFile firstItem = ContainerUtil.getFirstItem(files);
		VirtualFile baseDir = firstItem != null ? firstItem.getParent() : suggestedTargetDirectory;

		// Note: the code in use is deprecated for IntellIJ 2025.x Use the commented out code instead.
		//FileChooserDescriptor chooseDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor(fileType)
		//		.withTitle(nonNullTitle).withDescription(nonNullDescription);
		//FileSaverDescriptor saveDescriptor = new FileSaverDescriptor(chooseDescriptor);
		FileSaverDescriptor saveDescriptor = new FileSaverDescriptor(nonNullTitle, nonNullDescription);
		// withFileFilter() returns 'this'
		saveDescriptor.withFileFilter(file -> FileTypeRegistry.getInstance().isFileOfType(file, fileType));

		return FileChooserFactory.getInstance().createSaveFileDialog(saveDescriptor, project).save(baseDir, fileName);
	}

	/**
	 * Find the first registered extensions for a file type. If none found (or if they're all hidden in wildcard patterns), returns the default extension.
	 *
	 * @param fileType a registered file type
	 * @return all registered extensions
	 */
	@SuppressWarnings("SameParameterValue")
	@NotNull
	protected String findExtensionFor(@NotNull FileType fileType) {
		return findExtensionsFor(fileType).get(0);
	}

	/**
	 * Find all registered extensions for a file type. If none found (or if they're all hidden in wildcard patterns), returns the default extension.
	 *
	 * @param fileType a registered file type
	 * @return all registered extensions, or the default extension if none found
	 */
	@NotNull
	protected List<String> findExtensionsFor(@NotNull FileType fileType) {
		final List<String> result = new ArrayList<>();
		final List<FileNameMatcher> associations = FileTypeManager.getInstance().getAssociations(fileType);
		for (FileNameMatcher association : associations) {
			if (association instanceof ExtensionFileNameMatcher extensionFileNameMatcher) {
				result.add(extensionFileNameMatcher.getExtension());
			}
		}
		if (result.isEmpty()) {
			// No associated extension found (if it is hidden in a wildcard matcher, we cannot find it): use the default.
			return singletonList(fileType.getDefaultExtension());
		} else {
			return unmodifiableList(result);
		}
	}

	public void update(@NotNull AnActionEvent e) {
		Project project = e.getProject();
		boolean actionAvailable = false;

		if (project != null && !project.isDisposed()) {
			List<VirtualFile> files = getFiles(e);
			actionAvailable = !files.isEmpty();
		}

		e.getPresentation().setEnabledAndVisible(actionAvailable);
	}

	@NotNull
	private List<VirtualFile> getFiles(@NotNull AnActionEvent e) {
		final VirtualFile[] virtualFiles = e.getData(LangDataKeys.VIRTUAL_FILE_ARRAY);
		if (virtualFiles == null) {
			return emptyList();
		}
		Project project = e.getProject();
		return Stream.of(virtualFiles).filter(file -> canConvert(file, project)).collect(Collectors.toList());
	}

	protected boolean canConvert(@NotNull VirtualFile file, Project project) {
		// We can process any file (not a virtual file or injected code) that is of the right type.
		return !file.isDirectory() && !(file instanceof VirtualFileWindow) && file.getFileType() == sourceFileType;
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		Project project = e.getProject();
		List<VirtualFile> files = getFiles(e);
		if (project == null || files.isEmpty()) {
			return;
		}

		// Ensure we're working with the latest versions
		PsiDocumentManager.getInstance(project).commitAllDocuments();
		FileDocumentManager.getInstance().saveAllDocuments();

		ConsoleView console = openConsole(project);
		convertFiles(project, console, files);

		// Ensure we have the latest changes available
		PsiDocumentManager.getInstance(project).commitAllDocuments();
		FileDocumentManager.getInstance().saveAllDocuments();
	}

	@NotNull
	protected ConsoleView openConsole(@NotNull Project project) {
		final ToolWindow buildToolWindow = BuildContentManager.getInstance(project).getOrCreateToolWindow();
		Content content = buildToolWindow.getContentManager().findContent(consoleTitle);
		ConsoleView console =
				content == null ? null : UIUtil.uiTraverser(content.getComponent()).filter(ConsoleView.class).first();
		if (content != null && console != null) {
			// The console already exists. Clear it.
			console.clear();
		} else {
			// The console doesn't exist yet. Create it.
			console = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();

			JComponent panel = new JPanel(new BorderLayout());
			panel.add(console.getComponent(), BorderLayout.CENTER);
			DefaultActionGroup toolbarActions = new DefaultActionGroup();
			for (AnAction action : console.createConsoleActions()) {
				toolbarActions.add(action);
			}

			ActionToolbar toolbar = ActionManager.getInstance()
					.createActionToolbar(ActionPlaces.TOOLBAR, toolbarActions, false);
			toolbar.setTargetComponent(console.getComponent());
			panel.add(toolbar.getComponent(), BorderLayout.WEST);

			content = buildToolWindow.getContentManager().getFactory().createContent(panel, consoleTitle, true);
			buildToolWindow.getContentManager().addContent(content);
			Disposer.register(content, console);
		}

		final Content activeContent = content;
		buildToolWindow.activate(() -> buildToolWindow.getContentManager().setSelectedContent(activeContent), false,
				false);
		return console;
	}

	protected void writeStackTrace(@NotNull ConsoleView console, @NotNull Throwable throwable) {
		StringWriter buffer = new StringWriter();
		throwable.printStackTrace(new PrintWriter(buffer));
		buffer.write('\n');
		console.print(buffer.toString(), ConsoleViewContentType.ERROR_OUTPUT);
	}
}
