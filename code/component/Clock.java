package component;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;


/**
 * cette classe sert à génerer des ticks d'horloge à interval réguliers.
 * <br/>
 * a chaque tick elle appelle toutes les méthodes <i>work()</i> des composant auquels elle est connecté
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class Clock extends AbsComponent
{
	
	private int ticksize;
	private long lasttick;
	private boolean enslaved;
	private boolean paused;
	
	/**
	 * créé une hologe
	 */
	public Clock()
	{
		ticksize = 0;
		connected = new ArrayList<AbsComponent>();
		enslaved = false;
	}
	/**
	 * céé une horloge cadencée
	 * @param ticks la taille d'un tick en ms
	 */
	public Clock(int ticks)
	{
		if(ticks < 0)
			throw new InvalidParameterException();
		ticksize = ticks;
		connected = new ArrayList<AbsComponent>();
		enslaved = false;
	}
	//set
	/**
	 * change le temps entre deux ticks
	 * @param val la taille d'un tick  en ms
	 */
	public void setTickSize(int val)
	{
		if(val >=0)
			ticksize = val;
		else throw new InvalidParameterException();
	}
	//get
	/**
	 * permet de récupérer la taille d'un tick
	 * @return la taille d'un tick en ms
	 */
	public int getTickSize()
	{return ticksize;}
	//action
	/**
	 * met imediatement l'horloge en pause, elle n'emmet pas de tick
	 */
	public void pause()
	{paused = true;}
	/**
	 * relance les ticks d'horloges, il peut y avoir du délais entre l'appel et le prochain tick
	 */
	public void unpause()
	{paused = false;}
	/**
	 * synchronize les ticks d'une horloge sur ceux de cette horloge 
	 */
	public void enslave(Clock c)
	{
		c.setEnslaved(true);
		this.connected.add(c);
	}
	public void start()
	{work();}
	/**
	 * appelle les methodes work de tout les composants connectés à l'horloge
	 */
	public void tick()
	{
		Iterator<AbsComponent> it = connected.iterator();
		while(it.hasNext())
			it.next().work();
	}
	/**
	 * l'horloge ne synchronise plus les ticks d'une autre horloge
	 * @throws NonConnectedException si l'horloge n'est pas synchronisée sur celle-ci
	 */
	public void unslave(Clock c) throws NonConnectedException
	{
		Clock temp;
		if(connected.contains(c))
		{
			temp = (Clock)connected.get(connected.indexOf(c));
			connected.remove(c);
			temp.enslaved = false;
		}
		else throw new NonConnectedException();
	}
	//question
	//redefinition
	@Override
	public void connectTo(AbsComponent c) {
		if(connected.contains(c))
			;//throw
		connected.add(c);
	}
	@Override
	public void disconnect() {
		connected.clear();
		
	}
	@Override
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		if(!connected.contains(c))
			throw new NonConnectedException();
		connected.remove(c);
		
	}
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(this.getClass().equals(o.getClass()))
		{
			Clock c = (Clock)o;
			if(c.connected.equals(this.connected))
				if(c.enslaved == this.enslaved)
					if(c.paused == this.paused)
						if(c.ticksize == this.ticksize)
							return true;
		}
		return false;
	}
	@Override
	public JPanel getUI() {
		return null;
		
	}
	@Override
	public void work() {//normalement c'est bon
		if(!enslaved)//si l'horloge n'est pas gérée par l'exterieur
		{
			while(!enslaved)// tant qu'elle n'est pas gérée de l'exterieur
			{
				if(paused)//on verifie toute les seconde si l'horloge est en pause 
					synchronized(this){try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}}
				else //on effectue un tick tout les ticksize ms
				{
					if((lasttick + ticksize) <= System.currentTimeMillis())
					{
						lasttick = System.currentTimeMillis();
						tick();
					}
				}
			}
		}
		else tick();
	}
	//private
	
	private void setEnslaved(boolean b)//cette horloge est synchronisée avec les ticks d'une autre horloge
	{enslaved = b;}
}