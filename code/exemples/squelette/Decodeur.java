package squelette;

import component.processor.unit.AbsDecodeur;

public class Decodeur extends AbsDecodeur{
	private int instruction;
	private int r1;
	private int r2;
	
	public Decodeur()
	{instruction = r1 = r2 = 0x0000;}
	//set
	//get
	public int getInstruction()
	{return instruction;}
	public int getR1()
	{return r1;}
	public int getR2()
	{return r2;}
	//action
	public void decode(int instr)
	{
		if((instr & 0x0F00) == 0x0F00)
		{
			instruction = instr & 0xFFF0;
			r1 = instr & 0x000F;
		}
		else
		{
			instruction = instr & 0xFF00;
			r1 = (instr & 0x00F0) >> 4;
			r2 = instr & 0x000F;
		}
		
	}
	//question
	//redefinition
}
