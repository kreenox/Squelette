package divers;

/**
 * cette classe contient toutes les m�thodes pour effectuer des affichages sp�ciaux
 * @author Fabien Anxa
 * @version 0.1.0
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
	/**
	 * permet de r�cuperer une chaine pour afficher une valeur binaire
	 * @param val la valeure � afficher en binaire
	 * @param nbdg le nombre de digit � afficher
	 * @return une chaine pour afficher la valeur en binaire
	 */
	public static String binStringFromInt(int val, int nbdg)
	{
		String res = "0b";
		String temp = Integer.toBinaryString(val);
		while(temp.length() < nbdg)
			temp = "0" + temp;
		while(temp.length() > nbdg)
			temp = temp.substring(1);
		res = res + temp;
		return res;
	}
	/**
	 * permet de recuperer une chaine pour afficher une valeur octal
	 * @param val la valeur a afficher en octal
	 * @param nbdg le nobre de digit a afficher
	 * @return une chaine pour afficher la valeur en octal
	 */
	public static String octStringFromInt(int val, int nbdg)
	{
		String res = "0";
		String temp = Integer.toOctalString(val);
		while(temp.length() > nbdg)
			temp = "0" + temp;
		while(temp.length() < nbdg)
			temp = temp.substring(1);
		res = res + temp;
		return res;
	}
}
