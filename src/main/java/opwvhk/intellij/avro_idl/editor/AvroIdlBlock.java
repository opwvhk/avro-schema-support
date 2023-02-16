package opwvhk.intellij.avro_idl.editor;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlBlock extends AbstractBlock {
	/*
	 * Token sets for all declarations; obvious ones like protocols & records, but also (anonymous) field & message return types.
	 * Sets to identify the preamble (annotations & documentation), the actual declaration, the start, content and end of the body (if any), and
	 * anything that comes after that.
	 *
	 * We'll be testing for the preamble elements, and the body start & end to make the algorithm robust against changes to the types (especially logical types).
	 */
	protected static final TokenSet DECLARATIONS = TokenSet.create(PROTOCOL_DECLARATION, FIXED_DECLARATION,
			ENUM_DECLARATION, RECORD_DECLARATION, MESSAGE_DECLARATION, FIELD_DECLARATION, TYPE, RESULT_TYPE,
			PRIMITIVE_TYPE, ARRAY_TYPE, MAP_TYPE, UNION_TYPE, REFERENCE_TYPE);
	private static final TokenSet DECLARATION_BODIES_WITH_UNINDENTED_CHILDREN = TokenSet.create(PROTOCOL_BODY,
			ENUM_BODY, RECORD_BODY);

	// IMPORTANT: Include *all* non-leaf grammar elements here. If not, formatting & formatting preferences WILL break!
	// Its use is always inverted, but listing leaf elements yields >90 results...
	private static final TokenSet NON_LEAF_ELEMENTS = TokenSet.create(ARRAY_TYPE, DECIMAL_TYPE, ENUM_CONSTANT,
			ENUM_BODY, ENUM_DECLARATION, ENUM_DEFAULT, FIELD_DECLARATION, FIXED_DECLARATION, FORMAL_PARAMETER,
			IMPORT_DECLARATION, IMPORT_TYPE, JSON_ARRAY, JSON_OBJECT, JSON_PAIR, JSON_VALUE, MAP_TYPE,
			MESSAGE_ATTRIBUTES, MESSAGE_ATTRIBUTE_THROWS, MESSAGE_DECLARATION, NAMED_SCHEMA_DECLARATION, NULLABLE_TYPE,
			PRIMITIVE_TYPE, PROTOCOL_BODY, PROTOCOL_DECLARATION, RECORD_BODY, RECORD_DECLARATION, REFERENCE_TYPE,
			RESULT_TYPE, SCHEMA_PROPERTY, TYPE, UNION_TYPE, VARIABLE_DECLARATOR);

	private static final TokenSet ELEMENTS_THAT_WRAP_BEFORE_CHILDREN = TokenSet.create(MESSAGE_ATTRIBUTE_THROWS,
			JSON_PAIR);
	private static final TokenSet ELEMENTS_THAT_NEVER_WRAP = TokenSet.create(COMMA, SEMICOLON, LEFT_PAREN, RIGHT_PAREN,
			LEFT_ANGLE, RIGHT_ANGLE, LEFT_BRACKET, RIGHT_BRACKET);

	private final Indent myIndent;
	private final SpacingBuilder mySpacingBuilder;
	private final List<ChildAttributes> newSubBlockAttributes;

	protected AvroIdlBlock(@NotNull ASTNode node, @NotNull Wrap wrap, @Nullable Alignment alignment, Indent indent,
	                       @NotNull SpacingBuilder spacingBuilder) {
		super(node, wrap, alignment);
		myIndent = indent;
		mySpacingBuilder = spacingBuilder;
		newSubBlockAttributes = new ArrayList<>();
	}

	@Override
	protected List<Block> buildChildren() {
		if (myNode.getFirstChildNode() == null) {
			return EMPTY;
		}

		final Wrap childWrap = createChildWrap();
		List<Block> blocks = new ArrayList<>();
		@NotNull final ASTNode[] children = myNode.getChildren(null);
		for (int i = 0; i < children.length; i++) {
			ASTNode child = children[i];
			final IElementType childElementType = child.getElementType();
			if (childElementType == WHITE_SPACE) {
				continue;
			}
			final Indent childIndent = getChildIndent(i);
			final AvroIdlBlock block;
			if (DECLARATIONS.contains(childElementType)) {
				block = new AvroIdlDeclarationBlock(child, childWrap, null, childIndent, mySpacingBuilder);
			} else {
				block = new AvroIdlBlock(child, childWrap, null, childIndent, mySpacingBuilder);
			}
			// When adding a new block before the block added above, use these
			final Indent previousChildIndent = getPreviousChildIndent(i);
			newSubBlockAttributes.add(new ChildAttributes(previousChildIndent, null));
			blocks.add(block);
		}
		// When adding a new block at the end, take the next child index
		final Indent nextChildIndent = getPreviousChildIndent(children.length);
		newSubBlockAttributes.add(new ChildAttributes(nextChildIndent, null));

		return blocks;
	}

	@NotNull
	private Wrap createChildWrap() {
		final IElementType elementType = myNode.getElementType();
		if (ELEMENTS_THAT_WRAP_BEFORE_CHILDREN.contains(elementType)) {
			return Wrap.createChildWrap(myWrap, WrapType.NORMAL, false);
		} else if (ELEMENTS_THAT_NEVER_WRAP.contains(elementType)) {
			return Wrap.createWrap(WrapType.NONE, false);
		}

		// No specifics? Then use a simple default.
		return Wrap.createWrap(WrapType.NORMAL, false);
	}


	@Nullable
	protected Indent getPreviousChildIndent(int childNodeIndex) {
		return getChildIndent();
	}

	@Nullable
	protected Indent getChildIndent(int childNodeIndex) {
		return getChildIndent();
	}

	@Override
	@Nullable
	protected Indent getChildIndent() {
		final IElementType myElementType = myNode.getElementType();

		if (DECLARATION_BODIES_WITH_UNINDENTED_CHILDREN.contains(myElementType) || myNode instanceof FileElement) {
			return Indent.getNoneIndent();
		}
		// Use platform default (a continuation indent)
		return null;
	}

	@Override
	@NotNull
	public ChildAttributes getChildAttributes(int newChildBlockIndex) {
		if (newSubBlockAttributes.isEmpty()) {
			// If we get here, we have not built sub blocks.
			return super.getChildAttributes(newChildBlockIndex);
		}
		return newSubBlockAttributes.get(newChildBlockIndex);
	}

	@Override
	public Indent getIndent() {
		return myIndent;
	}

	@Override
	@Nullable
	public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
		return mySpacingBuilder.getSpacing(this, child1, child2);
	}

	@Override
	public boolean isLeaf() {
		return !NON_LEAF_ELEMENTS.contains(myNode.getElementType());
	}

	@Override
	public String toString() {
		return String.format("%s -> %s / %s / %s", myNode.getElementType(), myIndent, myWrap, myAlignment);
	}
}
