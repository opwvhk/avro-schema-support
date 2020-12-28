package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.util.ProcessingContext;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;

class AvroIdlCompletionContributor extends CompletionContributor {
	@Override
	public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
		super.fillCompletionVariants(parameters, result);
	}

	public AvroIdlCompletionContributor() {

		// Complete protocol keyword

		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParent(PsiErrorElement.class).withSuperParent(2, AvroIdlFile.class),
			"@namespace(\"\u0000\")", "protocol ");

		// Complete declaration types

		// Identifiers in a protocol are identified as message return types until proven to be something else; hence the withSuperParent(2, ...).
		// Also note that due to the syntax tree, message arguments are at a different level than the return type (otherwise this would also match there).
		// Completions include declarations and the extra return type 'void'
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParent(AvroIdlReferenceType.class).withSuperParent(2, AvroIdlMessageDeclaration.class),
			"import ", "record ", "error ", "enum ", "fixed ", "void ");

		// Complete primitive types, logical types and anonymous types

		// Identifiers at a type location are identified as Reference type until proven to be something else
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParent(AvroIdlReferenceType.class),
			"boolean ", "bytes ", "int ", "string ", "float ", "double ", "long ",
			"null ",
			"date ", "time_ms ", "local_timestamp_ms ", "timestamp_ms ", "decimal(",
			"union {", "array<", "map<"
		);

		// Complete message attributes & import types

		addBasicCompletion(psiElement().withParent(PsiErrorElement.class).withSuperParent(2, AvroIdlMessageDeclaration.class),
			"throws ", "oneway");
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParent(PsiErrorElement.class).withSuperParent(2, AvroIdlImportDeclaration.class),
			"idl ", "protocol ", "schema ");

		// Complete special annotations: @namespace (protocols & named types), @order (record fields), @aliases (in both), @logicalType (types)

		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParents(AvroIdlSchemaProperty.class, AvroIdlProtocolDeclaration.class),
			'@', "namespace(\"\u0000\")");
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParents(AvroIdlSchemaProperty.class, AvroIdlNamedSchemaDeclaration.class),
			'@', "namespace(\"\u0000\")", "aliases([\"\u0000\"])", "logicalType(\"\u0000\")");
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParents(AvroIdlSchemaProperty.class, AvroIdlVariableDeclarator.class),
			'@', "aliases([\"\u0000\"])", "order(\"\u0000\")");
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParents(AvroIdlSchemaProperty.class, AvroIdlType.class),
			'@', "logicalType(\"\u0000\")");
		// Due to missing/present quotes, identifying values of @order annotations is split into (in order): just after the (, and within a string literal
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER).withParent(
			psiElement(PsiErrorElement.class).withParent(psiElement(AvroIdlSchemaProperty.class).withName("order"))),
			"\"ascending\"", "\"descending\"", "\"ignore\"");
		addBasicCompletion(psiElement(AvroIdlTypes.STRING_LITERAL).withParent(AvroIdlJsonValue.class)
				.withSuperParent(2, psiElement(AvroIdlSchemaProperty.class).withName("order")),
			"ascending", "descending", "ignore");

		// Complete imported schema files

		// TODO: identify string literals in import statements to add file name completion
	}

	/**
	 * Equivalent to {@link #addBasicCompletion(ElementPattern, Character, String...) addBasicCompletion(pattern, null, completions)}.
	 */
	private void addBasicCompletion(ElementPattern<? extends PsiElement> pattern, String... completions) {
		addBasicCompletion(pattern, null, completions);
	}

	/**
	 * Add "basic" completions for a pattern. The completions are coded: the completion popup only shows word characters (it removes spaces, parentheses, etc)
	 * and if the completion contains a null char, it's removed and after inserting the caret is placed there.
	 *
	 * @param pattern       the pattern to complete
	 * @param skipFirstChar if this string matches the first part of the prefix, skip that
	 * @param completions   the completions
	 */
	private void addBasicCompletion(ElementPattern<? extends PsiElement> pattern, Character skipFirstChar, String... completions) {
		List<LookupElement> lookupElements = new ArrayList<>();
		for (String completion : completions) {
			final String textToPutInEditor = completion.replace("\u0000", "");
			final String textInTheCompletionPopup = completion.replaceAll("\\W", "");
			LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(textToPutInEditor).withPresentableText(textInTheCompletionPopup);
			int relativeCaretOffset = completion.indexOf("\u0000"); // Is there a caret marker?
			if (relativeCaretOffset != -1) {
				lookupElementBuilder = lookupElementBuilder.withInsertHandler((context, item) -> {
					final int startOffset = context.getStartOffset();
					final int newCaretOffset = startOffset + relativeCaretOffset;
					final Editor editor = context.getEditor();
					editor.getCaretModel().moveToOffset(newCaretOffset);
				});
			}
			lookupElements.add(lookupElementBuilder);
		}

		extend(CompletionType.BASIC, pattern, new CompletionProvider<>() {
			@Override
			protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
				if (skipFirstChar != null) {
					final PrefixMatcher prefixMatcher = result.getPrefixMatcher();
					final String prefix = prefixMatcher.getPrefix();
					if (prefix.length() > 1 && prefix.charAt(0) == skipFirstChar) {
						result.withPrefixMatcher(prefix.substring(1)).addAllElements(lookupElements);
						return;
					}
				}
				result.addAllElements(lookupElements);
			}
		});
	}
}
