package opwvhk.intellij.avro_idl;

import com.intellij.ide.DataManager;
import com.intellij.ide.actions.OpenFileAction;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.util.Consumer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kohsuke.github.GHApp;
import org.kohsuke.github.GHAppInstallation;
import org.kohsuke.github.GHAppInstallationToken;
import org.kohsuke.github.GHDirection;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHIssueSearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedSearchIterable;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.*;

public class AvroIdlErrorReportSubmitter extends ErrorReportSubmitter {
	private static final Pattern EXCEPTION_MESSAGE = Pattern.compile("^[^:]++:\\s*+((?:(?!\\R).)*+)");
	private static final Pattern FIRST_STACK_LINE = Pattern.compile("^(?:(?!\\R).)*+\\R\\s*+((?:(?!\\R).)*+)");

	private final String repository = "opwvhk/avro-schema-support";

	@Override
	public String getPrivacyNoticeText() {
		return TextBundle.message("error.reporter.notice.anonymous", repository);
	}

	@Override
	public @NotNull
	@NlsActions.ActionText String getReportActionText() {
		return TextBundle.diagnosticMessage("error.report.impossible.action");
	}

	@Override
	public boolean submit(IdeaLoggingEvent @NotNull [] events, @Nullable String additionalInfo,
	                      @NotNull Component parentComponent,
	                      @NotNull Consumer<? super SubmittedReportInfo> consumer) {
		ApplicationManager.getApplication().invokeLater(() -> {
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
						issue = createCrashReport(gitHub, errorMessage, markdownText);
					}
					String issueUrl = issue.getHtmlUrl().toExternalForm();
					int issueNumber = issue.getNumber();
					reportInfo = new SubmittedReportInfo(issueUrl, "issue " + issueNumber,
							duplicate ? DUPLICATE : NEW_ISSUE);
				} catch (Exception e) {
					reportInfo = new SubmittedReportInfo(null, null, FAILED);
					String markdownErrorReport = TextBundle.message("error.reporter.failure.manual.fix", repository,
							markdownText);
					ApplicationManager.getApplication()
							.invokeLater(() -> openInScratchFile(parentComponent, markdownErrorReport));
				}
			}
			consumer.consume(reportInfo);
		});
		return true;
	}

	private static @NotNull String createCrashReportMarkdown(@NotNull IdeaLoggingEvent[] events,
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
		appendPluginByDescriptor(builder, "Plugin", AvroIdlPluginUtils.getMyPluginDescriptor());
		appendPluginByDescriptor(builder, "Extra", AvroIdlPluginUtils.getConflictingPluginDescriptor());
		appendPluginByDescriptor(builder, "Extra", PluginManagerCore.getPlugin(PluginId.getId("sparql4idea")));
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

	private static String getFirstMatchingGroup(Pattern pattern, CharSequence text) {
		Matcher matcher = pattern.matcher(text);
		return matcher.find() ? matcher.group(1) : null;
	}

	private GitHub connectToGitHub() throws Exception {
		Properties props = new Properties();
		try (InputStream resource = getClass().getResourceAsStream("/META-INF/github.properties")) {
			props.load(resource);
		}

		String keyFile = props.getProperty("key_file");
		String appId = props.getProperty("app_id");
		String instId = props.getProperty("inst_id");

		// Create a JWT (local operation) to connect as the generic app.
		String jwtToken = createJWT(appId, keyFile);
		GitHub jwtGitHub = new GitHubBuilder().withJwtToken(jwtToken).build();
		GHApp pluginGitHubApp = jwtGitHub.getApp();
		// Then, create an installation token (remote operation) to connect as a specific app (for the plugin repo).
		GHAppInstallation pluginInstallation = pluginGitHubApp.getInstallationById(Long.parseLong(instId));
		GHAppInstallationToken installationToken = pluginInstallation.createToken().create();
		GitHub repoSpecificGitHub = new GitHubBuilder().withAppInstallationToken(installationToken.getToken()).build();
		// Then use the latter to authenticate (and be able to search & create issues).
		return repoSpecificGitHub;
	}

	private PrivateKey keyFromResource(String keyResourceName)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		try (InputStream inputStream = getClass().getResourceAsStream(keyResourceName)) {
			byte[] keyBytes = Objects.requireNonNull(inputStream).readAllBytes();
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePrivate(spec);
		}
	}

	private String createJWT(String githubAppId, String keyFile)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// We need to define two timestamps between which out JWT will be valid, according to the GitHub server clock.
		// The "issued at" timestamp must be in the past, and the expiration timestamp must be at most 10 minutes into
		// the future.

		// Guard against (large) clock drift: issue the token 4 minutes in the past, with a maximum expiration window
		// (of 10 min.), to the token is valid unless the local clock drift is outside the interval (-6 min, 4 min).
		long nowMillis = System.currentTimeMillis() - 240_000;
		long expMillis = nowMillis + 600_000;

		//We will sign our JWT with our private key
        Key signingKey = keyFromResource("/META-INF/" + keyFile);

		//Build the JWT by its claims and serializes it to a compact, URL-safe string
		return Jwts.builder()
				.issuedAt(new Date(nowMillis))
				.expiration(new Date(expMillis))
				.issuer(githubAppId)
				.signWith(signingKey, StandardSecureDigestAlgorithms.findBySigningKey(signingKey))
				.compact();
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
			return ghIssues.toList().getFirst();
		}
	}

	private GHIssue createCrashReport(GitHub gitHub, String errorMessage, String markdownBody) throws IOException {
		GHIssueBuilder issueBuilder = gitHub.getRepository(repository).createIssue("Crash Report: " + errorMessage);
		issueBuilder.body(markdownBody);
		issueBuilder.label("crash report");
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
		VirtualFile file;
		ScratchRootType scratchRoot = ScratchRootType.getInstance();
		Language markdownLanguage = Language.findLanguageByID("Markdown"); // Is Markdown supported?
		// Without a properly working JCEF, the Markdown preview will not work well, so fall back to plain text then.
		if (markdownLanguage != null && hasWorkingJCEF()) {
			file = scratchRoot.createScratchFile(project, "bug-report.md", markdownLanguage, text);
		} else {
			file = scratchRoot.createScratchFile(project, "bug-report.txt", PlainTextLanguage.INSTANCE, text);
		}

		if (file != null) {
			OpenFileAction.openFile(file, project);
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
