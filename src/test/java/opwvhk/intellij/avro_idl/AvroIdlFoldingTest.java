package opwvhk.intellij.avro_idl;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class AvroIdlFoldingTest extends BasePlatformTestCase {
	@Override
	protected String getTestDataPath() {
		return "src/test/testData/folding";
	}

	public void testFolding() {
		myFixture.testFolding(getTestDataPath() + "/TestDataWithFolds.avdl");
	}
}
