package divers;

/**
 * cette classe contient les operations � effectuer sur les masques de bits
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.1.0
 *
 */

public abstract class MaskOperation {
	
	/**
	 * permet de construire un masque dont les <emph>size</emph> bits a droite sont � 1
	 * @param size le nombre de bits � 1
	 * @return le masque demand�
	 */
	public static int buildRigthFullMask(int size)
	{
		int res = 0x00000000;
		while(size > 0)
		{
			res = res << 1;
			res = res | 0x1;
		}
		return res;
	}
	/**
	 * permet de construire un masque dont les <emph>size</emph> bits a gauche sont � 1
	 * @param size le nombre de bits � 1
	 * @return le masque demand�
	 */
	public static int buildLeftFullMask(int size)
	{
		int res = 0x00000000;
		while(size > 0)
		{
			res = res >> 1;
			res = res | 0x80000000;
		}
		return res;
	}
}
