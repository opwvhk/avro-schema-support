package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumBody;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumConstant;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumDeclaration;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class AvroIdlEnumConstantReference extends AvroIdlAbstractReference {

	private final String name;

	@NotNull
	public static AvroIdlEnumConstantReference forDefault(@NotNull AvroIdlEnumDefault element) {
		return new AvroIdlEnumConstantReference(element);
	}

	private AvroIdlEnumConstantReference(@NotNull AvroIdlEnumDefault element) {
		super(element, element.getIdentifier());
		name = element.getIdentifier().getText();
	}

	@Override
	@Nullable
	public AvroIdlEnumConstant resolve() {
		return findConstants()
				.filter(enumValue -> name.equals(enumValue.getName()))
				.findFirst().orElse(null);
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

	@Override
	public Object @NotNull [] getVariants() {
		return findConstants()
				.filter(enumValue -> enumValue.getName() != null && !enumValue.getName().isBlank())
				.map(enumValue -> LookupElementBuilder.create(enumValue.getName())
						.withTypeText(enumValue.getContainingFile().getName(), AvroIdlIcons.LOGO, false))
				.toArray();
	}
}
