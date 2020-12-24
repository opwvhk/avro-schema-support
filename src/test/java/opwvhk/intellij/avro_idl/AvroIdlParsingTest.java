package opwvhk.intellij.avro_idl;

import com.intellij.testFramework.ParsingTestCase;
import opwvhk.intellij.avro_idl.syntax.AvroIdlParserDefinition;

public class AvroIdlParsingTest extends ParsingTestCase {
	public AvroIdlParsingTest() {
		super("", "avdl", new AvroIdlParserDefinition());
	}

	public void testTestData() {
		doTest(true);
	}

	/**
	 * @return path to test data file directory relative to root of this module.
	 */
	@Override
	protected String getTestDataPath() {
		return "src/test/testData/parsing";
	}

	@Override
	protected boolean skipSpaces() {
		return false;
	}

	@Override
	protected boolean includeRanges() {
		return true;
	}
}
