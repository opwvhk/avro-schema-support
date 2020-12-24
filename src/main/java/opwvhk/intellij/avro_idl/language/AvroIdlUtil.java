package opwvhk.intellij.avro_idl.language;

import com.intellij.psi.PsiElement;
import groovy.json.StringEscapeUtils;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.Nullable;

public class AvroIdlUtil {

	public static String getJsonString(@Nullable AvroIdlJsonValue jsonValue) {
		return jsonValue == null ? null : stringValue(jsonValue.getStringLiteral());
	}

	@Nullable
	public static String stringValue(@Nullable PsiElement stringLiteral) {
		if (stringLiteral == null) {
			return null;
		} else {
			String escapedLiteralWithQuotes = stringLiteral.getText();
			String escapedLiteral = escapedLiteralWithQuotes.substring(1, escapedLiteralWithQuotes.length() - 1);
			return StringEscapeUtils.unescapeJavaScript(escapedLiteral);
		}
	}

	public static boolean hasJsonIntValueForInteger(@Nullable AvroIdlJsonValue jsonValue) {
		final PsiElement intLiteral = jsonValue == null ? null : jsonValue.getIntLiteral();
		if (intLiteral == null) {
			return false;
		}
		long value = Long.parseLong(intLiteral.getText());
		return value == (int) value; // If values don't match, value is larger than Integer.MAX_VALUE
	}

	public static Long getJsonIntValue(@Nullable AvroIdlJsonValue jsonValue) {
		final PsiElement intLiteral = jsonValue == null ? null : jsonValue.getIntLiteral();
		if (intLiteral == null) {
			return null;
		}
		return Long.parseLong(intLiteral.getText());
	}
}
