package opwvhk.intellij.avro_idl.editor;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class AvroIdlColorSettingsPage implements ColorSettingsPage {
	@Override
	@Nullable
	public Icon getIcon() {
		return AvroIdlIcons.PLUGIN_ICON;
	}

	@Override
	@NotNull
	public SyntaxHighlighter getHighlighter() {
		return new AvroIdlSyntaxHighlighter();
	}

	@Override
	@NotNull
	public String getDemoText() {
		return """
		       /**
		        * An example protocol in Avro IDL
		        */
		       <annotation>@namespace("org.apache.avro.test")</annotation>
		       protocol Simple {

		           <annotation>@aliases(["org.foo.KindOf"])</annotation>
		           enum Kind {
		               FOO,
		               BAR, // the bar enum value
		               BAZ
		           }

		           fixed MD5(16);

		           record TestRecord {
		               <annotation>@order("ignore")</annotation>
		               string name;

		               <annotation>@order("descending")</annotation>
		               Kind kind;

		               MD5 hash;

		               union { MD5, null} <annotation>@aliases(["hash"])</annotation> nullableHash;

		               array<long> arrayOfLongs;
		           }

		           error TestError {
		               string message;
		           }

		           string hello(string greeting);
		           TestRecord echo(TestRecord `record`);
		           int add(int arg1, int arg2);
		           bytes echoBytes(bytes data);
		           void `error`() throws TestError;
		           void ping() oneway;
		       }
		       """;
	}

	@Override
	@Nullable
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		Map<String, TextAttributesKey> map = new HashMap<>();
		map.put("annotation", AvroIdlSyntaxColors.ANNOTATION);
		return map;
	}

	@Override
	public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
		return AvroIdlSyntaxColors.DESCRIPTORS;
	}

	@Override
	public ColorDescriptor @NotNull [] getColorDescriptors() {
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@Override
	@NotNull
	public String getDisplayName() {
		return AvroIdlLanguage.INSTANCE.getDisplayName();
	}
}
