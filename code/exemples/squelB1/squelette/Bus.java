package squelette;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import component.bus.AbsBus;

public class Bus extends AbsBus {

	private int mask;
	private int masksize;
	private BusType type;
	
	private JPanel observed = null;
	
	public enum BusType{ADR, DATA, CONT}
	
	public Bus()
	{
		super();
		masksize = 32;
		mask = 0xFFFFFFFF;
	}
	public Bus(int nblignes, BusType t){
		super();
		masksize = nblignes;
		mask = divers.MaskOperation.buildRigthFullMask(masksize);
		type = t;
	}
	//set
	//get
	public BusType getBusType()
	{return type;}
	//action
	//question
	//redefinition
	
	@Override
	public void work() {
		this.setChanged();
		this.notifyObservers();
		if(this.isTransmiting())
			reset();
		if(this.isUsed() && !this.isTransmiting())
			transmit();

	}

	@Override
	public void call(int data)
	{
		super.call(data & mask);
	}
	@Override
	public JPanel getUI() {

		class Panel extends JPanel implements Observer{

			private static final long serialVersionUID = 1L;
			private JLabel etat;

			Panel()
			{
				this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				this.add(new JLabel("Bus"));
				this.add(new JLabel("etat :"));
				this.add((etat = new JLabel("LIBRE")));
			}
			@Override
			public void update(Observable o, Object arg) {
				if(!isUsed())
					etat.setText("LIBRE");
				else if(isUsed() && ! isTransmiting())
					etat.setText("APPELE");
				else if(isTransmiting())
					etat.setText("TRANSMET");
				
			}
			
		}
		
		if(observed == null)
		{
			observed = new Panel();
			addObserver((Observer) observed);
		}
		return observed;
	}
	

}
