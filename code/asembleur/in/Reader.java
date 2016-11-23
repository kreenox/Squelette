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
}
