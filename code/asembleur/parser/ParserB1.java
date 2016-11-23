package parser;

import instr.set.SquelInstrB1;
import parser.exception.NotInstructionException;

public class ParserB1 {

	/**
	 * permet de reconnaitre une instruction et la transforme en jeton
	 * @param s la chaine a reconnaitre
	 * @return l'instruction reconnue
	 */
	public static int parseB1Instr(String s) throws NotInstructionException
	{
		switch(s.trim().toLowerCase().charAt(0))
		{
		case 'a':
			if(s.contentEquals("and"))
				return SquelInstrB1.AND;
			if(s.contentEquals("add"))
				return SquelInstrB1.ADD;
			throw new NotInstructionException();
		case 'b':
			if(s.contentEquals("bus"))
				return SquelInstrB1.BUS;
			throw new NotInstructionException();
		case 'c':
			if(s.contentEquals("cp"))
				return SquelInstrB1.CP;
			if(s.contentEquals("call"))
				return SquelInstrB1.CALL;
			if(s.contentEquals("cmp"))
				return SquelInstrB1.CMP;
			if(s.contentEquals("co"))
				return SquelInstrB1.CO;
			throw new NotInstructionException();
		case 'd':
			if(s.contentEquals("div"))
				return SquelInstrB1.DIV;
			if(s.contentEquals("dec"))
				return SquelInstrB1.DEC;
			throw new NotInstructionException();
		case 'h':
			if(s.contentEquals("halt"))
				return SquelInstrB1.HALT;
			throw new NotInstructionException();
		case 'i':
			if(s.contentEquals("int"))
				return SquelInstrB1.INT;
			if(s.contentEquals("iret"))
				return SquelInstrB1.IRET;
			if(s.contentEquals("inc"))
				return SquelInstrB1.INC;
			throw new NotInstructionException();
		case 'j':
			if(s.contentEquals("jz"))
				return SquelInstrB1.JZ;
			if(s.contentEquals("jnz"))
				return SquelInstrB1.JNZ;
			if(s.contentEquals("jmp"))
				return SquelInstrB1.JMP;
			throw new NotInstructionException();
		case 'l':
			if(s.contentEquals("load"))
				return SquelInstrB1.LOAD;
			throw new NotInstructionException();
		case 'm':
			if(s.contentEquals("mul"))
				return SquelInstrB1.MUL;
			if(s.contentEquals("mod"))
				return SquelInstrB1.MOD;
			if(s.contentEquals("me"))
				return SquelInstrB1.ME;
			throw new NotInstructionException();
		case 'n':
			if(s.contentEquals("not"))
				return SquelInstrB1.NOT;
			if(s.contentEquals("noop"))
				return SquelInstrB1.NOOP;
			throw new NotInstructionException();
		case 'o':
			if(s.contentEquals("or"))
				return SquelInstrB1.OR;
			throw new NotInstructionException();
		case 'p':
			if(s.contentEquals("pop"))
				return SquelInstrB1.POP;
			if(s.contentEquals("push"))
				return SquelInstrB1.PUSH;
			if(s.contentEquals("pp"))
				return SquelInstrB1.PP;
			throw new NotInstructionException();
		case 'r':
			switch(s.trim().toLowerCase())
			{
			case "r0":
				return SquelInstrB1.R0;
			case "r1":
				return SquelInstrB1.R1;
			case "r2":
				return SquelInstrB1.R2;
			case "r3":
				return SquelInstrB1.R3;
			case "r4":
				return SquelInstrB1.R4;
			case "r5":
				return SquelInstrB1.R5;
			case "r6":
				return SquelInstrB1.R6;
			case "r7":
				return SquelInstrB1.R7;
			case "rba":
				return SquelInstrB1.RBA;
			case "rbc":
				return SquelInstrB1.RBC;
			case "rbd":
				return SquelInstrB1.RBD;
			case "rh":
				return SquelInstrB1.RH;
			case "ri":
				return SquelInstrB1.RI;
			case "ret":
				return SquelInstrB1.RET;
			case "rst":
				return SquelInstrB1.RST;
			case "rol":
				return SquelInstrB1.ROL;
			case "ror":
				return SquelInstrB1.ROR;
				default:
					throw new NotInstructionException();
			}
		case 's':
			if(s.contentEquals("save"))
				return SquelInstrB1.SAVE;
			if(s.contentEquals("set"))
				return SquelInstrB1.SET;
			if(s.contentEquals("sub"))
				return SquelInstrB1.SUB;
			if(s.contentEquals("shl"))
				return SquelInstrB1.SHL;
			if(s.contentEquals("shr"))
				return SquelInstrB1.SHR;
			throw new NotInstructionException();
		case 'x':
			if(s.contentEquals("xor"))
					return SquelInstrB1.XOR;
			throw new NotInstructionException();
			default:
				throw new NotInstructionException();
		}
	}
}
