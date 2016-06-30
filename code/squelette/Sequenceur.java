package squelette;

import component.memory.AbsMemory;
import component.processor.unit.Registre;

public class Sequenceur {
	
	private boolean wait = false;
	private int state = 0;

	public void work(Bus b, Registre[] tab, Registre intmask, UAL u, Decodeur d)
	{
		if(!wait)
		{
			if(state == 0)//appel du bus pour recuperation de l'instruction
			{
				wait = true;
				b.call(0x0010, tab[SquelInstr.CO].read(), AbsMemory.READ);
				state |= 0x0001;
			}
			if((state & 0x0001) != 0x0001)//recuperation de l'instruction dans le bus  
			{
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC)
				{
					tab[SquelInstr.RI].write(b.getTransmitedData());
					d.decode(tab[SquelInstr.RI].read());//decoder l'instruction
					//executer l'instruction
					if((d.getInstruction() & 0xF000) == 0x3000)//si c'est une instruction de l'ual
					{
						u.e1.write(tab[d.getR1()].read());
						u.e2.write(tab[d.getR2()].read());
						u.work();
						tab[d.getR1()].write(u.s.read());
						//mise a jour du mot d'état
						int resflag = 0x0000;
						if(u.getZeroFlag())
							resflag |= 0x0001;
						if(u.getNegativeFlag())
							resflag |= 0x0002;
						if(u.getDebordementFlag())
							resflag |= 0x0004;
						tab[SquelInstr.ME].write((tab[SquelInstr.ME].read() & 0xFFF8) | resflag);
						incRegistre(tab[SquelInstr.CO]);//on augmente le compteur ordinal
					}
					else
					{
						switch(d.getInstruction())
						{
						case SquelInstr.NOOP:
							incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.BUS:
							b.call(tab[SquelInstr.RBA].read(), tab[SquelInstr.RBD].read(), tab[SquelInstr.RBC].read());
							incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.HALT://arret de l'horloge principale
							break;
						case SquelInstr.CP:
							tab[d.getR1()].write(tab[d.getR2()].read());
							incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.RST:
							tab[d.getR1()].reset();
							incRegistre(tab[SquelInstr.CO]);
							break;
						//instructions de saut
						case SquelInstr.JZ:
							if(tab[d.getR1()].read() == 0)
								tab[SquelInstr.CO].write(tab[d.getR2()].read());
							else incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.JNZ:
							if(tab[d.getR1()].read() != 0)
								tab[SquelInstr.CO].write(tab[d.getR2()].read());
							else incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.JMP:
							tab[SquelInstr.CO].write(tab[d.getR1()].read());
							break;
						case SquelInstr.CALL://pousse CO dans la pile puis saut inconditionel
							break;
						case SquelInstr.RET://prend le dessus de la pile et le met dans CO
							break;
						case SquelInstr.IRET://prend dans la pile toutes les valeures des registres
							break;
						case SquelInstr.INT://pousse tout les registresdans la pile puis saut
							break;
						//gestion de la memoire
						case SquelInstr.LOAD:
							incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.SAVE:
							incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.PUSH:
							b.call(SquelAdr.RAM, tab[d.getR1()].read(), AbsMemory.READ);
							incRegistre(tab[SquelInstr.PP]);
							incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.POP:
							incRegistre(tab[SquelInstr.CO]);
							break;
						case SquelInstr.SET:
							incRegistre(tab[SquelInstr.CO]);
							incRegistre(tab[SquelInstr.CO]);
							break;
							default:
								incRegistre(tab[SquelInstr.CO]);
								break;
						}
					}
				}
					
			}
		}
		
	}
	
	private void incRegistre(Registre r)
		{
			r.write(r.read() + 1);
		}
}
