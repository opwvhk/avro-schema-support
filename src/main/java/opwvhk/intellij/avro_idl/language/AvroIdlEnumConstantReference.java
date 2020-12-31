package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumBody;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumConstant;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumDeclaration;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumDefault;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class AvroIdlEnumConstantReference extends AvroIdlAbstractReference<AvroIdlEnumConstant> implements PsiPolyVariantReference {

	private final String name;

	public AvroIdlEnumConstantReference(@NotNull AvroIdlEnumDefault element) {
		super(element, element.getIdentifier());
		name = element.getIdentifier().getText();
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		return findConstants()
			.filter(enumValue -> name.equals(enumValue.getName()))
			.map(PsiElementResolveResult::createResults)
			.findFirst().orElse(new ResolveResult[0]);
	}

	private Stream<AvroIdlEnumConstant> findConstants() {
		final AvroIdlEnumDeclaration parent = (AvroIdlEnumDeclaration) myElement.getParent();
		final AvroIdlEnumBody enumBody = parent.getEnumBody();
		if (enumBody != null) {
			return enumBody.getEnumConstantList().stream();
		} else {
			return Stream.empty();
		}
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return findConstants()
			.filter(enumValue -> enumValue.getName() != null && !enumValue.getName().isBlank())
			.map(enumValue -> LookupElementBuilder.create(enumValue.getName()).withTypeText(enumValue.getContainingFile().getName(), AvroIdlIcons.FILE, false))
			.toArray();
	}
}
