package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AvroIdlNamedSchemaReference extends AvroIdlAbstractReference<AvroIdlNamedSchemaDeclaration>
	implements PsiPolyVariantReference {

	private final String fullName;
	private final boolean matchErrorTypesOnly;

	public AvroIdlNamedSchemaReference(@NotNull AvroIdlReferenceType element) {
		this(element, element.getIdentifier(), false);
	}

	public AvroIdlNamedSchemaReference(@NotNull AvroIdlMessageAttributeThrows element) {
		this(element, element.getIdentifier(), true);
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

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		AvroIdlProtocolDeclaration protocol = findProtocol();
		if (protocol != null) {
			final List<AvroIdlNamedSchemaDeclaration> namedTypes = AvroIdlUtil.findNamedTypes(protocol, fullName, incompleteCode);
			return PsiElementResolveResult.createResults(namedTypes);
		}
		return new ResolveResult[0];
	}


	@Nullable
	private AvroIdlProtocolDeclaration findProtocol() {
		PsiElement parent = myElement;
		while (parent != null && !(parent instanceof AvroIdlProtocolDeclaration)) {
			parent = parent.getParent();
		}
		return (AvroIdlProtocolDeclaration) parent;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		final List<AvroIdlNamedSchemaDeclaration> namedTypes = AvroIdlUtil.findNamedTypes(findProtocol(), null, true);
		final String myNamespacePrefix = AvroIdlPsiUtil.getNamespacePrefix(myElement);
		List<LookupElement> variants = new ArrayList<>();
		for (AvroIdlNamedSchemaDeclaration namedSchema : namedTypes) {
			boolean hasName = namedSchema.getName() != null && namedSchema.getName().length() > 0;
			boolean isError = namedSchema.isErrorType();
			if ((!matchErrorTypesOnly || isError) && hasName) {
				final String fullName = namedSchema.getFullName();
				//noinspection ConstantConditions hasName means getFullName does not return null.
				final String name = fullName.startsWith(myNamespacePrefix) ? fullName.substring(myNamespacePrefix.length()) : fullName;
				variants.add(LookupElementBuilder.create(name)
					.withTypeText(namedSchema.getContainingFile().getName()).withIcon(AvroIdlIcons.FILE));
			}
		}
		return variants.toArray();
	}
}
