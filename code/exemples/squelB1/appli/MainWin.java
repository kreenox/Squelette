package appli;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


public class MainWin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel ckpane;
	private JPanel tab;

	public static void main(String args[])
	{
		Compute c = new Compute(0xFFFF, "ROM");
		MainWin w = new MainWin(c);
		w.setVisible(true);
	}
	
	
	public MainWin(Compute c)
	{
		this.setTitle("Squelette");
		this.setSize(600, 400);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//mise en place des menus
		JMenuBar menu = new JMenuBar();
		menu.setMaximumSize(new Dimension(999999999, 30));
		this.getContentPane().add(menu);
		JMenu men = new JMenu("Machine");
		men.add(new JMenuItem("lancer la machine"));
		men.add(new JMenuItem("Arreter la machine"));
		men.add(new JMenuItem("Redemarer la machine"));
		menu.add(men);
		men = new JMenu("Aide");
		men.add(new JMenuItem("à propos"));
		menu.add(men);
		//mise en place du panel de l'horloge
		JSlider slid = new JSlider(SwingConstants.HORIZONTAL, 100, 2000, 500);
		JTextField txf = new JTextField("500");
		ckpane = new JPanel();
		ckpane.setLayout(new BoxLayout(ckpane, BoxLayout.LINE_AXIS));
		ckpane.add(new JLabel("Horloge manuelle : "));
		ckpane.add(slid);
		ckpane.add(txf);
		//mise en place du textefield et de la barre
		slid.setMaximumSize(new Dimension(450, 30));
		slid.setMinimumSize(new Dimension(200, 30));
		slid.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				int temp = slid.getValue();
				if(temp != 2000)
					txf.setText("" + temp);
				else txf.setText("manuel");
			}});
		txf.setMaximumSize(new Dimension(150, 30));
		txf.setMinimumSize(new Dimension(75, 30));
		txf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int temp = Integer.parseInt(txf.getText());
				if(temp > 2000)
					slid.setValue(2000);
				if(temp < 100)
					slid.setValue(100);
				else slid.setValue(temp);
			}});
		ckpane.add(new JButton("pause"));
		JButton man = new JButton("Tick manuels");
		ckpane.add(man);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.getContentPane().add(c.mainck.getUI() /*ckpane*/);
		
		//mise en place du panel des composants
		tab = new JPanel();
		tab.setLayout(new GridLayout(2, 2, 5, 5));
		tab.add(c.proc.getUI());
		tab.add(c.ram.getUI());
		tab.add(c.b.getUI());
		this.getContentPane().add(tab);
		
	}
	
}
