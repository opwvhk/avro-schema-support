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

		// Empty file: namespace annotation for protocol, or the protocol, namespace, schema, record, enum & fixed keywords

		// TODO: Uncomment schema syntax completions when Avro supports the schema syntax (Avro 1.12.0)
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParent(
			psiElement(PsiErrorElement.class).atStartOf(psiElement(AvroIdlFile.class))
		), "6@namespace(\"\u0000\")", "protocol ");
		/*
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParent(
			psiElement(PsiErrorElement.class).atStartOf(psiElement(AvroIdlFile.class))
		), "6@namespace(\"\u0000\")", "protocol ", "9namespace ", "8schema ", "1import ", "3record ", "4enum ", "4fixed ");
		*/
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).afterSiblingSkipping(
			psiElement().whitespaceCommentOrError(), psiElement(AvroIdlProtocolDeclaration.class)
		), "6@namespace(\"\u0000\")", "protocol ");

		// After a namespace declaration: schema, record, enum & fixed keywords

		/*
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParent(
			psiElement(PsiErrorElement.class).withParent(AvroIdlNamespaceDeclaration.class)
				.afterLeafSkipping(psiElement().whitespaceCommentOrError(), psiElement(AvroIdlTypes.SEMICOLON))
		), "9schema ", "4import ", "6record ", "enum ", "fixed ");
		*/


		// Complete declaration types

		// Identifiers in a protocol parse as message return types until proven to be something else; hence the withSuperParent(2, ...).
		// Also note that due to the syntax tree, message arguments are at a different level than the return type (otherwise this would also match there).
		// Completions include declarations, and the extra return type 'void'.
		addBasicCompletion(
			psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParents(AvroIdlIdentifier.class, AvroIdlReferenceType.class, AvroIdlMessageDeclaration.class),
			"import ", "9record ", "error ", "7enum ", "7fixed ", "3void ");
		/*
		addBasicCompletion(
			psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParent(
				psiElement(PsiErrorElement.class).withParent(AvroIdlMainSchemaDeclaration.class)
					.afterLeafSkipping(psiElement().whitespaceCommentOrError(), psiElement(AvroIdlTypes.SEMICOLON))
			), "import ", "9record ", "7enum ", "7fixed ");
		*/
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParent(
			psiElement(PsiErrorElement.class).afterSiblingSkipping(psiElement().whitespaceCommentOrError(), psiElement(AvroIdlImportDeclaration.class))
		), "import ", "9record ", "7enum ", "7fixed ");
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParent(
			psiElement(PsiErrorElement.class).afterSiblingSkipping(psiElement().whitespaceCommentOrError(), psiElement(AvroIdlNamedSchemaDeclaration.class))
		), "import ", "9record ", "7enum ", "7fixed ");

		// Complete primitive types, logical types and anonymous types

		// Identifiers at a type location are Reference type until proven to be something else
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParents(AvroIdlIdentifier.class, AvroIdlReferenceType.class),
			"4boolean ", "4bytes ", "4int ", "4string ", "4float ", "4double ", "4long ",
			"4null ",
			"4date ", "4time_ms ", "4local_timestamp_ms ", "4timestamp_ms ", "4decimal(",
			"4union {", "4array<", "4map<"
		);

		// Complete message attributes & import types

		addBasicCompletion(psiElement().withParents(PsiErrorElement.class, AvroIdlMessageDeclaration.class),
			"throws ", "oneway");
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParents(PsiErrorElement.class, AvroIdlImportDeclaration.class),
			"9idl ", "protocol ", "schema ");

		// Complete special annotations: @namespace (protocols & named types), @order (record fields), @aliases (in both), @logicalType (types)

		addBasicCompletion(
			psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParents(AvroIdlIdentifier.class, AvroIdlSchemaProperty.class, AvroIdlProtocolDeclaration.class),
			'@', "namespace(\"\u0000\")");
		addBasicCompletion(
			psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParents(AvroIdlIdentifier.class, AvroIdlSchemaProperty.class, AvroIdlNamedSchemaDeclaration.class),
			'@', "namespace(\"\u0000\")", "aliases([\"\u0000\"])", "logicalType(\"\u0000\")");
		addBasicCompletion(
			psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParents(AvroIdlIdentifier.class, AvroIdlSchemaProperty.class, AvroIdlVariableDeclarator.class),
			'@', "aliases([\"\u0000\"])", "order(\"\u0000\")");
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParents(AvroIdlIdentifier.class, AvroIdlSchemaProperty.class, AvroIdlType.class),
			'@', "logicalType(\"\u0000\")");
		// Due to missing/present quotes, identify values of @order annotations by (in order): just after the (, and within a string literal
		addBasicCompletion(psiElement(AvroIdlTypes.IDENTIFIER_TOKEN).withParent(
				psiElement(PsiErrorElement.class).withParent(psiElement(AvroIdlSchemaProperty.class).withName("order"))),
			"\"ascending\"", "\"descending\"", "\"ignore\"");
		addBasicCompletion(psiElement(AvroIdlTypes.STRING_LITERAL).withParent(AvroIdlJsonValue.class)
				.withSuperParent(2, psiElement(AvroIdlSchemaProperty.class).withName("order")),
			"ascending", "descending", "ignore");
	}

	/**
	 * Equivalent to {@link #addBasicCompletion(ElementPattern, Character, String...) addBasicCompletion(pattern, null, completions)}.
	 */
	private void addBasicCompletion(ElementPattern<? extends PsiElement> pattern, String... completions) {
		addBasicCompletion(pattern, null, completions);
	}

	/**
	 * Add "basic" completions for a pattern. The completions have a syntax: the completion popup only shows word characters (without whitespace, parentheses,
	 * etc. but with any @ character), and the editor places the caret where a null char was (if any), or at the end.
	 * Completions may optionally be prefixed by a digit 1-9, which will be used as a priority (with 5 being neutral).
	 *
	 * @param pattern       the pattern to complete
	 * @param skipFirstChar if this string matches the first part of the prefix, skip that
	 * @param completions   the completions
	 */
	private void addBasicCompletion(ElementPattern<? extends PsiElement> pattern, Character skipFirstChar, String... completions) {
		List<LookupElement> lookupElements = new ArrayList<>();
		for (String completion : completions) {
			int priority;
			if (Character.isDigit(completion.charAt(0))) {
				priority = Character.digit(completion.charAt(0), 10) - 5;
				completion = completion.substring(1);
			} else {
				priority = 0;
			}
			final String textToPutInEditor = completion.replace("\u0000", "");
			final String textInTheCompletionPopup = completion.replaceAll("\\W&&[^@]", "");
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
			lookupElements.add(PrioritizedLookupElement.withPriority(lookupElementBuilder, priority));
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
