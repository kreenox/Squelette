package component.processor;

public class SquelInstr {

	//gestion du processeur
	public static final int NOOP = 0x0000;
	public static final int CP = 0x0100;
	public static final int HALT = 0x0F00;
	public static final int RST = 0x0F10;
	public static final int BUS = 0x0F20;
	
	
	//Saut
	public static final int JZ = 0x1000;
	public static final int JNZ = 0x1100;
	public static final int JMP = 0x1F00; 
	public static final int CALL = 0x1F10;
	public static final int RET = 0x1F20;
	public static final int INT = 0x1F30;
	public static final int IRET = 0x1F40;
	//memoire
	public static final int LOAD = 0x2000;
	public static final int SAVE = 0x2100;
	public static final int PUSH = 0x2F00;
	public static final int POP = 0x2F10;
	public static final int SET = 0x2F30;
	//UAL
	//binaire
	public static final int ADD = 0x3000;
	public static final int SUB = 0x3100;
	public static final int MUL = 0x3200;
	public static final int DIV = 0x3300;
	public static final int MOD = 0x3400;
	public static final int OR = 0x3500;
	public static final int AND = 0x3600;
	public static final int XOR = 0x3700;
	public static final int RORL = 0x3800;
	public static final int RORR = 0x3900;
	public static final int ROLL = 0x3A00;
	public static final int ROLR = 0x3B00;
	public static final int EQ = 0x3C00;
	public static final int GT = 0x3D00;
	public static final int LT = 0x3E00;
	
	//unaire

	public static final int NOT = 0x3F00;
	public static final int INC = 0x3F10;
	public static final int DEC = 0x3F20;
	//trinaire
	
	//registres
	public static final int R0 = 0x0000;
	public static final int R1 = 0x0000;
	public static final int R2 = 0x0000;
	public static final int R3 = 0x0000;
	public static final int R4 = 0x0000;
	public static final int R5 = 0x0000;
	public static final int R6 = 0x0000;
	public static final int R7 = 0x0000;
	public static final int RBA = 0x0000;//bus d'adresse
	public static final int RBD = 0x0000;//bus de donn�es
	public static final int RBC = 0x0000;//bus de controle
	public static final int RH = 0x0000;//registre d'horloge
	public static final int PP = 0x0000;//Pointeur de pile
	public static final int RI = 0x0000;//registre d'intruction
	public static final int ME = 0x0000;//mot d'�tat
	public static final int CO = 0x0000;//compteur ordinal
	
}
