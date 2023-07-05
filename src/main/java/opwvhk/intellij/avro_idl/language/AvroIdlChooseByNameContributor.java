package opwvhk.intellij.avro_idl.language;

import com.intellij.navigation.ChooseByNameContributorEx;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FindSymbolParameters;
import com.intellij.util.indexing.IdFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public class AvroIdlChooseByNameContributor implements ChooseByNameContributorEx {
	@Override
	public void processNames(@NotNull Processor<? super String> processor, @NotNull GlobalSearchScope scope,
	                         @Nullable IdFilter filter) {
		AvroIdlUtil.findDeclarationsInScope(scope).stream()
				.map(NavigationItem::getName)
				.filter(Objects::nonNull)
				// These last two commands have the same effect as com.intellij.util.containers.ContainerUtil#process:
				// they process all elements until processor#process(String) returns false
				.takeWhile(processor::process)
				.forEach(ignored -> {
					// Allow stopping early
					ProgressManager.checkCanceled();
				});
	}

	@Override
	public void processElementsWithName(@NotNull String name, @NotNull Processor<? super NavigationItem> processor,
	                                    @NotNull FindSymbolParameters parameters) {
		Predicate<NavigationItem> filter = item -> name.equals(item.getName());
		AvroIdlUtil.findDeclarationsInScope(parameters.getSearchScope()).stream()
				.filter(filter)
				// These last two commands have the same effect as com.intellij.util.containers.ContainerUtil#process:
				// they process all elements until processor#process(String) returns false
				.takeWhile(processor::process)
				.forEach(ignored -> {
					// Allow stopping early
					ProgressManager.checkCanceled();
				});
	}
}
