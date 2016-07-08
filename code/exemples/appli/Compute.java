package appli;

import java.io.FileNotFoundException;
import java.io.IOException;

import component.Clock;
import squelette.*;

public class Compute {
	
	public Clock mainck;
	
	public SqueletteB1 proc;
	public Bus b;
	public RAM ram;
	public PROM rom;
	
	public Compute(int ramsz, String rom)
	{
		mainck = new Clock(500);
		
		proc = new SqueletteB1();
		b = new Bus();
		ram = new RAM(ramsz);
		
		try {this.rom = new PROM(rom);} catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		
		mainck.connectTo(proc);
		mainck.connectTo(b);
		mainck.connectTo(ram);
		mainck.connectTo(this.rom);
		
		ram.connectTo(b);
		proc.connectTo(b);
		this.rom.connectTo(b);
		mainck.start();
		mainck.pause();
	}
	
	

}
