package opwvhk.intellij.avro_idl.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.MapDataContext;
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
		MapDataContext dataContext = new MapDataContext();
		LocalFileSystem vfs = LocalFileSystem.getInstance();
		dataContext.put(LangDataKeys.VIRTUAL_FILE_ARRAY,
				list(inputDirectory, s -> s.map(vfs::refreshAndFindFileByNioFile).toArray(VirtualFile[]::new)));
		dataContext.put(CommonDataKeys.PROJECT, getProject());

		AnActionEvent event = TestActionEvent.createTestEvent(action, dataContext);
		action.update(event);
		Presentation p = event.getPresentation();
		assertThat(p.isEnabled()).as("event %s", p.isEnabled() ? "enabled" : "disabled").isEqualTo(shouldExecute);
		assertThat(p.isVisible()).as("event %s", p.isVisible() ? "visible" : "hidden").isEqualTo(shouldExecute);

		action.actionPerformed(event); // Outside if statement to verify it correctly handles null invocations
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
