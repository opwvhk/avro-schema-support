package opwvhk.intellij.avro_idl;

import com.intellij.codeInsight.generation.actions.CommentByBlockCommentAction;
import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

public class AvroIdlCommenterTest extends LightJavaCodeInsightFixtureTestCase {

	@Override
	protected String getTestDataPath() {
		return "src/test/testData/commenter";
	}

	public void testLineCommenting() {
		myFixture.configureByFile("UncommentedLine.avdl");
		new CommentByLineCommentAction().actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResultByFile("UncommentedLineCommented.avdl");
	}

	public void testLineUncommenting() {
		myFixture.configureByFile("CommentedLine.avdl");
		new CommentByLineCommentAction().actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResultByFile("CommentedLineUncommented.avdl");
	}

	public void testBlockCommenting() {

		myFixture.configureByFile("UncommentedBlock.avdl");
		new CommentByBlockCommentAction().actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResultByFile("UncommentedBlockCommented.avdl");
	}

	public void testBlockUncommenting() {

		myFixture.configureByFile("CommentedBlock.avdl");
		new CommentByBlockCommentAction().actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResultByFile("CommentedBlockUncommented.avdl");
	}

	public void testSelectionCommenting() {

		myFixture.configureByFile("UncommentedSelection.avdl");
		new CommentByBlockCommentAction().actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResultByFile("UncommentedSelectionCommented.avdl");
	}

	public void testSelectionUncommenting() {

		myFixture.configureByFile("CommentedSelection.avdl");
		new CommentByBlockCommentAction().actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResultByFile("CommentedSelectionUncommented.avdl");
	}
}
