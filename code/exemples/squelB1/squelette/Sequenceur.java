package squelette;


public class Sequenceur {
	
	private enum State{OFF, //processeur eteint
		FREE, //processeur en attente d'action
		INIT, //processeur en initialisation
		INT,  //processeur interrompu
		WORK  //processeur en travail
		}
	
	private State phase;
	private int sousphase;
	private boolean fetching;//le fetching c'est l'operation de recherche de donnée sur le bus
	
	public Sequenceur(){
		phase = State.OFF;
		fetching = false;
		sousphase = 0;
	}
	
	//set
	public void setFetching(boolean b)
	{fetching = b;}
	//get
	public int getSousPhase()
	{return sousphase;}
	//action
	public void SousPhaseSuiv()
	{sousphase++;}
	public void shutdown()
	{phase = State.OFF;}
	public void wakeUp()
	{
		if(phase == State.OFF)
		{
			phase = State.INIT;
			sousphase = 0;
		}
	}
	public void initEnd()
	{
		if(phase == State.INIT)
		{
			phase = State.FREE;
			sousphase = 0;
		}
	}
	public void startWork()
	{
		if(phase == State.FREE)
		{
			phase = State.WORK;
			sousphase = 0;
		}
	}
	public void workend()
	{
		if(phase == State.WORK)
		{
			phase = State.FREE;
			sousphase = 0;
		}
	}
	public void interupted()
	{
		if(phase == State.FREE)
		{
			phase = State.INT;
			sousphase = 0;
		}
	}
	public void intEnt()
	{
		if(phase == State.INT)
		{
			phase = State.FREE;
			sousphase = 0;
		}
	}
	//question
	public boolean isInerupted()
	{return phase == State.INT;}
	public boolean initiating()
	{return phase == State.INIT;}
	public boolean isFree()
	{return phase == State.FREE;}
	public boolean isWorking()
	{return phase == State.WORK;}
	public boolean isFetching()
	{return fetching;}
	//redefiniton
	
	
}
