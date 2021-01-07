package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.psi.AvroIdlMessageAttributeThrows;
import opwvhk.intellij.avro_idl.psi.AvroIdlProtocolDeclaration;
import opwvhk.intellij.avro_idl.psi.AvroIdlPsiUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlReferenceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

import static opwvhk.intellij.avro_idl.language.AvroIdlUtil.IS_ERROR_KEY;

public class AvroIdlNamedSchemaReference extends AvroIdlAbstractReference {

	private final String fullName;
	private final boolean matchErrorTypesOnly;

	@Nullable
	public static AvroIdlNamedSchemaReference forType(@NotNull AvroIdlReferenceType owner) {
		if (owner.getIdentifier() == null) {
			return null;
		}
		return new AvroIdlNamedSchemaReference(owner, owner.getIdentifier(), false);
	}

	@NotNull
	public static AvroIdlNamedSchemaReference forMessageAttribute(@NotNull AvroIdlMessageAttributeThrows owner) {
		return new AvroIdlNamedSchemaReference(owner, owner.getIdentifier(), true);
	}

	private AvroIdlNamedSchemaReference(@NotNull PsiElement element, @NotNull PsiElement identifier, boolean matchErrorTypesOnly) {
		super(element, identifier);
		final String name = identifier.getText();
		if (name.contains(".")) {
			fullName = name;
		} else {
			fullName = AvroIdlPsiUtil.getNamespacePrefix(element) + name;
		}
		this.matchErrorTypesOnly = matchErrorTypesOnly;
	}

	protected Optional<LookupElement> resolve0() {
		final String namespace = AvroIdlPsiUtil.getNamespace(myElement);
		return Stream.ofNullable(findProtocol())
			.flatMap(protocol -> AvroIdlUtil.findAllSchemaNamesAvailableInProtocol(protocol, false, namespace))
			.filter(lookupElement -> lookupElement.getAllLookupStrings().contains(fullName))
			.findFirst();
	}

	@Override
	@Nullable
	public PsiElement resolve() {
		return resolve0().map(LookupElement::getPsiElement).orElse(null);
	}

	public boolean resolvesToError() {
		return resolve0().map(lookupElement -> lookupElement.getUserData(IS_ERROR_KEY)).orElse(false);
	}

	private @NotNull AvroIdlProtocolDeclaration findProtocol() {
		PsiElement parent = myElement;
		while (!(parent instanceof AvroIdlProtocolDeclaration)) {
			parent = parent.getParent();
		}
		return (AvroIdlProtocolDeclaration) parent;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		final AvroIdlProtocolDeclaration protocol = findProtocol();
		final String namespace = AvroIdlPsiUtil.getNamespace(myElement);
		final Object[] variants = AvroIdlUtil.findAllSchemaNamesAvailableInProtocol(protocol, matchErrorTypesOnly, namespace).toArray();
		return variants;
	}
}
