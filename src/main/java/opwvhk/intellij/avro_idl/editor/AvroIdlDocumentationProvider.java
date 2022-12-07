package opwvhk.intellij.avro_idl.editor;

import com.intellij.codeInsight.hint.DeclarationRangeUtil;
import com.intellij.lang.Language;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.FakePsiElement;
import com.intellij.psi.presentation.java.SymbolPresentationUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.lang.documentation.DocumentationMarkup.*;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

@SuppressWarnings("UnstableApiUsage")
public class AvroIdlDocumentationProvider extends AbstractDocumentationProvider {
	public static final TokenSet DECIMAL_TYPE_DECLARATION_ELEMENTS = TokenSet.create(DECIMAL, INT_LITERAL, COMMA, LEFT_PAREN, RIGHT_PAREN);
	public static final TokenSet NON_DECIMAL_NULLABLE_TYPE_ELEMENTS = TokenSet.create(BOOLEAN, BYTES, INT, STRING, FLOAT, DOUBLE, LONG, NULL,
		DATE, TIME_MS, TIMESTAMP_MS, LOCAL_TIMESTAMP_MS, UUID, IDENTIFIER);
	/**
	 * Pattern to match the common whitespace indents in a multi-line String.
	 * Doesn't match a single-line String, fully matches any multi-line String.
	 * <p>
	 * To use: match on a {@link String#trim() trimmed} String, and then replace all
	 * newlines followed by the group "indent" with a newline.
	 */
	private static final Pattern WS_INDENT = Pattern.compile("(?U).*\\R(?<indent>\\h*).*(?:\\R\\k<indent>.*)*");
	/**
	 * Pattern to match the whitespace indents plus common stars (1 or 2) in a
	 * multi-line String. If a String fully matches, replace all occurrences of a
	 * newline followed by whitespace and then the group "stars" with a newline.
	 * <p>
	 * Note: partial matches are invalid.
	 */
	private static final Pattern STAR_INDENT = Pattern.compile("(?U)(?<stars>\\*{1,2}).*(?:\\R\\h*\\k<stars>.*)*");

	@Override
    @Nullable
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
		if (element instanceof AvroIdlNameIdentifierOwner) {
			final String name = ((AvroIdlNameIdentifierOwner)element).getName();
			final String file = SymbolPresentationUtil.getFilePathPresentation(element.getContainingFile());
			return "\"" + name + "\" in " + file;
		}
		return null;
	}

	@Override
    @Nullable
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
		TextRange declarationRange = DeclarationRangeUtil.getPossibleDeclarationAtRange(element);
		if (declarationRange == null) {
			return null;
		}

		PsiDocCommentBase docComment = findDocComment(element.getContainingFile(), declarationRange);
		String documentation = docComment == null ? null : generateRenderedDoc(docComment);

		String declaration = renderDeclaration(element);
		if (declaration == null) {
			return null;
		}

		return formatDeclaration(declaration, documentation);
	}

	private String renderDeclaration(PsiElement element) {
		if (element instanceof AvroIdlNameIdentifierOwner) {
			String name = ((AvroIdlNameIdentifierOwner)element).getName();
			if (name == null) {
				return null;
			}
			if (element instanceof AvroIdlRecordDeclaration) {
				boolean errorType = ((AvroIdlRecordDeclaration)element).isErrorType();
				String properties = renderProperties((AvroIdlWithSchemaProperties)element, "\n");
				return properties + (errorType ? "error " : "record ") + name;
			} else if (element instanceof AvroIdlEnumDeclaration) {
				String properties = renderProperties((AvroIdlWithSchemaProperties)element, "\n");
				return properties + "enum " + name;
			} else if (element instanceof AvroIdlFixedDeclaration) {
				PsiElement intLiteral = ((AvroIdlFixedDeclaration)element).getIntLiteral();
				String properties = renderProperties((AvroIdlWithSchemaProperties)element, "\n");
				return intLiteral != null ? properties + "fixed " + name + "(" + intLiteral.getText() + ")" : null;
			} else if (element instanceof AvroIdlVariableDeclarator) {
				PsiElement parent = element.getParent();
				AvroIdlType type = parent instanceof AvroIdlFieldDeclaration ?
				                   ((AvroIdlFieldDeclaration)parent).getType() :
				                   ((AvroIdlFormalParameter)parent).getType();
				String properties = renderProperties((AvroIdlWithSchemaProperties)element, " ");
				return formatType(type) + " " + properties + name;
			} else if (element instanceof AvroIdlEnumConstant) {
				return name;
			} else if (element instanceof AvroIdlProtocolDeclaration) {
				String properties = renderProperties((AvroIdlWithSchemaProperties)element, "\n");
				return properties + "protocol " + name;
			} else if (element instanceof AvroIdlMessageDeclaration) {
				String properties = renderProperties((AvroIdlWithSchemaProperties)element, "\n");
				List<AvroIdlFormalParameter> formalParameters = ((AvroIdlMessageDeclaration)element).getFormalParameterList();
				Stream<AvroIdlType> formalParameterTypes = formalParameters.stream().map(AvroIdlFormalParameter::getType);
				String messageSignature = name + "(" + formalParameterTypes.map(this::formatType).collect(Collectors.joining(", ")) + ")";
				return properties + messageSignature;
			}
		} else if (element instanceof AvroIdlFieldDeclaration) {
			List<AvroIdlVariableDeclarator> variableDeclarators = ((AvroIdlFieldDeclaration)element).getVariableDeclaratorList();
			return variableDeclarators.stream()
				.map(this::renderDeclaration)
				.collect(Collectors.joining("\n"));
		} else if (element instanceof AvroIdlFormalParameter) {
			return renderDeclaration(((AvroIdlFormalParameter)element).getVariableDeclarator());
		}
		return null;
	}

	@NotNull
    private String renderProperties(@NotNull AvroIdlWithSchemaProperties element, @NotNull String separator) {
		List<AvroIdlSchemaProperty> schemaProperties = element.getSchemaPropertyList();
		if (schemaProperties.isEmpty()) {
			return "";
		}
		return schemaProperties.stream().map(PsiElement::getText).collect(Collectors.joining(separator)) + separator;
	}

	private String formatType(@Nullable AvroIdlType type) {
		if (type instanceof AvroIdlArrayType) {
			final AvroIdlType elementType = ((AvroIdlArrayType)type).getType();
			return elementType == null ? null : "array<" + formatType(elementType) + ">";
		} else if (type instanceof AvroIdlMapType) {
			final AvroIdlType valueType = ((AvroIdlMapType)type).getType();
			return valueType == null ? null : "map<" + formatType(valueType) + ">";
		} else if (type instanceof AvroIdlUnionType) {
			final List<AvroIdlType> typeList = ((AvroIdlUnionType)type).getTypeList();
			return typeList.isEmpty() ? null : typeList.stream().map(this::formatType).collect(Collectors.joining(", ", "union { ", " }"));
		} else if (type instanceof AvroIdlDecimalType) {
			final List<PsiElement> declarationElements = childrenByElementType(DECIMAL_TYPE_DECLARATION_ELEMENTS, type);
			if (declarationElements.isEmpty()) {
				return null;
			} else {
				final String declaration = declarationElements.stream().map(PsiElement::getText).collect(Collectors.joining());
				return type.isOptional() ? declaration + "?" : declaration;
			}
		} else if (type instanceof AvroIdlNullableType) {
			final List<PsiElement> declarationElements = childrenByElementType(NON_DECIMAL_NULLABLE_TYPE_ELEMENTS, type);
			if (declarationElements.isEmpty()) {
				return null;
			} else {
				final String declaration = declarationElements.get(0).getText();
				return type.isOptional() ? declaration + "?" : declaration;
			}
		} else {
			// type == null
			return null;
		}
	}

	private List<PsiElement> childrenByElementType(@SuppressWarnings("SameParameterValue") TokenSet filter, PsiElement element) {
		List<PsiElement> result = new ArrayList<>();
		for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (filter == null || filter.contains(child.getNode().getElementType())) {
				result.add(child);
			}
		}
		return result;
	}

	private String formatDeclaration(@NotNull String declaration, @Nullable String renderedDocumentation) {
		return DEFINITION_START + declaration.replace("\n", "<br/>") + DEFINITION_END +
			CONTENT_START +
			//(renderedDocumentation == null ? CodeInsightBundle.message("no.documentation.found") : documentation) +
			(renderedDocumentation == null ? "&nbsp;" : renderedDocumentation) +
			CONTENT_END;
	}

	@Override
    @Nls
    @Nullable
    public String generateRenderedDoc(@NotNull PsiDocCommentBase comment) {
		String rawComment = comment.getText();
		String commentContent = rawComment.substring(3, rawComment.length() - 2);
		String documentation = stripIndents(commentContent.strip());
		return documentation.replace("\n", "<br/>");
	}

	/* Package private to facilitate testing */
	static String stripIndents(String doc) {
		Matcher starMatcher = STAR_INDENT.matcher(doc);
		if (starMatcher.matches()) {
			return doc.replaceAll("(?U)(?:^|(\\R)\\h*)\\Q" + starMatcher.group("stars") + "\\E\\h?", "$1");
		}

		Matcher whitespaceMatcher = WS_INDENT.matcher(doc);
		if (whitespaceMatcher.matches()) {
			return doc.replaceAll("(?U)(\\R)" + whitespaceMatcher.group("indent"), "$1");
		}

		return doc;
	}

	@Override
	public void collectDocComments(@NotNull PsiFile file, @NotNull Consumer<? super @NotNull PsiDocCommentBase> sink) {
		if (!(file instanceof AvroIdlFile)) {
			return;
		}

		List<AvroIdlNameIdentifierOwner> possiblyDocumentedElements = new ArrayList<>();
		AvroIdlProtocolDeclaration protocolDeclaration = PsiTreeUtil.findChildOfType(file, AvroIdlProtocolDeclaration.class);
		if (protocolDeclaration != null) {
			possiblyDocumentedElements.add(protocolDeclaration);
			AvroIdlProtocolBody protocolBody = protocolDeclaration.getProtocolBody();
			if (protocolBody != null) {
				possiblyDocumentedElements.addAll(protocolBody.getNamedSchemaDeclarationList());
			}
		} else {
			possiblyDocumentedElements.addAll(PsiTreeUtil.getChildrenOfTypeAsList(file, AvroIdlNamedSchemaDeclaration.class));
		}

		possiblyDocumentedElements.stream().map(this::findDocComment).filter(Objects::nonNull).forEach(sink);
	}

	@Override
    @Nullable
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
		if (object instanceof PsiElement) {
			return (PsiElement)object;
		}
		return null;
	}

	@Override
    @Nullable
    public PsiDocCommentBase findDocComment(@NotNull PsiFile file, @NotNull TextRange range) {
		PsiElement element = file.findElementAt(range.getStartOffset());
		final AvroIdlNameIdentifierOwner nameIdentifierOwner = PsiTreeUtil.getParentOfType(element, AvroIdlNameIdentifierOwner.class, false);
		if (nameIdentifierOwner == null) {
			return null;
		}
		return findDocComment(nameIdentifierOwner);
	}

	@Nullable
    private PsiDocCommentBase findDocComment(AvroIdlNameIdentifierOwner element) {
		PsiComment docComment = null;
		if (element instanceof AvroIdlVariableDeclarator) {
			docComment = findDeclarationDocComment(element);
			if (docComment == null) {
				docComment = findDeclarationDocComment(element.getParent());
			}
		} else if ((element instanceof AvroIdlNamedSchemaDeclaration || element instanceof AvroIdlEnumConstant ||
			element instanceof AvroIdlFieldDeclaration) || element instanceof AvroIdlProtocolDeclaration ||
			element instanceof AvroIdlMessageDeclaration || element instanceof AvroIdlFormalParameter) {
			docComment = findDeclarationDocComment(element);
		}

		return docComment == null ? null : new FakePsiDocComment(element, docComment);
	}

	@Nullable
    private PsiComment findDeclarationDocComment(@Nullable PsiElement element) {
		final PsiElement prevLeaf = AvroIdlPsiUtil.prevNonCommentLeaf(element);
		if (prevLeaf instanceof PsiComment) {
			return (PsiComment)prevLeaf;
		}
		return null;
	}

	private static class FakePsiDocComment extends FakePsiElement implements PsiDocCommentBase {
		private final AvroIdlNameIdentifierOwner owner;
		private final PsiComment comment;

		public FakePsiDocComment(AvroIdlNameIdentifierOwner owner, PsiComment comment) {
			this.owner = owner;
			this.comment = comment;
		}

		@Override
		public int getStartOffsetInParent() {
			return comment.getTextOffset() - owner.getTextOffset();
		}

		@Override
        @Nullable
        public TextRange getTextRange() {
			final int startOffset = getTextOffset();
			return new TextRange(startOffset, startOffset + getTextLength());
		}

		@Override
		public int getTextLength() {
			return comment.getTextLength();
		}

		@Override
        @Nullable
        @NonNls
        public String getText() {
			return comment.getText();
		}

		@Override
		public String getName() {
			return owner.getName();
		}

		@Override
        @Nullable
        public PsiElement getOwner() {
			return owner;
		}

		@Override
        @NotNull
        public Language getLanguage() {
			return owner.getLanguage();
		}

		@Override
        @NotNull
        public IElementType getTokenType() {
			return DOC_COMMENT;
		}

		@Override
		public PsiElement getParent() {
			return owner.getParent();
		}
	}
}
