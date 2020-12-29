package opwvhk.intellij.avro_idl;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

public class AvroIdlFoldingTest extends LightJavaCodeInsightFixtureTestCase {
	@Override
	protected String getTestDataPath() {
		return "src/test/testData/folding";
	}

	public void testFolding() {
		myFixture.testFolding(getTestDataPath() + "/TestDataWithFolds.avdl");
	}
}
