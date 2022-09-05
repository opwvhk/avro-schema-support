package opwvhk.intellij.avro_idl.naming;

import com.intellij.codeInspection.naming.NamingConvention;
import com.intellij.codeInspection.naming.NamingConventionBean;
import opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NonNls;

import static java.util.Objects.requireNonNull;

public abstract class AvroIdlNamingConvention extends NamingConvention<AvroIdlNameIdentifierOwner> {
	public static final String IDENTIFIER_START_UPPERCASE = "[A-Z][a-zA-Z0-9]*";
	public static final String IDENTIFIER_START_LOWERCASE = "[a-z][a-zA-Z0-9]*";

	private final Class<? extends AvroIdlNameIdentifierOwner> namedTypeClass;
	private final int minLength;
	private final int maxLength;
	@RegExp
	private final String regex;

	public AvroIdlNamingConvention(Class<? extends AvroIdlNameIdentifierOwner> namedTypeClass, int minLength, int maxLength, @RegExp String regex) {
		this.namedTypeClass = requireNonNull(namedTypeClass);
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.regex = regex;
	}

	@Override
	public @NonNls String getShortName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isApplicable(AvroIdlNameIdentifierOwner member) {
		return namedTypeClass.isInstance(member) && member.getName() != null;
	}

	@Override
	public boolean isEnabledByDefault() {
		return true;
	}

	@Override
	public NamingConventionBean createDefaultBean() {
		return new NamingConventionBean(regex, minLength, maxLength);
	}
}
