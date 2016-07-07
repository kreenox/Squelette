package component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import javax.swing.JPanel; a remettre avec getUI


/**
 * cette classe sert à génerer des ticks d'horloge à interval réguliers.
 * a chaque tick elle appelle toutes les méthodes <i>work()</i> des composant auquels elle est connecté
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class Clock implements Clockable 
{
	
	private int ticksize;
	private long lasttick;
	private boolean enslaved;
	private boolean paused;
	private ArrayList<Clockable> connected;
	private Thread t;
	
	//pour l'ui
	private JPanel observed = null;
	
	/**
	 * créé une hologe
	 */
	public Clock()
	{
		ticksize = 0;
		connected = new ArrayList<Clockable>();
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
		connected = new ArrayList<Clockable>();
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
	//a décommenter plus tard
	public JPanel getUI() {
		
		class Panel extends JPanel implements Observer{
			private static final long serialVersionUID = 2447946466878582673L;
			private JSlider slid;
			private JTextField val;
			private JButton pau;
			private JButton man;
			
			public Panel(){//a revoir
				super();
				this.setMaximumSize(new Dimension(9999999, 50));
				this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
				slid = new JSlider(SwingConstants.HORIZONTAL, 100, 2000, 500);
				slid.setMaximumSize(new Dimension(450, 30));
				slid.setMinimumSize(new Dimension(250, 30));
				val = new JTextField("500");
				val.setMaximumSize(new Dimension(100, 30));
				val.setMinimumSize(new Dimension(75, 30));
				pau = new JButton("Pause");
				man = new JButton("Tick Manuel");
				this.add(new JLabel("Horloge"));
				this.add(slid);
				this.add(val);
				this.add(pau);
				this.add(man);
				slid.addChangeListener(new ChangeListener(){
					public void stateChanged(ChangeEvent arg0) {
						int temp = slid.getValue();
						if(temp != 2000)
							val.setText("" + temp);
						else val.setText("manuel");
					}});
				val.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						int temp = Integer.parseInt(val.getText());
						if(temp > 2000)
							temp = 2000;
						if(temp < 100)
							temp = 100;
						ticksize = temp;
						slid.setValue(temp);
					}});
				pau.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						if(paused)
						{
							pau.setText("pause");
							unpause();
						}else{
							pau.setText("lancer");
							pause();
						}
					}
					
				});
				man.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(true)//mettre la condition
							tick();
					}
					
				});
			}

			public void update(Observable arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
			
		}
		if(observed == null)
		{
			Panel pane = new Panel();
			observed = pane;
		}
		return observed;
		
	}
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
	 * permet de connecter un objet à cette horloge.
	 * l'horloge appelle la méthode <i>work()</i> de tout le object auxquel elle est connectée
	 * @param c l'objet à connecter
	 */
	public void connectTo(Clockable c) {
		if(connected.contains(c))
			;//throw
		connected.add(c);
	}
	/**
	 * permet de déconnecter tous les objet de cette horloge
	 */
	public void disconnect() {
		connected.clear();
		
	}
	/**
	 * permet de deconnecter un objet de l'horloge
	 * @param c l'objet à déconnecter
	 * @throws NonConnectedException est envoyé si l'objet n'est pas encor connecté à l'horloge
	 */
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		if(!connected.contains(c))
			throw new NonConnectedException();
		connected.remove(c);
		
	}
	/**
	 * synchronize les ticks d'une horloge sur ceux de cette horloge 
	 * @param c l'horloge a synchroniser
	 */
	public void enslave(Clock c)
	{
		c.setEnslaved(true);
		this.connected.add(c);
	}
	/**
	 * met en marche l'horloge
	 */
	public void start()
	{
		t = new Thread(){
			public void run(){
				work();
			}
		};
		t.start();
	}
	/**
	 * appelle les methodes work de tout les composants connectés à l'horloge
	 */
	public void tick()
	{
		System.out.println("tick emmited");
		Iterator<Clockable> it = connected.iterator();
		while(it.hasNext())
			it.next().work();
	}
	/**
	 * l'horloge ne synchronise plus les ticks d'une autre horloge
	 * @param c l'horloge à libérer
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
	/**
	 * permet de savoir si l'horloge est en pause
	 * @return true si elle l'est, false sinon
	 */
	public boolean isPaused()
	{return paused;}
	//redefinition
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