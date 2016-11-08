package squelette;

public class MemInstr {
	public static final int NOOP = 		0x0000;//ne fait rien
	public static final int WRITE = 	0b1000000000000000;//lecture si non utilisé
	public static final int PROC =		0b0100000000000000;//si utilisé on ne lit pas ROM et PERI
	public static final int PERI = 		0b0010000000000000;//si non utilisé c'est la memoire qui recupere
	public static final int ROM = 		0b0001000000000000;//si non utilisé c'est la ram qui est utilisé
	//le reste des bits sont utilisé pour le controle
}
