/*
 * Copyright 2003-2016 Dave Griffith, Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package opwvhk.intellij.avro_idl.naming;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.RefactoringQuickFix;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.RefactoringActionHandlerFactory;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.intellij.refactoring.rename.RenameHandler;
import com.intellij.refactoring.rename.RenameHandlerRegistry;
import com.intellij.util.IncorrectOperationException;
import opwvhk.intellij.avro_idl.TextBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RenameFix implements RefactoringQuickFix {

	private final String targetName;
	private final boolean searchInStrings;
	private final boolean searchInNonJavaFiles;

	public RenameFix() {
		this(null);
	}

	public RenameFix(@NonNls String targetName) {
		this(targetName, true, true);
	}

	public RenameFix(@NonNls String targetName, boolean searchInStrings, boolean searchInNonJavaFiles) {
		this.targetName = targetName;
		this.searchInStrings = searchInStrings;
		this.searchInNonJavaFiles = searchInNonJavaFiles;
	}

	@Override
	@NotNull
	public String getFamilyName() {
		return TextBundle.message("naming.fix.group");
	}

	@Override
	@NotNull
	public String getName() {
		if (targetName == null) {
			return TextBundle.message("naming.fix.name");
		} else {
			return TextBundle.message("naming.fix.name.with.target", targetName);
		}
	}

	@Override
	@NotNull
	public RefactoringActionHandler getHandler() {
		return RefactoringActionHandlerFactory.getInstance().createRenameHandler();
	}

	@Override
	@NotNull
	public RefactoringActionHandler getHandler(@NotNull DataContext context) {
		RenameHandler renameHandler = RenameHandlerRegistry.getInstance().getRenameHandler(context);
		return renameHandler != null ? renameHandler : getHandler();
	}

	public void doFix(Project project, ProblemDescriptor descriptor) {
		if (targetName == null) {
			doFix(descriptor.getPsiElement());
		} else {
			final PsiElement nameIdentifier = descriptor.getPsiElement();
			final PsiElement elementToRename = nameIdentifier.getParent();
			final RefactoringFactory factory = RefactoringFactory.getInstance(project);
			final RenameRefactoring renameRefactoring =
					factory.createRename(elementToRename, targetName, searchInStrings, searchInNonJavaFiles);
			renameRefactoring.run();
		}
	}

	@Override
	public final void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
		final PsiElement problemElement = descriptor.getPsiElement();
		if (problemElement == null || !problemElement.isValid()) {
			return;
		}
		try {
			doFix(project, descriptor);
		} catch (IncorrectOperationException e) {
			final Class<?> myClass = getClass();
			final Logger logger = Logger.getInstance(myClass);
			logger.error(e);
		}
	}
}
