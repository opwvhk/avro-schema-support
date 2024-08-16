package opwvhk.intellij.avro_idl.language;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.inspections.SimpleAvroIdlQuickFixOnPsiElement;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class AddEnumSymbolFix extends SimpleAvroIdlQuickFixOnPsiElement<PsiElement> {

	/**
	 * Create an "Add enum symbol" fix.
	 *
	 * @param element the identifier element of an enum default
	 */
	public AddEnumSymbolFix(@NotNull PsiElement element) {
		super(element, TextBundle.message("syntax.unknown.enum.default.fix"));
	}

	@Override
	protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
	                      @NotNull PsiElement element) {
		AvroIdlEnumDeclaration enumDeclaration = PsiTreeUtil.getParentOfType(element, AvroIdlEnumDeclaration.class);
		if (enumDeclaration == null) {
			return;
		}
		AvroIdlEnumBody enumBody = enumDeclaration.getEnumBody();
		if (enumBody == null) {
			return;
		}
		boolean isFirstSymbol = enumBody.getEnumConstantList().isEmpty();

		AvroIdlElementFactory factory = new AvroIdlElementFactory(project);
		AvroIdlFile dummyFile = factory.createDummyFile(String.format("enum Foo { A, %s }", element.getText()));
		AvroIdlEnumBody dummyBody = requireNonNull(PsiTreeUtil.findChildOfType(dummyFile, AvroIdlEnumBody.class));
		List<AvroIdlEnumConstant> generatedConstants = dummyBody.getEnumConstantList();

		AvroIdlEnumConstant newSymbol = generatedConstants.get(1);
		if (isFirstSymbol) {
			enumBody.add(newSymbol);
		} else {
			enumBody.addRange(generatedConstants.get(0).getNextSibling(), newSymbol);
		}
	}

	@Override
	public boolean availableInBatchMode() {
		return false;
	}
}
