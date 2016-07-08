package saij;

import java.io.FileNotFoundException;

import saij.Opt.Procver;

//cette classe doit extraire les option du programme
public class Init {

	public static void main(String[] args) {
		extractOption(args);

	}
	
	private static void extractOption(String[] args)
	{
		for(int n = 0; n < args.length; n++)
		{
			Opt o = Opt.getInstance();
			switch(args[n])
			{
			case "-o":
				n++;
				o.setOutName(args[n]);
				break;
			case "-R":
				o.setRom(true);
				break;
			case "-B1":
				o.setProc(Procver.B1);
				break;
				default:
				try {
					o.addFileInput(args[n]);
				} catch (FileNotFoundException e) {
					System.err.println("le fichier : " + args[n] + " n'as pas été trouvé.\n l'application va s'arreter");
					System.exit(1);
				}
			}
		}
	}

}
