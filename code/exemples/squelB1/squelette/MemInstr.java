package squelette;

public class MemInstr {
	public static final int NOOP = 		0x0000;//ne fait rien
	public static final int WRITE = 	0b1000000000000000;//lecture si non utilis�
	public static final int PROC =		0b0100000000000000;//si utilis� on ne lit pas ROM et PERI
	public static final int PERI = 		0b0010000000000000;//si non utilis� c'est la memoire qui recupere
	public static final int ROM = 		0b0001000000000000;//si non utilis� c'est la ram qui est utilis�
	//le reste des bits sont utilis� pour le controle
}
