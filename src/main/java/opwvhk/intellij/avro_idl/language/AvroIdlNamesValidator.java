package opwvhk.intellij.avro_idl.language;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.regex.Pattern;

public class AvroIdlNamesValidator implements NamesValidator {
	private static final Set<String> KEYWORDS = Set.of("array", "boolean", "bytes", "date", "decimal", "double", "enum", "error", "false", "fixed", "float",
		"idl", "import", "int", "long", "map", "null", "oneway", "protocol", "record", "schema", "string", "throws", "time", "timestamp", "true", "union",
		"void");
	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile(
		"\\p{javaJavaIdentifierStart}[\\p{javaJavaIdentifierPart}.-]*\\p{javaJavaIdentifierPart}(?<![.-])",
		Pattern.UNICODE_CHARACTER_CLASS);

	@Override
	public boolean isKeyword(@NotNull String name, Project project) {
		return KEYWORDS.contains(name);
	}

	@Override
	public boolean isIdentifier(@NotNull String name, Project project) {
		return IDENTIFIER_PATTERN.matcher(name).matches();
	}
}
