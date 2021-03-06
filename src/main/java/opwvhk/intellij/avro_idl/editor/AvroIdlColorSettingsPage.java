package opwvhk.intellij.avro_idl.editor;

import opwvhk.intellij.avro_idl.AvroIdlIcons;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import opwvhk.intellij.avro_idl.syntax.AvroIdlSyntaxColors;
import opwvhk.intellij.avro_idl.syntax.AvroIdlSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class AvroIdlColorSettingsPage implements ColorSettingsPage {
    @Nullable
    @Override
    public Icon getIcon() {
        return AvroIdlIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new AvroIdlSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return
        "/**\n" +
        " * An example protocol in Avro IDL\n" +
        " */\n" +
        "<annotation>@namespace(\"org.apache.avro.test\")</annotation>\n" +
        "protocol Simple {\n" +
        "\n" +
        "    <annotation>@aliases([\"org.foo.KindOf\"])</annotation>\n" +
        "    enum Kind {\n" +
        "        FOO,\n" +
        "        BAR, // the bar enum value\n" +
        "        BAZ\n" +
        "    }\n" +
        "\n" +
        "    fixed MD5(16);\n" +
        "\n" +
        "    record TestRecord {\n" +
        "        <annotation>@order(\"ignore\")</annotation>\n" +
        "        string name;\n" +
        "\n" +
        "        <annotation>@order(\"descending\")</annotation>\n" +
        "        Kind kind;\n" +
        "\n" +
        "        MD5 hash;\n" +
        "\n" +
        "        union { MD5, null} <annotation>@aliases([\"hash\"])</annotation> nullableHash;\n" +
        "\n" +
        "        array<long> arrayOfLongs;\n" +
        "    }\n" +
        "\n" +
        "    error TestError {\n" +
        "        string message;\n" +
        "    }\n" +
        "\n" +
        "    string hello(string greeting);\n" +
        "    TestRecord echo(TestRecord `record`);\n" +
        "    int add(int arg1, int arg2);\n" +
        "    bytes echoBytes(bytes data);\n" +
        "    void `error`() throws TestError;\n" +
        "    void ping() oneway;\n" +
        "}\n";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        Map<String, TextAttributesKey> map = new HashMap<>();
        map.put("annotation", AvroIdlSyntaxColors.ANNOTATION);
        return map;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return AvroIdlSyntaxColors.DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return AvroIdlLanguage.INSTANCE.getDisplayName();
    }
}
