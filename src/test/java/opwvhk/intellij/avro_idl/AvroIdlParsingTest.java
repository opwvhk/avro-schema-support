package opwvhk.intellij.avro_idl;

import com.intellij.testFramework.ParsingTestCase;
import opwvhk.intellij.avro_idl.syntax.AvroIdlParserDefinition;

public class AvroIdlParsingTest extends ParsingTestCase {
	public AvroIdlParsingTest() {
		super("", "avdl", new AvroIdlParserDefinition());
	}

	/*
	 * The parse result is a tree structure in text. By default, the tree includes whitespace, but no parse ranges.
	 * We want neither to more easily verify the result (and IDL syntax is not space aware anyway)
	 */

	@Override
	protected boolean skipSpaces() {
		return true;
	}

	/**
	 * @return path to test data file directory relative to root of this module.
	 */
	@Override
	protected String getTestDataPath() {
		return "src/test/testData/parsing";
	}

	/*
	 * Test cases. The name of the test (minus the 'test' prefix) is also the name of the data file that is read.
	 */

	public void testProtocol() {
		doTest(true);
	}

	public void testSchema() {
		doTest(true);
	}
}
