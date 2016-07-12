package squelette;

import component.memory.AbsMemory;
import component.processor.unit.Registre;

public class Sequenceur{

	private int state = 0;
	private final int ATT = 0x0000;
	private final int FETCH = 0x0001;
	private final int ACT = 0x0002;
	private final int SECACT = 0x0004;
	private final int TRIACT = 0x0008;
	private final int QUAACT = 0x0010;

	public void work(Bus b, Registre[] tab, Registre intmask, UAL u, Decodeur d)
	{
		if(state == ATT && !b.isUsed())//appel du bus pour recuperation de l'instruction
		{
			b.call(SquelAdr.RAM, tab[SquelInstr.CO].read(), AbsMemory.READ);
			state = FETCH;
		}
		else if((state & FETCH) == FETCH && b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC)//recuperation de l'instruction dans le bus
		{
			tab[SquelInstr.RI].write(b.getTransmitedData());
			state = ACT; 
		}
		if((state & ACT) == ACT)  
		{


			d.decode(tab[SquelInstr.RI].read());//decoder l'instruction
			//executer l'instruction
			if((d.getInstruction() & 0xF000) == 0x3000)//si c'est une instruction de l'ual
			{
				u.e1.write(tab[d.getR1()].read());
				u.e2.write(tab[d.getR2()].read());
				u.instruction = d.getInstruction();
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
				if(u.getErrCalcFlag())
					resflag |= 0x0008;
				tab[SquelInstr.ME].write((tab[SquelInstr.ME].read() & 0xFFF0) | resflag);
				incRegistre(tab[SquelInstr.CO]);//on augmente le compteur ordinal
				state = ATT;
			}
			else
			{
				switch(d.getInstruction())
				{
				case SquelInstr.NOOP:
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
					break;
				case SquelInstr.BUS:
					if(!b.isUsed()){
						b.call(tab[SquelInstr.RBA].read(), tab[SquelInstr.RBD].read(), tab[SquelInstr.RBC].read());
						incRegistre(tab[SquelInstr.CO]);
						state = ATT;
					}
					break;
				case SquelInstr.HALT://arret de l'horloge principale
					break;
				case SquelInstr.CP:
					tab[d.getR1()].write(tab[d.getR2()].read());
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
					break;
				case SquelInstr.RST:
					tab[d.getR1()].reset();
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
					break;
					//instructions de saut
				case SquelInstr.JZ:
					if(tab[d.getR1()].read() == 0)
						tab[SquelInstr.CO].write(tab[d.getR2()].read());
					else incRegistre(tab[SquelInstr.CO]);
					state = ATT;
					break;
				case SquelInstr.JNZ:
					if(tab[d.getR1()].read() != 0)
						tab[SquelInstr.CO].write(tab[d.getR2()].read());
					else incRegistre(tab[SquelInstr.CO]);
					state = ATT;
					break;
				case SquelInstr.JMP:
					tab[SquelInstr.CO].write(tab[d.getR1()].read());
					state = ATT;
					break;
				case SquelInstr.CALL://pousse CO dans la pile puis saut inconditionel
					if(!b.isUsed()){
						b.call(SquelAdr.RAM, tab[SquelInstr.PP].read(), AbsMemory.WRITE);
						state = SECACT;
					}
					break;
				case SquelInstr.RET://prend le dessus de la pile et le met dans CO
					if(!b.isUsed()){
						decRegistre(tab[SquelInstr.PP]);
						b.call(SquelAdr.RAM, tab[SquelInstr.PP].read(), AbsMemory.READ);
						state = SECACT;
					}
					break;
				case SquelInstr.IRET://prend CO dans la pile puis le met dans le bon registre
					if(!b.isUsed())
					{
						decRegistre(tab[SquelInstr.PP]);
						b.call(SquelAdr.RAM, tab[SquelInstr.PP].read(), AbsMemory.READ);
						state = SECACT;
					}
					break;
				case SquelInstr.INT://pousse CO dans la pile puis met le traitant dans CO 
					if(!b.isUsed()){
						b.call(SquelAdr.RAM, tab[SquelInstr.PP].read(), AbsMemory.WRITE);
						state = SECACT;
					}
					break;
					//gestion de la memoire
				case SquelInstr.LOAD:
					if(!b.isUsed()){
						b.call(SquelAdr.RAM, tab[d.getR2()].read(), AbsMemory.READ);
						state = SECACT;
					}
					break;
				case SquelInstr.SAVE:
					if(!b.isUsed()){
						b.call(SquelAdr.RAM, tab[d.getR2()].read(), AbsMemory.WRITE);
						state = SECACT;
					}
					break;
				case SquelInstr.PUSH:
					if(!b.isUsed()){
						b.call(SquelAdr.RAM, tab[SquelInstr.PP].read(), AbsMemory.WRITE);
						state = SECACT;
					}
					break;
				case SquelInstr.POP:
					if(!b.isUsed()){
						b.call(SquelAdr.RAM, tab[SquelInstr.PP].read(), AbsMemory.READ);
						state = SECACT;
					}
					break;
				case SquelInstr.SET:
					if(!b.isUsed()){
						incRegistre(tab[SquelInstr.CO]);
						b.call(SquelAdr.RAM, tab[SquelInstr.CO].read(), AbsMemory.READ);
						state = SECACT;
					}
					break;
				default:
					incRegistre(tab[SquelInstr.CO]);
					break;
				}
			}
		}else if((state & SECACT) == SECACT){//action après premier appel de bus
			switch(d.getInstruction()){
			case SquelInstr.CALL://pousse CO dans la pile puis saut inconditionel
				if(!b.isUsed()){
					b.call(SquelAdr.RAM, tab[SquelInstr.CO].read(), AbsMemory.WRITE);
					tab[SquelInstr.CO].write(tab[d.getR1()].read());
					incRegistre(tab[SquelInstr.PP]);
					state = ATT;
				}
				break;
			case SquelInstr.RET://prend le dessus de la pile et le met dans CO
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC)
				{
					tab[SquelInstr.CO].write(b.getTransmitedData());
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
				}
				break;
			case SquelInstr.INT:
				if(!b.isUsed())
				{
					b.call(SquelAdr.RAM, tab[SquelInstr.CO].read(), AbsMemory.WRITE);
					incRegistre(tab[SquelInstr.PP]);
					state = TRIACT;
				}
				break;
			case SquelInstr.IRET:
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC)
				{
					tab[SquelInstr.CO].write(b.getTransmitedData());
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
				}
				break;
			case SquelInstr.LOAD:
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC)
				{
					tab[d.getR1()].write(b.getTransmitedData());
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
				}
				break;
			case SquelInstr.SAVE:
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC){
					b.call(SquelAdr.RAM, tab[d.getR1()].read(), AbsMemory.WRITE);
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
				}
				break;
			case SquelInstr.PUSH:
				if(!b.isUsed()){
					b.call(SquelAdr.RAM, tab[d.getR1()].read(), AbsMemory.WRITE);
					incRegistre(tab[SquelInstr.PP]);
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
				}
				break;
			case SquelInstr.POP:
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC){
					tab[d.getR1()].write(b.getTransmitedData());
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
				}
				break;
			case SquelInstr.SET:
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC){
					tab[d.getR1()].write(b.getTransmitedData());
					incRegistre(tab[SquelInstr.CO]);
					state = ATT;
				}
				break;
			}
		}else if((state & TRIACT) == TRIACT){
			switch(d.getInstruction())
			{
			case SquelInstr.INT:
				if(!b.isUsed())
				{
					b.call(SquelAdr.RAM, (d.getR1() | 0x0010), AbsMemory.READ);
					state = QUAACT;
				}
				break;
				default:
					break;
			}
		}else if((state & QUAACT) == QUAACT){
			switch(d.getInstruction())
			{
			case SquelInstr.INT:
				if(b.isTransmiting() && b.getTransmitedAdresse() == SquelAdr.PROC)
				{
					tab[SquelInstr.CO].write(b.getTransmitedData());
					state = ATT;
				}
				break;
				default:
					break;
			}
		}
		
		
	}

	private void incRegistre(Registre r)
	{
		r.write(r.read() + 1);
	}
	private void decRegistre(Registre r)
	{
		r.write(r.read() - 1);
	}
	/*private int switchbit(int ori, int mask, boolean val)
	{
		int res = ori;
		if(val)//on doit mettre à vrai
			ori |= mask;
		else//on doit mettre à faut
			ori &= ~mask;
		return res;
	}*/

	//redefinitions
	
}
