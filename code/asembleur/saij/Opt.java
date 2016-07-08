package saij;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;


//contient les options de l'application
public class Opt {
	public enum Procver{B1}
	private ArrayList<File> infilelist;
	private String outname;
	private Procver verproc;
	private boolean rom;
	private static Opt inst = null;
	
	private Opt()
	{
		infilelist = new ArrayList<File>();
		outname = "";
		verproc = Procver.B1;
		rom = false;
	}
	
	public static Opt getInstance(){
		if(inst == null)
			inst = new Opt();
		return inst;
	}
	
	//set
	public void setOutName(String s)
	{outname = s;}
	public void setRom(boolean b)
	{rom = b;}
	public void setProc(Procver ver)
	{verproc = ver;}
	//get
	public String getOutName()
	{return outname;}
	public boolean getRom()
	{return rom;}
	public Procver getVerProc()
	{return verproc;}
	public Iterator<File> getInFiles()
	{return infilelist.iterator();}
	//action
	public void addFileInput(String path) throws FileNotFoundException
	{
		File temp = new File(path);
		if(!temp.exists())
			throw new FileNotFoundException();
		infilelist.add(temp);
	}
	//question
	//redefinition
}

