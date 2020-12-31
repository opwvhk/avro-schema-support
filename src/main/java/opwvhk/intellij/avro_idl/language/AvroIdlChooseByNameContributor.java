package opwvhk.intellij.avro_idl.language;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamedSchemaDeclaration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class AvroIdlChooseByNameContributor implements ChooseByNameContributor {
	@Override
	public @NotNull String[] getNames(Project project, boolean includeNonProjectItems) {
		final List<AvroIdlNamedSchemaDeclaration> allAvailableTypes = AvroIdlUtil.findAllAvailableTypes(project);
		return allAvailableTypes.stream().flatMap(type -> {
			String fullName = type.getFullName();
			return fullName == null || fullName.isBlank() ? Stream.empty() : Stream.of(fullName);
		}).toArray(String[]::new);
	}

	@Override
	public @NotNull NavigationItem[] getItemsByName(String name, String pattern,
													Project project,
													boolean includeNonProjectItems) {
		final List<AvroIdlNamedSchemaDeclaration> allAvailableTypes = AvroIdlUtil.findAllAvailableTypes(project);
		return allAvailableTypes.stream().flatMap(type -> {
			String fullName = type.getFullName();
			return fullName != null && fullName.equals(name) ? Stream.of((NavigationItem) type) : Stream.empty();
		}).toArray(NavigationItem[]::new);
	}
}
