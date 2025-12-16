package opwvhk.intellij.avro_idl.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.TestActionEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AvroIdlActionsTest extends HeavyPlatformTestCase {
	private Path inputDirectory;
	private Path outputDirectory;
	private Path resultDirectory;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		Path testDirectory = Path.of(getTestDataPath(), getTestDirectoryName());
		inputDirectory = testDirectory.resolve("input");
		outputDirectory = testDirectory.resolve("output");

		VirtualFile resultVDirectory = getTempDir().createVirtualDir(getTestDirectoryName());
		ConversionActionBase.targetDirectory = resultVDirectory;
		resultDirectory = resultVDirectory.toNioPath();

		Path[] array = list(outputDirectory, s -> s.sorted(Comparator.comparing(Path::toString)).toArray(Path[]::new));
		if (array.length == 1) {
			Path relative = outputDirectory.relativize(array[0]);
			Path expectedResultFile = resultDirectory.resolve(relative);
			ConversionActionBase.targetFile = new VirtualFileWrapper(expectedResultFile.toFile());
		}
	}

	@Override
	public void tearDown() throws Exception {
		ConversionActionBase.targetDirectory = null;
		ConversionActionBase.targetFile = null;
		super.tearDown();
	}

	protected String getTestDataPath() {
		return "src/test/testData/actions";
	}

	public void testNoFiles() throws IOException {
		AnAction action = ActionManager.getInstance().getAction("AvroIdl.IdlToProtocol");
		executeTest(action, false);
	}

	public void testNoSuitableFiles() throws IOException {
		AnAction action = ActionManager.getInstance().getAction("AvroIdl.IdlToProtocol");

		executeTest(action, false);
	}

	public void testIdlToProtocol() throws IOException {
		AnAction action = ActionManager.getInstance().getAction("AvroIdl.IdlToProtocol");
		executeTest(action, true);
	}

	public void testIdlToSchema() throws IOException {
		AnAction action = ActionManager.getInstance().getAction("AvroIdl.IdlToSchema");
		executeTest(action, true);
	}

	public void testProtocolToIdl() throws IOException {
		AnAction action = ActionManager.getInstance().getAction("AvroIdl.ProtocolToIdl");
		executeTest(action, true);
	}

	public void testSchemaToIdl() throws IOException {
		AnAction action = ActionManager.getInstance().getAction("AvroIdl.SchemaToIdl");
		executeTest(action, true);
	}

	private void executeTest(AnAction action, boolean shouldExecute) throws IOException {
		LocalFileSystem vfs = LocalFileSystem.getInstance();
		DataContext dataContext = SimpleDataContext.builder()
				.add(LangDataKeys.VIRTUAL_FILE_ARRAY,
						list(inputDirectory, s -> s.map(vfs::refreshAndFindFileByNioFile).toArray(VirtualFile[]::new)))
				.add(CommonDataKeys.PROJECT, getProject())
				.build();

		AnActionEvent event = TestActionEvent.createTestEvent(action, dataContext);
		// TODO: use next line instead of the one after when requiring IDE 2025.?
		//ActionUtil.updateAction(action, event);
		ActionUtil.performDumbAwareUpdate(action, event, false);
		Presentation p = event.getPresentation();
		assertThat(p.isEnabled()).as("event %s", p.isEnabled() ? "enabled" : "disabled").isEqualTo(shouldExecute);
		assertThat(p.isVisible()).as("event %s", p.isVisible() ? "visible" : "hidden").isEqualTo(shouldExecute);

		// TODO: use next line instead of the one after when requiring IDE 2025.?
		//ActionUtil.performAction(action, event);
		ActionUtil.performActionDumbAwareWithCallbacks(action, event);
		if (shouldExecute) {
			assertSameTextContentRecursive(resultDirectory, outputDirectory);
		} else {
			try (Stream<Path> resultFiles = Files.list(resultDirectory)) {
				assertThat(resultFiles.count()).isEqualTo(0);
			}
		}
	}

	private void assertSameTextContentRecursive(Path actualPath, Path expectedPath) throws IOException {
		if (Files.isDirectory(expectedPath)) {
			assertThat(actualPath).isDirectory();
			List<Path> actualPaths = list(actualPath, s -> s.map(actualPath::relativize).toList());
			List<Path> expectedPaths = list(expectedPath, s -> s.map(expectedPath::relativize).toList());
			assertThat(actualPaths).isEqualTo(expectedPaths);
			for (Path p : expectedPaths) {
				assertSameTextContentRecursive(actualPath.resolve(p), expectedPath.resolve(p));
			}
		} else {
			assertThat(actualPath).isRegularFile().hasFileName(expectedPath.getFileName().toString());
			String actualContent = Files.readString(actualPath);
			String expectedContent = Files.readString(expectedPath);
			assertThat(actualContent).as("content of %s", actualPath.getFileName().toString())
					.isEqualTo(expectedContent);
		}
	}

	private static <T> T list(Path path, Function<Stream<Path>, T> function) throws IOException {
		try (Stream<Path> stream = Files.list(path)) {
			return function.apply(stream);
		}
	}
}
