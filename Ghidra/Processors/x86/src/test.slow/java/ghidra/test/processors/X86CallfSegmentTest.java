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
package ghidra.test.processors;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ghidra.program.database.ProgramBuilder;
import ghidra.program.model.lang.Register;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.Program;
import ghidra.program.model.pcode.PcodeOp;
import ghidra.test.AbstractGhidraHeadlessIntegrationTest;

public class X86CallfSegmentTest extends AbstractGhidraHeadlessIntegrationTest {

	private static final String LANGUAGE_ID = "x86:LE:16:Protected Mode";

	private ProgramBuilder builder;
	private Program program;

	@Before
	public void setUp() throws Exception {
		builder = new ProgramBuilder("CALLF segment test", LANGUAGE_ID);
		builder.createMemory("test", "0x1000", 0x100);
		program = builder.getProgram();
	}

	@After
	public void tearDown() {
		if (builder != null) {
			builder.dispose();
		}
	}

	@Test
	public void testBpRelativeCallfDefaultsToSs() throws Exception {
		assertAddressSegment("FF 5E E8", "SS");
	}

	@Test
	public void testBxRelativeCallfDefaultsToDs() throws Exception {
		assertAddressSegment("FF 5F E8", "DS");
	}

	@Test
	public void testBpRelativeCallfHonorsDsOverride() throws Exception {
		assertAddressSegment("3E FF 5E E8", "DS");
	}

	private void assertAddressSegment(String bytes, String registerName) throws Exception {
		builder.setBytes("0x1000", bytes);
		builder.disassemble("0x1000", bytes.split(" ").length);

		Instruction instruction =
			program.getListing().getInstructionAt(builder.addr(0x1000));
		assertNotNull(instruction);
		assertEquals("CALLF", instruction.getMnemonicString());

		PcodeOp segmentOp = getAddressSegmentOp(instruction);
		Register expected = builder.getRegister(registerName);
		assertEquals(expected.getAddress(), segmentOp.getInput(1).getAddress());
	}

	private PcodeOp getAddressSegmentOp(Instruction instruction) {
		for (PcodeOp op : instruction.getPcode()) {
			if (op.getOpcode() != PcodeOp.CALLOTHER) {
				continue;
			}
			int useropIndex = (int) op.getInput(0).getOffset();
			if ("segment".equals(program.getLanguage().getUserDefinedOpName(useropIndex))) {
				return op;
			}
		}
		fail("CALLF did not emit a segment p-code operation");
		return null;
	}
}
