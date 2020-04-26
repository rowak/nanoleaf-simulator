package io.github.rowak.nanoleafsimulator;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import io.github.rowak.nanoleafapi.Panel;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;

public class Main extends JFrame
{
	private PanelCanvas canvas;
	private Simulator simulator;
	
	public Main()
	{
		initUI();
	}
	
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		canvas = new PanelCanvas(this);
		simulator = new AuroraSimulator(canvas);
		canvas.setSimulator(simulator);
		// default aurora documentation layout
		Panel[] panels = new Panel[]
		{
			new Panel(15, 599, 303, 60),
			new Panel(16, 524, 259, 240),
			new Panel(17, 524, 173, 300),
			new Panel(18, 599, 129, 120),
			new Panel(19, 599, 43, 60),
			new Panel(20, 524, 0, 240),
			new Panel(21, 449, 43, 60),
			new Panel(23, 374, 0, 240),
			new Panel(24, 299, 43, 300),
			new Panel(25, 299, 129, 0)
		};
//		Panel[] panels = new Panel[]
//		{
//			new Panel(1, 100, 100, 60),
//			new Panel(2, 324, 56, 0),
//			new Panel(3, 249, -159, 60),
//			new Panel(4, 174, 56, 240),
//			new Panel(5, 324, -29, 60),
//			new Panel(6, -49, 100, 60),
//			new Panel(7, 399, 99, 300),
//			new Panel(8, 174, -29, 60),
//			new Panel(9, 25, 56, 120),
//			new Panel(10, 249, -73, 240)
//		};
		// default canvas documentation layout
//		Panel[] panels = new Panel[]
//				{
//					new Panel(1, 250, 100, 0),
//					new Panel(2, 150, 100, 0),
//					new Panel(3, 50, 100, 0),
//					new Panel(4, 0, 0, 180),
//					new Panel(5, 50, 200, 0),
//					new Panel(6, 150, 250, 180)
//				};
		simulator.setPanels(panels);
		
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		
		JMenu fileMenu = new JMenu("File");
		menu.add(fileMenu);
		
		JMenuItem openLayoutItem = new JMenuItem("Open Layout");
		fileMenu.add(openLayoutItem);
		
		JMenuItem saveLayoutItem = new JMenuItem("Save Layout");
		fileMenu.add(saveLayoutItem);
		
		JMenu editMenu = new JMenu("Edit");
		menu.add(editMenu);
		
		JCheckBoxMenuItem showPanelIdsItem = new JCheckBoxMenuItem("Show Panel IDs");
		showPanelIdsItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				canvas.setShowPanelIds(showPanelIdsItem.getState());
				canvas.repaint();
			}
		});
		editMenu.add(showPanelIdsItem);
		setContentPane(canvas);
	}
	
	public static void main(String[] args)
	{
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (Exception e) {}
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Main frame = new Main();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
