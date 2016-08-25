package squelette;

import component.processor.unit.AbsDecodeur;

public class Decodeur extends AbsDecodeur {
	
	private int instr;
	private int arg1;
	private int arg2;
	
	public Decodeur()
	{instr = arg1 = arg2 = 0;}
	
	public int getInstr()
	{return instr;}
	public int getArg1()
	{return arg1;}
	public int getArg2()
	{return arg2;}
	@Override
	public void decode(int instruction) {
		if((instruction & 0x0F00) == 0x0F00)
		{
			instr = instruction & 0xFFF0;
			arg1 = instruction & 0x000F;
			arg2 = 0;
		}
		else
		{
			instr = instruction & 0xFF00;
			arg1 = instruction & 0x00F0;
			arg2 = instruction & 0x000F;
		}

	}

}
