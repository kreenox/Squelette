package instr.set;



public class SquelInstrB1 {

	//gestion du processeur
	public static final int NOOP = 0x0000;	//no opérations - ne fait rien
	public static final int CP = 0x0100;	//copy - copy un registre dans un autre
	public static final int HALT = 0x0F00;	//halt - effectue les opérations d'arre ou de gestion de l'energie
	public static final int RST = 0x0F10;	//reset - met la valeure d'un registre à 0
	public static final int BUS = 0x0F20;	//effectue un appel de bus
	
	
	//Saut
	public static final int JZ = 0x1000;	//jumpzero - effectue un saut si le registre ést égale à zero
	public static final int JNZ = 0x1100;	//jumpnonzero - effectue un saut si le registre n'est pas égale à 0
	public static final int JMP = 0x1F00; 	//jump - saut non coditionnel
	public static final int CALL = 0x1F10;	//call - appel une fonction
	public static final int RET = 0x1F20;	//return - retour d'une fonction
	public static final int INT = 0x1F30;	//interruption - effectue une interruption
	public static final int IRET = 0x1F40;	//interruptionreturn - retour d'interruption
	//memoire
	public static final int LOAD = 0x2000;	//load - charge dans un registre depuis la mémoire
	public static final int SAVE = 0x2100;	//save - sauvegarde un registre dans la mémoire
	public static final int PUSH = 0x2F00;	//push - met un registre sur la pile
	public static final int POP = 0x2F10;	//pop - recupère une valeur de la pile
	public static final int SET = 0x2F20;	//setvalue - met une valeure dans un registre
	//UAL
	//binaire
	public static final int ADD = 0x3000;	//addition - ajouute deux registre
	public static final int SUB = 0x3100;	//subtraction - soustrait deux registres
	public static final int MUL = 0x3200;	//multiplication - multiplie deux registres
	public static final int DIV = 0x3300;	//division - divise deux registres
	public static final int MOD = 0x3400;	//modulo - rest d'une division
	public static final int OR = 0x3500;	//ou logique bit à bit
	public static final int AND = 0x3600;	//et logique bit à bit
	public static final int XOR = 0x3700;	//ouexclusif - ou exclusif bit à bit
	public static final int CMP = 0x3800;	//comparaison - effectue une soustraction sans résultat mais effectue une mise a jour du mot d'état quand meme
	
	//unaire

	public static final int NOT = 0x3F00;	//non - effectue un non bit à bit
	public static final int INC = 0x3F10;	//incrementation - incrémente un registre
	public static final int DEC = 0x3F20;	//decrementation - decremente un registre
	public static final int ROL = 0x3F30;	//rotationleft - rotation de bit vers la gauche
	public static final int ROR = 0x3F40;	//rotationrigth - rotation de bit vers la droite
	public static final int SHL = 0x3F50;	//shiftleft - deplacement de bit vers la gauche
	public static final int SHR = 0x3F60;	//shiftrigth - deplacement de bit vers la droite
	//trinaire
	
	//registres
	//registres généreaux(R0-R7)
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
