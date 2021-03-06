/*
 * Copyright 2016 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.gradle.spotless;

import static com.diffplug.gradle.spotless.Tasks.execute;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.gradle.api.Project;
import org.junit.Before;
import org.junit.Test;

import com.diffplug.spotless.FormatterStep;
import com.diffplug.spotless.LineEnding;
import com.diffplug.spotless.ResourceHarness;
import com.diffplug.spotless.TestProvisioner;

public class FormatTaskTest extends ResourceHarness {
	private SpotlessTask spotlessTask;

	@Before
	public void createTask() throws IOException {
		Project project = TestProvisioner.gradleProject(rootFolder());
		spotlessTask = project.getTasks().create("spotlessTaskUnderTest", SpotlessTask.class);
	}

	@Test
	public void testLineEndings() throws Exception {
		File testFile = setFile("testFile").toContent("\r\n");
		File outputFile = new File(spotlessTask.getOutputDirectory(), "testFile");

		spotlessTask.setLineEndingsPolicy(LineEnding.UNIX.createPolicy());
		spotlessTask.setTarget(Collections.singleton(testFile));
		execute(spotlessTask);

		assertFile(outputFile).hasContent("\n");
	}

	@Test
	public void testStep() throws Exception {
		File testFile = setFile("testFile").toContent("apple");
		File outputFile = new File(spotlessTask.getOutputDirectory(), "testFile");
		spotlessTask.setTarget(Collections.singleton(testFile));

		spotlessTask.addStep(FormatterStep.createNeverUpToDate("double-p", content -> content.replace("pp", "p")));
		execute(spotlessTask);

		assertFile(outputFile).hasContent("aple");
	}
}
