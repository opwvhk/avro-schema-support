package opwvhk.intellij.avro_idl;

import com.intellij.application.options.CodeStyle;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.usageView.UsageInfo;
import opwvhk.intellij.avro_idl.editor.AvroIdlCodeStyleSettings;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumDeclaration;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamedSchemaDeclaration;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlReferencesTest extends BasePlatformTestCase {
	@Override
	protected String getTestDataPath() {
		return "src/test/testData/references";
	}

	public void testSchemaRenameWithoutAlias() {
		myFixture.configureByFiles("BeforeSchemaRename.avdl");
		AvroIdlCodeStyleSettings codeStyleSettings = CodeStyle.getCustomSettings(myFixture.getFile(),
				AvroIdlCodeStyleSettings.class);
		codeStyleSettings.ADD_ALIAS_ON_SCHEMA_RENAME = false;
		myFixture.renameElementAtCaret("OnOrOff");
		myFixture.checkResultByFile("AfterSchemaRenameNoAlias.avdl", false);
	}

	public void testSchemaRenameWithAlias() {
		myFixture.configureByFiles("BeforeSchemaRename.avdl");
		AvroIdlCodeStyleSettings codeStyleSettings = CodeStyle.getCustomSettings(myFixture.getFile(),
				AvroIdlCodeStyleSettings.class);
		codeStyleSettings.ADD_ALIAS_ON_SCHEMA_RENAME = true;
		myFixture.renameElementAtCaret("OnOrOff");
		myFixture.checkResultByFile("AfterSchemaRenameWithAlias.avdl", false);
	}

	public void testFieldRenameWithoutAlias() {
		myFixture.configureByFiles("BeforeFieldRename.avdl");
		AvroIdlCodeStyleSettings codeStyleSettings = CodeStyle.getCustomSettings(myFixture.getFile(),
				AvroIdlCodeStyleSettings.class);
		codeStyleSettings.ADD_ALIAS_ON_FIELD_RENAME = false;
		myFixture.renameElementAtCaret("onOrOff");
		myFixture.checkResultByFile("AfterFieldRenameNoAlias.avdl", false);
	}

	public void testFieldRenameWithAlias() {
		myFixture.configureByFiles("BeforeFieldRename.avdl");
		myFixture.renameElementAtCaret("onOrOff");
		AvroIdlCodeStyleSettings codeStyleSettings = CodeStyle.getCustomSettings(myFixture.getFile(),
				AvroIdlCodeStyleSettings.class);
		codeStyleSettings.ADD_ALIAS_ON_FIELD_RENAME = true;
		myFixture.checkResultByFile("AfterFieldRenameWithAlias.avdl", false);
	}

	public void testReference() {
		myFixture.configureByFiles("BeforeSchemaRename.avdl");
		PsiElement element = myFixture.getFile().findElementAt(myFixture.getCaretOffset());

		// The element will be an identifier. Ensure it references something.
		assertNotNull(element);
		PsiReference reference = element.getParent().getParent().getReference();
		assertNotNull(reference);
		final PsiElement resolvedReference = reference.resolve();
		assertNotNull(resolvedReference);

		// Verify the reference points to the expected element.
		assertTrue(resolvedReference instanceof AvroIdlEnumDeclaration);
		assertEquals("org.apache.avro.test.Status", ((AvroIdlEnumDeclaration) resolvedReference).getFullName());
	}

	public void testFindUsages() {
		Collection<UsageInfo> usageInfos = myFixture.testFindUsages("BeforeSchemaRename.avdl");
		assertUsage(usageInfos,
				usage("org.apache.avro.test.Status", "Status", FIELD_DECLARATION, RECORD_BODY),
				usage("org.apache.avro.test.Status", "org.apache.avro.test.Status", MESSAGE_DECLARATION, PROTOCOL_BODY)
		);
	}

	private void assertUsage(Collection<UsageInfo> usageInfos, Usage... expectedUsages) {
		final Usage[] actualUsages = usageInfos.stream()
				.sorted(Comparator.comparing(UsageInfo::getNavigationOffset))
				.map(Usage::fromUsageInfo)
				.toArray(Usage[]::new);
		assertOrderedEquals(actualUsages, expectedUsages);
	}

	@SuppressWarnings("SameParameterValue")
	private static Usage usage(@NotNull String resolvedFullName, @NotNull String referenceText,
	                           @NotNull IElementType parentElement,
	                           @NotNull IElementType grandParentElement) {
		return new Usage(resolvedFullName, referenceText, parentElement, grandParentElement);
	}

	private record Usage(String resolvedFullName, String referenceText, IElementType parentElement,
	                     IElementType grandParentElement) {
		static Usage fromUsageInfo(UsageInfo usageInfo) {
			final PsiReference reference = usageInfo.getReference();
			if (reference != null) {
				final PsiElement resolved = reference.resolve();
				final ASTNode node = reference.getElement().getNode();
				if (resolved instanceof AvroIdlNamedSchemaDeclaration) {
					return new Usage(((AvroIdlNamedSchemaDeclaration) resolved).getFullName(),
							reference.getElement().getText(),
							node.getTreeParent().getElementType(),
							node.getTreeParent().getTreeParent().getElementType());
				}
			}
			return new Usage("", "", WHITE_SPACE, WHITE_SPACE);
		}

		@Override
		public String toString() {
			return "Usage{" +
					"fullName='" + resolvedFullName + '\'' +
					", displayName='" + referenceText + '\'' +
					", parentElement=" + parentElement +
					", grandParentElement=" + grandParentElement +
					'}';
		}
	}
}
