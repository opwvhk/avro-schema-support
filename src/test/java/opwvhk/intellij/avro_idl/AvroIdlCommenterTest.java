package opwvhk.intellij.avro_idl;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.testFramework.TestActionEvent;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class AvroIdlCommenterTest extends BasePlatformTestCase {

	@Override
	protected String getTestDataPath() {
		return "src/test/testData/commenter";
	}

	public void testLineCommenting() {
		myFixture.configureByFile("UncommentedLine.avdl");
		performEditorAction("CommentByLineComment");
		myFixture.checkResultByFile("UncommentedLineCommented.avdl");
	}

	public void testLineUncommenting() {
		myFixture.configureByFile("CommentedLine.avdl");
		performEditorAction("CommentByLineComment");
		myFixture.checkResultByFile("CommentedLineUncommented.avdl");
	}

	public void testBlockCommenting() {
		myFixture.configureByFile("UncommentedBlock.avdl");
		performEditorAction("CommentByBlockComment");
		myFixture.checkResultByFile("UncommentedBlockCommented.avdl");
	}

	public void testBlockUncommenting() {
		myFixture.configureByFile("CommentedBlock.avdl");
		performEditorAction("CommentByBlockComment");
		myFixture.checkResultByFile("CommentedBlockUncommented.avdl");
	}

	public void testSelectionCommenting() {
		myFixture.configureByFile("UncommentedSelection.avdl");
		performEditorAction("CommentByBlockComment");
		myFixture.checkResultByFile("UncommentedSelectionCommented.avdl");
	}

	public void testSelectionUncommenting() {
		myFixture.configureByFile("CommentedSelection.avdl");
		performEditorAction("CommentByBlockComment");
		myFixture.checkResultByFile("CommentedSelectionUncommented.avdl");
	}

	private void performEditorAction(String action) {
		var commentByBlockComment = ActionManager.getInstance().getAction(action);
		AnActionEvent event = TestActionEvent.createTestEvent(SimpleDataContext.builder()
				.add(CommonDataKeys.PROJECT, myFixture.getEditor().getProject())
				.add(CommonDataKeys.EDITOR, myFixture.getEditor()).build());
		ActionUtil.performAction(commentByBlockComment, event);
	}
}
