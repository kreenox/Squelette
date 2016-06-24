package squelette;

import component.processor.unit.AbsUAL;
import component.processor.unit.Registre;

public class UAL extends AbsUAL {

	//flags
		/**
		 * flag de débordement
		 */
		public static final int DEB = 0;//il y a une retenue
		/**
		 * flag de valeur negative
		 */
		public static final int NEG = 1;//la valeur dans sortie est negative
		/**
		 * flag de valeur zero
		 */
		public static final int ZERO = 2;//la valeur dans sortie est nulle
		
		private int mask = 0xFFFF;
		
	public UAL()
	{
		e1 = new Registre();
		e2 = new Registre();
		s = new Registre();
		instruction = 0x0;
		flags = new boolean[3];
		for(int n = 0; n < flags.length; n++)
			flags[n] = false;
	}
	@Override
	public void work() {

		int tempres;
		switch(instruction)
		{
		case SquelInstr.ADD:
			tempres = e1.read() + e2.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.SUB:
			tempres = e1.read() - e2.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.MUL:
			tempres = e1.read() * e2.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.DIV:
			tempres = e1.read() / e2.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.MOD:
			tempres = e1.read() % e2.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.AND:
			tempres = e1.read() & e2.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.OR:
			tempres = e1.read() | e2.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.XOR:
			tempres = ~(e1.read()) & e2.read() | e1.read() & ~(e2.read());
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.NOT:
			tempres = ~e1.read();
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.INC:
			tempres = e1.read() + 1;
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.DEC:
			tempres = e1.read() - 1;
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.ROL:
			tempres = e1.read() << 1;
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.ROR:
			tempres = e1.read() >>> 1;
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.SHL://a revoir
			tempres = e1.read() << 1;
			testflag(tempres);
			if(flags[DEB])
				tempres = tempres | 0x0001;
			s.write(tempres & mask);
			break;
		case SquelInstr.SHR://a revoir
			tempres = e1.read();
			if((tempres & 0x0001) != 0)
			{
				tempres = tempres >>> 1;
				tempres = tempres | 0x8000;
			}else tempres = tempres >>> 1;
			testflag(tempres);
			s.write(tempres & mask);
			break;
		case SquelInstr.CMP:
			tempres = e1.read() - e2.read();
			testflag(tempres);
			break;
			
			default:
				break;
		}

	}
	
	private void testflag(int val)
	{
		if(val == 0)
			flags[ZERO] = true;
		else flags[ZERO] = false;

		if((val & mask) != 0)
			flags[DEB] = true;
		else flags[DEB] = false;

		if(val < 0)
			flags[NEG] = true;
		else flags[NEG] = false;
	}

}
