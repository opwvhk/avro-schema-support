package opwvhk.intellij.avro_idl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.usageView.UsageInfo;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlReferencesTest extends LightJavaCodeInsightFixtureTestCase {
	public void testRename() {
		myFixture.configureByFiles("BeforeRename.avdl");
		myFixture.renameElementAtCaret("OnOrOff");
		myFixture.checkResultByFile("AfterRename.avdl", false);
	}

	@Override
	protected String getTestDataPath() {
		return "src/test/testData/references";
	}

	public void testReference() {
		myFixture.configureByFiles("BeforeRename.avdl");
		PsiElement element = myFixture.getFile().findElementAt(myFixture.getCaretOffset());

		// The element will be an identifier. Ensure it references something.
		assertNotNull(element);
		PsiReference reference = element.getParent().getReference();
		assertNotNull(reference);
		final PsiElement resolvedReference = reference.resolve();
		assertNotNull(resolvedReference);

		// Verify the reference points to the expected element.
		assertTrue(resolvedReference instanceof AvroIdlEnumDeclaration);
		assertEquals("org.apache.avro.test.Status", ((AvroIdlEnumDeclaration) resolvedReference).getFullName());
	}

	public void testFindUsages() {
		Collection<UsageInfo> usageInfos = myFixture.testFindUsages("BeforeRename.avdl");
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
	private static Usage usage(@NotNull String resolvedFullName, @NotNull String referenceText, @NotNull IElementType parentElement,
							   @NotNull IElementType grandParentElement) {
		return new Usage(resolvedFullName, referenceText, parentElement, grandParentElement);
	}

	private static class Usage {
		private final String resolvedFullName;
		private final String referenceText;
		private final IElementType parentElement;
		private final IElementType grandParentElement;

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

		Usage(String resolvedFullName, String referenceText, IElementType parentElement, IElementType grandParentElement) {
			this.resolvedFullName = resolvedFullName;
			this.referenceText = referenceText;
			this.parentElement = parentElement;
			this.grandParentElement = grandParentElement;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Usage usage = (Usage) o;
			return Objects.equals(resolvedFullName, usage.resolvedFullName) &&
				Objects.equals(referenceText, usage.referenceText) &&
				Objects.equals(parentElement, usage.parentElement) &&
				Objects.equals(grandParentElement, usage.grandParentElement);
		}

		@Override
		public int hashCode() {
			return Objects.hash(resolvedFullName, referenceText, parentElement, grandParentElement);
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
