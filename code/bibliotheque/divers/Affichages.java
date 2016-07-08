package divers;

/**
 * cette classe contient toutes les m�thodes pour effectuer des affichages sp�ciaux
 * @author Fabien Anxa
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public abstract class Affichages {

	/**
	 * permet de r�cuperer une chaine pour afficher une valeure hexadecimale
	 * @param val la valeure a afficher en hexadecimal
	 * @param nbdg le nombre de digits a afficher
	 * @return une chaine pour afficher la valeur en hexad�cimal
	 */
	public static String hexStringFromInt(int val, int nbdg)
	{
		String res = "0x";
		String temp = Integer.toHexString(val);
		while(temp.length() < nbdg)
			temp = "0" + temp;
		while(temp.length() > nbdg)
			temp = temp.substring(1);
		res = res + temp.toUpperCase();
		return res;
	}
}
