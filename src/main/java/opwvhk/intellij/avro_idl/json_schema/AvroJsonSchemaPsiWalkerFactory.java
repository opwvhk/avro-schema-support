package opwvhk.intellij.avro_idl.json_schema;

import com.intellij.json.JsonDialectUtil;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.jetbrains.jsonSchema.extension.JsonLikePsiWalker;
import com.jetbrains.jsonSchema.extension.JsonLikePsiWalkerFactory;
import com.jetbrains.jsonSchema.impl.JsonOriginalPsiWalker;
import com.jetbrains.jsonSchema.impl.JsonSchemaObject;
import opwvhk.intellij.avro_idl.AvroProtocolLanguage;
import opwvhk.intellij.avro_idl.AvroSchemaLanguage;
import org.jetbrains.annotations.NotNull;

public class AvroJsonSchemaPsiWalkerFactory implements JsonLikePsiWalkerFactory {
	public static final JsonLikePsiWalker WALKER_INSTANCE = new JsonOriginalPsiWalker();

	@Override
	public boolean handles(@NotNull PsiElement element) {
		PsiElement parent = element.getParent();
		Language language = parent == null ? null : JsonDialectUtil.getLanguageOrDefaultJson(parent);
		return language instanceof AvroSchemaLanguage || language instanceof AvroProtocolLanguage;
	}

	@Override
	@NotNull
	public JsonLikePsiWalker create(@NotNull JsonSchemaObject schemaObject) {
		return WALKER_INSTANCE;
	}
}
