package in;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

public class Reader {

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
	
	public static String[] getLines(String s)
	{
		return s.split("\n");
	}
}
