package in;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

public class Reader {

	/**
	 * permet de recuperer le contenu d'un fichier
	 * @param f le chemin du fichier
	 * @return le contenu du fichier sous la forme d'une chaine
	 * @throws IOException
	 */
	public static String extract(File f) throws IOException
	{
		if(!f.exists())
			throw new FileNotFoundException();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		StringWriter out = new StringWriter();
		int b;
		while ((b=in.read()) != -1)
			out.write(b);
		out.flush();
		out.close();
		in.close();
		return out.toString();

	}
	
	/**
	 * découpe une chaine de caractère en tableau de chaine dont chaque élément correspond a une ligne
	 * @param s la chaine a découper
	 * @return un tableau de chaine
	 */
	public static String[] getLines(String s)
	{
		return s.split("\n");
	}
	
	/**
	 * retire les commentaires de la chaine de caractere
	 * dans cette fonction les commentaires commencent par # et ne font qu'une ligne
	 * @param s la chaine a nettoyer
	 * @param comm la chaine qui definit un commentaire
	 * @return la chaine sans commentaires
	 */
	public static String unComment(String s, String comm)
	{
		s = s.trim();
		if(!s.contains(comm))//si la chaine n'a pas de # elle n'a pas de commentaire
			return s;
		if(s.indexOf(comm) == 0)//si la chaine commence par # elle est un commentaire
			return "";
		return s.split(comm)[0];
	}
	
	/**
	 * retir toutes les chaines nulles ou vide d'un tableau de chaine
	 * @param s le tableau de chaine a nettoyer
	 * @return le tableau nettoyé
	 */
	public static String[] cleanTable(String[] s)
	{
		String[] res;
		int n = 0;
		for(int i = 0; i < s.length; i++)
		{
			if(s[i].isEmpty())
				continue;
			n++;
		}
		res = new String[n];
		n = 0;
		for(int i = 0; i < s.length; i++)
		{
			if(s[i].isEmpty())
			{
				n++;
				continue;
			}
			res[i - n] = s[i];
		}
		return res;
	}
}
