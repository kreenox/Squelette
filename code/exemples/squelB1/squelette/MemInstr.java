package squelette;

public class MemInstr {
	public static final int NOOP = 0x0000;//ne fait rien
	public static final int READ = 0x1000;//0x1nAD lit n emplacement et les envoi a ADresse 
	public static final int WRITE = 0x2000;//0x2000 ecrit dans un emplacement
	public static final int RESET = 0x3000;//0x3n00 remet a zero n emplacements
	public static final int SENDALL = 0x4000;//0x40AD envoi tout le contenu de la memeoire a ADresse
}
