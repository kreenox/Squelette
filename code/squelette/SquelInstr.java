package squelette;

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
	public static final int CMP = 0x3800;
	
	//unaire

	public static final int NOT = 0x3F00;
	public static final int INC = 0x3F10;
	public static final int DEC = 0x3F20;
	public static final int ROL = 0x3F30;
	public static final int ROR = 0x3F40;
	public static final int SHL = 0x3F50;
	public static final int SHR = 0x3F60;
	//trinaire
	
	//registres
	public static final int R0 = 0x0;
	public static final int R1 = 0x1;
	public static final int R2 = 0x2;
	public static final int R3 = 0x3;
	public static final int R4 = 0x4;
	public static final int R5 = 0x5;
	public static final int R6 = 0x6;
	public static final int R7 = 0x7;
	public static final int RBA = 0x8;//bus d'adresse
	public static final int RBD = 0x9;//bus de données
	public static final int RBC = 0xA;//bus de controle
	public static final int RH = 0xB;//registre d'horloge
	public static final int PP = 0xC;//Pointeur de pile
	public static final int RI = 0xD;//registre d'intruction
	public static final int ME = 0xE;//mot d'état
	public static final int CO = 0xF;//compteur ordinal
	
}
