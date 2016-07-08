package squelette;

import component.processor.unit.AbsUAL;
import component.processor.unit.Registre;

public class UAL extends AbsUAL{

	//flags
		private boolean DEB = false;//il y a une retenue
		private boolean NEG = false;//la valeur dans sortie est negative
		private boolean ZERO = false;//la valeur dans sortie est nulle
		private boolean ERR = false;//le calcul contient une erreure
		
		private int mask = 0xFFFF;
		
	public UAL()
	{
		e1 = new Registre();
		e2 = new Registre();
		s = new Registre();
		instruction = 0x0;
		DEB = NEG = ZERO = false;
	}
	//set
	//get
	public boolean getDebordementFlag()
	{return DEB;}
	public boolean getErrCalcFlag()
	{return ERR;}
	public boolean getNegativeFlag()
	{return NEG;}
	public boolean getZeroFlag()
	{return ZERO;}
	//action
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
		if(e2.read() == 0){
			ERR = true;
			ZERO = NEG = DEB = false;
			s.write(0x0000);
			break;
		}
		tempres = e1.read() / e2.read();
		testflag(tempres);
		s.write(tempres & mask);
		break;
	case SquelInstr.MOD:
		if(e2.read() == 0x0000){
			ERR = true;
			ZERO = NEG = DEB = false;
			break;
		}
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
		if(DEB)
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
	//question
	//redefinition
	
	
	private void testflag(int val)
	{
		ERR = false;
		
		if(val == 0)
			ZERO = true;
		else ZERO = false;

		if((val & mask) != 0)
			DEB = true;
		else DEB = false;

		if(val < 0)
			NEG = true;
		else NEG = false;
	}

}
