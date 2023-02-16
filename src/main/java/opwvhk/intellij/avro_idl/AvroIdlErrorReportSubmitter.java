package opwvhk.intellij.avro_idl;

import com.intellij.diagnostic.DiagnosticBundle;
import com.intellij.ide.DataManager;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kohsuke.github.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.*;

public class AvroIdlErrorReportSubmitter extends ErrorReportSubmitter {
	private static final Pattern EXCEPTION_MESSAGE = Pattern.compile("^[^:]++: ((?:(?!\\R\t).)*+)\\R\t");
	private static final Pattern FIRST_STACK_LINE = Pattern.compile("^(?:(?!\\R\t).)*+\\R\t((?:(?!\\R\t).)*+)\\R\t");
	private static final String MARKDOWN_GITHUB_FAILURE = "Please file this bug report at: https://github.com/%s/issues/new\n\n%s";

	private final String repository = "opwvhk/avro-schema-support";

	@Override
	public @Nullable String getPrivacyNoticeText() {
		return "<html>I agree to my hardware configuration, software configuration, product information, and the error details shown above " +
				"being published in the GitHub repository <a href=\"https://github.com/" + repository + "/issues\">" +
				repository + "</a> " +
				"to allow volunteers provide support if they have time.  \n" +
				"The reported exception data are not expected to contain any personal data.</html>";
	}

	@Override
	public @NotNull
	@NlsActions.ActionText String getReportActionText() {
		return DiagnosticBundle.message("error.report.impossible.action");
	}

	@Override
	public boolean submit(IdeaLoggingEvent @NotNull [] events, @Nullable String additionalInfo,
	                      @NotNull Component parentComponent,
	                      @NotNull Consumer<? super SubmittedReportInfo> consumer) {
		SubmittedReportInfo reportInfo;
		if (events.length == 0 && additionalInfo == null) {
			// Nothing to submit
			reportInfo = new SubmittedReportInfo(null, null, FAILED);
		} else {
			// The current implementation has only a single event...

			String firstThrowableText = events[0].getThrowableText();
			String searchTerm = getFirstMatchingGroup(FIRST_STACK_LINE, firstThrowableText);
			String errorMessage = getFirstMatchingGroup(EXCEPTION_MESSAGE, firstThrowableText);
			String markdownText = createCrashReportMarkdown(events, additionalInfo);

			try {
				GitHub gitHub = connectToGitHub();
				GHIssue issue = findIssue(gitHub, searchTerm);
				boolean duplicate = issue != null;
				if (!duplicate) {
					issue = createIssue(gitHub, "Crash Report " + getExceptionMessage(errorMessage), markdownText,
							"crash report");
				}
				String issueUrl = issue.getHtmlUrl().toExternalForm();
				int issueNumber = issue.getNumber();
				reportInfo = new SubmittedReportInfo(issueUrl, "issue " + issueNumber,
						duplicate ? DUPLICATE : NEW_ISSUE);
			} catch (Exception e) {
				reportInfo = new SubmittedReportInfo(null, null, FAILED);
				String markdownErrorReport = String.format(MARKDOWN_GITHUB_FAILURE, repository, markdownText);
				ApplicationManager.getApplication()
						.invokeLater(() -> openInScratchFile(parentComponent, markdownErrorReport),
								ModalityState.NON_MODAL);
			}
		}
		consumer.consume(reportInfo);
		return true;
	}

	private static @NotNull String createCrashReportMarkdown(IdeaLoggingEvent @NotNull [] events,
	                                                         @Nullable String additionalInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append("# Crash Report\n");
		builder.append("\n");
		builder.append("## What happened\n");
		builder.append("\n");
		builder.append(
						additionalInfo != null ? additionalInfo.trim() : "(The user did not submit additional information)")
				.append("\n");

		builder.append("\n");
		builder.append("\n");
		builder.append("## Context information\n");
		builder.append("\n");

		// Plugin version
		appendPluginByDescriptor(builder, "Plugin", AvroIdlPluginUpdateStartupActivity.getMyPluginDescriptor());
		appendPluginByDescriptor(builder, "Extra",
				PluginManagerCore.getPlugin(AvroIdlPluginUpdateStartupActivity.OLD_PLUGIN_ID));
		// IntelliJ version
		ApplicationInfo info = ApplicationInfo.getInstance();
		builder.append("* IDE: ").append(info.getVersionName()).append(" `").append(info.getFullVersion())
				.append("`\n");
		// Java VM
		builder.append("* JVM: ")
				.append(System.getProperty("java.vm.name")).append(" (")
				.append(System.getProperty("java.vm.vendor")).append(") `")
				.append(System.getProperty("java.vm.version")).append("`\n");
		// Operating system
		builder.append("* OS: ")
				.append(System.getProperty("os.name")).append(" (")
				.append(System.getProperty("os.arch")).append(") `")
				.append(System.getProperty("os.version")).append("`\n");

		for (IdeaLoggingEvent event : events) {
			builder.append("\n");
			builder.append("\n");
			builder.append("## Exception\n");
			builder.append("\n");
			String message = event.getMessage();
			if (message != null) {
				builder.append(message).append("\n");
				builder.append("\n");
			}

			if (event.getThrowable() != null) {
				String stackTrace = event.getThrowableText().trim();
				builder.append("```\n");
				builder.append(stackTrace).append("\n");
				builder.append("```\n");
			}
		}
		String markdownText = builder.toString();
		return markdownText;
	}

	private static void appendPluginByDescriptor(StringBuilder builder, String label, IdeaPluginDescriptor descriptor) {
		if (descriptor != null) {
			builder.append("* ").append(label).append(": ")
					.append(" ").append(descriptor.getName())
					.append(" `").append(descriptor.getVersion())
					.append("` by ").append(descriptor.getVendor())
					.append(" (id: ").append(descriptor.getPluginId().getIdString())
					.append(PluginManagerCore.isDisabled(descriptor.getPluginId()) ? "; disabled" : "")
					.append(")\n");
		}
	}

	private static String getExceptionMessage(CharSequence text) {
		return getFirstMatchingGroup(EXCEPTION_MESSAGE, text);
	}

	private static String getFirstMatchingGroup(Pattern pattern, CharSequence text) {
		Matcher matcher = pattern.matcher(text);
		return matcher.find() ? matcher.group(1) : null;
	}

	private GitHub connectToGitHub() throws IOException {
		Properties props = new Properties();
		try (InputStream resource = getClass().getResourceAsStream("/META-INF/github.properties")) {
			props.load(resource);
		}
		return GitHubBuilder.fromProperties(props).build();
	}

	private GHIssue findIssue(GitHub gitHub, String searchString) throws IOException {
		GHIssueSearchBuilder searchBuilder = gitHub.searchIssues()
				.sort(GHIssueSearchBuilder.Sort.CREATED).order(GHDirection.ASC)
				.q("repo:" + repository)
				.q("is:issue")
				.q("in:body")
				.q(searchString);
		PagedSearchIterable<GHIssue> ghIssues = searchBuilder.list().withPageSize(1);
		int issuesReported = ghIssues.getTotalCount();
		if (issuesReported == 0) {
			return null;
		} else {
			return ghIssues.toList().get(0);
		}
	}

	private GHIssue createIssue(GitHub gitHub, String title, String markdownBody, String... labels) throws IOException {
		GHIssueBuilder issueBuilder = gitHub.getRepository(repository).createIssue(title);
		issueBuilder.body(markdownBody);
		for (String label : labels) {
			issueBuilder.label(label);
		}
		GHIssue issue = issueBuilder.create();
		return issue;
	}

	private static void openInScratchFile(@NotNull Component parentComponent, String text) {
		DataContext dataContext = DataManager.getInstance().getDataContext(parentComponent);
		Project project = PlatformDataKeys.PROJECT.getData(dataContext);
		if (project == null) {
			project = ProjectManager.getInstance().getDefaultProject();
		}
		// Create scratch file.
		ScratchRootType scratchRoot = ScratchRootType.getInstance();
		String fileName = hasWorkingJCEF() ? "bug-report.md" : "bug-report.txt";
		VirtualFile file = scratchRoot.createScratchFile(project, fileName, Language.ANY, text);
		if (file != null) {
			FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, file), true);
		}
	}

	private static boolean hasWorkingJCEF() {
		try {
			if (!JBCefApp.isSupported()) {
				return false;
			}
			JBCefApp.getInstance();
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
}
