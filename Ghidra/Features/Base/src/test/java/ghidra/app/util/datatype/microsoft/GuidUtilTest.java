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
package ghidra.app.util.datatype.microsoft;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GuidUtilTest {

	@Test
	public void testReadVersionTreatsHighByteAsUnsigned() {
		byte[] bytes = { 0x42, (byte) 0x80 };

		assertEquals(0x8042, GuidUtil.readVersion(bytes, 0));
	}

	@Test
	public void testReadVersionTreatsLowByteAsUnsigned() {
		byte[] bytes = { (byte) 0x80, 0x00 };

		assertEquals(0x0080, GuidUtil.readVersion(bytes, 0));
	}

	@Test
	public void testReadVersionSupportsMaximumUnsignedValue() {
		byte[] bytes = { (byte) 0xff, (byte) 0xff };

		assertEquals(0xffff, GuidUtil.readVersion(bytes, 0));
	}
}
