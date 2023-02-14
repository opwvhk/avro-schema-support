package opwvhk.intellij.avro_idl.language;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AvroIdlChooseByNameContributor implements ChooseByNameContributor {
	@Override
	public String @NotNull [] getNames(Project project, boolean includeNonProjectItems) {
		return AvroIdlUtil.findNavigableNamedSchemasInProject(project).stream()
				.map(NavigationItem::getName)
				.filter(Objects::nonNull)
				.toArray(String[]::new);
	}

	@Override
	public NavigationItem @NotNull [] getItemsByName(String name, String pattern,
	                                                 Project project,
	                                                 boolean includeNonProjectItems) {
		return AvroIdlUtil.findNavigableNamedSchemasInProject(project)
				.toArray(NavigationItem[]::new);
	}
}
