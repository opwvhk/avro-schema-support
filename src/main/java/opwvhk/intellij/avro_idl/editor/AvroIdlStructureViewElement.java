package opwvhk.intellij.avro_idl.editor;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.language.AvroIdlUtil;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class AvroIdlStructureViewElement extends PsiTreeElementBase<PsiElement> {

	public AvroIdlStructureViewElement(PsiElement psiElement) {
		super(psiElement);
	}

	@Override
    @NotNull
    public Collection<StructureViewTreeElement> getChildrenBase() {
		final PsiElement element = getElement();
		List<StructureViewTreeElement> children = new ArrayList<>();

		if (element instanceof AvroIdlFile) {
			for (PsiNamedElement child : PsiTreeUtil.getChildrenOfTypeAsList(getValue(), PsiNamedElement.class)) {
				children.add(new AvroIdlStructureViewElement(child));
			}
		} else if (element instanceof AvroIdlProtocolDeclaration) {
			final AvroIdlProtocolBody protocolBody = ((AvroIdlProtocolDeclaration) element).getProtocolBody();
			if (protocolBody != null) {
				for (AvroIdlImportDeclaration child : protocolBody.getImportDeclarationList()) {
					children.add(new AvroIdlStructureViewElement(child));
				}
				for (AvroIdlNamedSchemaDeclaration child : protocolBody.getNamedSchemaDeclarationList()) {
					children.add(new AvroIdlStructureViewElement(child));
				}
				for (AvroIdlMessageDeclaration child : protocolBody.getMessageDeclarationList()) {
					children.add(new AvroIdlStructureViewElement(child));
				}
			}
		} else if (element instanceof AvroIdlEnumDeclaration) {
			final AvroIdlEnumBody enumBody = ((AvroIdlEnumDeclaration) element).getEnumBody();
			if (enumBody != null) {
				for (AvroIdlEnumConstant child : enumBody.getEnumConstantList()) {
					children.add(new AvroIdlStructureViewElement(child));
				}
			}
		} else if (element instanceof AvroIdlRecordDeclaration) {
			final AvroIdlRecordBody recordBody = ((AvroIdlRecordDeclaration) element).getRecordBody();
			if (recordBody != null) {
				for (AvroIdlFieldDeclaration field : recordBody.getFieldDeclarationList()) {
					for (AvroIdlVariableDeclarator child : field.getVariableDeclaratorList()) {
						children.add(new AvroIdlStructureViewElement(child));
					}
				}
			}
		}

		return children;
	}

	@Override
    @Nullable
    public String getPresentableText() {
		if (getValue() instanceof AvroIdlNamedSchemaDeclaration) {
			return text(((AvroIdlNamedSchemaDeclaration) getValue()).getFullName());
		} else if (getValue() instanceof AvroIdlMessageDeclaration) {
			final AvroIdlMessageDeclaration messageDeclaration = (AvroIdlMessageDeclaration) getValue();
			String messageName = text(messageDeclaration.getName());
			StringJoiner parameters = new StringJoiner(", ");
			for (AvroIdlFormalParameter formalParameter : messageDeclaration.getFormalParameterList()) {
				final AvroIdlType parameterType = formalParameter.getType();
				final AvroIdlVariableDeclarator variableDeclarator = formalParameter.getVariableDeclarator();
				final String parameterName = variableDeclarator == null ? "???" : text(variableDeclarator.getName());
				parameters.add(text("%s %s", text(parameterType), parameterName));
			}
			String messageAttributes = "";
			final AvroIdlMessageAttributes messageAttrs = messageDeclaration.getMessageAttributes();
			if (messageAttrs != null) {
				StringJoiner buffer = new StringJoiner(", ", " ", "").setEmptyValue("");
				for (AvroIdlMessageAttributeThrows attributeThrows : messageAttrs.getMessageAttributeThrowsList()) {
					buffer.add(text(attributeThrows.getText()));
				}
				messageAttributes = " " + messageAttrs.getFirstChild().getText() + buffer;
			}
			return text("%s(%s)%s", messageName, parameters, messageAttributes);
		} else if (getValue() instanceof PsiNamedElement) {
			return text(((PsiNamedElement) getValue()).getName());
		} else if (getValue() instanceof AvroIdlImportDeclaration) {
			final AvroIdlImportDeclaration value = (AvroIdlImportDeclaration) getValue();
			final AvroIdlImportType importType = value.getImportType();
			final String imported = AvroIdlUtil.getJsonString(value.getJsonStringLiteral());
			if (importType == null) {
				return "???";
			}
			return text("%s: %s", importType.getText(), imported);
		}
		return getValue().getNode().getElementType().toString();
	}

	@NotNull
    private String text(@Nullable String text) {
		return text == null || text.isBlank() ? "???" : text;
	}

	@NotNull
    private String text(@NotNull String format, @Nullable Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter == null) {
				return "???";
			}
		}
		return String.format(format, parameters);
	}

	@NotNull
    private String text(@Nullable AvroIdlType type) {
		if (type instanceof AvroIdlArrayType) {
			return text("array<%s>", text(((AvroIdlArrayType) type).getType()));
		} else if (type instanceof AvroIdlMapType) {
			return text("map<%s>", text(((AvroIdlMapType) type).getType()));
		} else if (type instanceof AvroIdlUnionType) {
			final StringJoiner buffer = new StringJoiner(", ");
			for (AvroIdlType avroIdlType : ((AvroIdlUnionType) type).getTypeList()) {
				buffer.add(text(avroIdlType));
			}
			return text("union{%s}", buffer.toString());
		} else if (type != null) {
			final PsiElement lastChild = type.getLastChild();
			if (lastChild != null) {
				return lastChild.getText();
			}
		}
		return "???";
	}

	@Override
	public Icon getIcon(boolean open) {
		final PsiElement element = getValue();
		return AvroIdlIcons.getAvroIdlIcon(element);
	}
}
