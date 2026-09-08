/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.plugin.core.blockmodel;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import ghidra.program.model.block.SimpleBlockModel;

public class BlockModelServicePluginTest {

	@Test
	public void testBlockModelInfoEqualsComparesModelNamesByValue() throws Exception {
		String firstName = new String("Test Model");
		String secondName = new String("Test Model");
		assertNotSame(firstName, secondName);

		Object first = newBlockModelInfo(firstName);
		Object second = newBlockModelInfo(secondName);

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	private Object newBlockModelInfo(String modelName) throws Exception {
		Class<?> infoClass = Class.forName(
			"ghidra.app.plugin.core.blockmodel.BlockModelServicePlugin$BlockModelInfo");
		Constructor<?> constructor = infoClass.getDeclaredConstructor(String.class, Class.class);
		constructor.setAccessible(true);
		return constructor.newInstance(modelName, SimpleBlockModel.class);
	}
}
