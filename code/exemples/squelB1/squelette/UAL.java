package squelette;

import component.processor.unit.AbsUAL;
import component.processor.unit.Registre;

public class UAL extends AbsUAL {

	private Registre mot;
	
	public UAL(Registre me)
	{
		mot = me;
		e1 = e2 = s = null;
	}
	
	public void work()
	{

		int temp;
		boolean bit;
		lowerErrCalc();
		switch(instruction)
		{
		//operations binaires
		case SquelInstr.ADD:
			s.write(buildEtat(e1.read() + e2.read()));
			break;
		case SquelInstr.SUB:
			s.write(buildEtat(e1.read() - e2.read()));
			break;
		case SquelInstr.MUL:
			s.write(buildEtat(e1.read() * e2.read()));
			break;
		case SquelInstr.DIV:
			if(e2.read() == 0)
			{
				riseErrCalc();
			}
			s.write(buildEtat(e1.read() / e2.read()));
			break;
		case SquelInstr.MOD:
			s.write(buildEtat(e1.read() % e2.read()));
			break;
		case SquelInstr.OR:
			s.write(buildEtat(e1.read() | e2.read()));
			break;
		case SquelInstr.AND:
			s.write(buildEtat(e1.read() & e2.read()));
			break;
		case SquelInstr.XOR:
			s.write(buildEtat(e1.read() ^ e2.read()));
			break;
		case SquelInstr.CMP:
			buildEtat(e1.read() - e2.read());
			break;
		//unaires
		case SquelInstr.NOT:
			s.write(buildEtat(~e1.read()));
			break;
		case SquelInstr.INC:
			s.write(buildEtat(e1.read() + 1));
			break;
		case SquelInstr.DEC:
			s.write(buildEtat(e1.read() - 1));
			break;
		case SquelInstr.ROL:
			s.write(buildEtat(e1.read() << 1));
			break;
		case SquelInstr.ROR:
			s.write(buildEtat(e1.read() >> 1));
			break;
		case SquelInstr.SHL:
			temp = e1.read();
			if((temp & 0x8000) != 0)
				bit = true;
			else bit = false;
			temp = temp << 1;
			if(bit)
				temp = temp | 0x8000;
			s.write(buildEtat(temp));
			break;
		case SquelInstr.SHR:
			temp = e1.read();
			if((temp & 0x0001) != 0)
				bit = true;
			else bit = false;
			temp = temp >> 1;
			if(bit)
				temp = temp | 0x0001;
			s.write(buildEtat(temp));
			break;
			default:
				break;
		}
	}
	
	private void riseErrCalc()
	{mot.write(mot.read() | 0x0008);}
	
	private void lowerErrCalc()
	{mot.write(mot.read() & 0xFFF7);}
	
	private int buildEtat(int val)
	{
		//zero flag
		if(val == 0)
			mot.write(mot.read() | 0x0001);
		else mot.write(mot.read() & 0xFFFE);
		//negative flag
		if(val < 0)
			mot.write(mot.read() | 0x0002);
		else mot.write(mot.read() & 0xFFFD);
		//debordement flag
		if((val & 0xFFFF0000) != 0)
			mot.write(mot.read() | 0x0004);
		else mot.write(mot.read() & 0xFFFB);
		
		return (val & 0xFFFF);
	}
}
