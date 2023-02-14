package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlMessageAttributeThrows;
import opwvhk.intellij.avro_idl.psi.AvroIdlPsiUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlReferenceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static opwvhk.intellij.avro_idl.language.AvroIdlUtil.IS_ERROR_KEY;
import static opwvhk.intellij.avro_idl.language.AvroIdlUtil.ifType;

public class AvroIdlNamedSchemaReference extends AvroIdlAbstractReference {

	private final String fullName;
	private final boolean matchErrorTypesOnly;

	@Nullable
	public static AvroIdlNamedSchemaReference forType(@NotNull AvroIdlReferenceType owner) {
		return Optional.of(owner)
				.map(AvroIdlReferenceType::getIdentifier)
				.map(PsiElement::getFirstChild)
				.map(token -> new AvroIdlNamedSchemaReference(owner, token, false))
				.orElse(null);
	}

	@NotNull
	public static AvroIdlNamedSchemaReference forMessageAttribute(@NotNull AvroIdlMessageAttributeThrows owner) {
		return new AvroIdlNamedSchemaReference(owner, requireNonNull(owner.getIdentifier().getIdentifierToken()), true);
	}

	private AvroIdlNamedSchemaReference(@NotNull PsiElement element, @NotNull PsiElement identifier,
	                                    boolean matchErrorTypesOnly) {
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
		return ifType(myElement.getContainingFile().getOriginalFile(), AvroIdlFile.class)
				.flatMap(idlFile -> AvroIdlUtil.findAllSchemaNamesAvailableInIdl(idlFile, false, namespace))
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

	@Override
	@NotNull
	public Object[] getVariants() {
		final AvroIdlFile idlFile = (AvroIdlFile) myElement.getContainingFile();
		AvroIdlFile idlFile2 = (AvroIdlFile) idlFile.getOriginalFile();
		final String namespace = AvroIdlPsiUtil.getNamespace(myElement);
		return AvroIdlUtil.findAllSchemaNamesAvailableInIdl(idlFile2, matchErrorTypesOnly, namespace).toArray();
	}
}
