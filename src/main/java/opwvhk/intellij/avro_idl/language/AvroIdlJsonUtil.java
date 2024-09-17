package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.json.psi.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlJsonStringLiteral;
import org.apache.avro.Schema;
import org.jetbrains.annotations.NotNull;

public class AvroIdlJsonUtil {
	@NotNull
	public static LookupElement createLookupElementForSchemaInJsonFile(@NotNull String namespace,
	                                                                   AvroIdlJsonStringLiteral importedFileReferenceElement,
	                                                                   PsiManager psiManager,
	                                                                   VirtualFile importedFile, Schema schema) {
		final PsiFile psiProtocolFile = psiManager.findFile(importedFile);
		if (psiProtocolFile instanceof JsonFile) {
			final PsiElement[] elements = PsiTreeUtil.collectElements(psiProtocolFile,
					element -> isSchemaJsonObject(element, schema));
			if (elements.length > 0) {
				return lookupElement(elements[0], schema, namespace);
			}
		}
		return lookupElement(importedFileReferenceElement, schema, namespace);
	}

	private static boolean isSchemaJsonObject(@NotNull PsiElement element, @NotNull Schema schema) {
		if (!(element instanceof JsonObject)) {
			return false;
		}

		JsonProperty nameProperty = ((JsonObject) element).findProperty("name");
		if (nameProperty == null) {
			return false;
		}
		JsonValue nameValue = nameProperty.getValue();
		if (!(nameValue instanceof JsonStringLiteral)) {
			return false;
		}
		String name = ElementManipulators.getValueText(nameValue);

		if (name.contains(".")) {
			return name.equals(schema.getFullName());
		} else if (name.equals(schema.getName())) {
			JsonElement parent = (JsonElement) element;
			while (parent != null && !(parent instanceof JsonFile)) {
				if (parent instanceof JsonObject) {
					final JsonProperty namespaceProperty = ((JsonObject) parent).findProperty("namespace");
					if (namespaceProperty != null) {
						final JsonValue namespaceValue = namespaceProperty.getValue();
						return namespaceValue instanceof JsonStringLiteral &&
								ElementManipulators.getValueText(namespaceValue).equals(schema.getNamespace());
					}
				}
				parent = (JsonElement) parent.getParent();
			}
		}
		return false;
	}

	@NotNull
	private static LookupElement lookupElement(@NotNull PsiElement psiElement, @NotNull Schema schema,
	                                           @NotNull String currentNamespace) {
		final String namespace0 = schema.getNamespace();
		final String namespace = namespace0 == null ? "" : namespace0;
		final String schemaName = schema.getName();
		final String schemaFullName = schema.getFullName();
		final LookupElement lookupElement = AvroIdlUtil.lookupElement(psiElement, schemaName, schemaFullName, namespace,
				currentNamespace);
		lookupElement.putUserData(AvroIdlUtil.IS_ERROR_KEY, schema.getType() == Schema.Type.RECORD && schema.isError());
		return lookupElement;
	}
}
