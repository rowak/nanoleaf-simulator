package io.github.rowak.nanoleafsimulator;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;

import io.github.rowak.nanoleafapi.Panel;
import io.github.rowak.nanoleafsimulator.panelcanvas.PanelCanvas;
import io.github.rowak.nanoleafsimulator.simulators.AuroraSimulator;
import io.github.rowak.nanoleafsimulator.simulators.CanvasSimulator;
import io.github.rowak.nanoleafsimulator.simulators.Simulator;
import io.github.rowak.nanoleafsimulator.tools.NLeafFile;

import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;

public class Main extends JFrame
{
	private PanelCanvas canvas;
	private Simulator simulator;
	
	public Main()
	{
		initUI();
	}
	
	private void startSimulator(Panel[] panels, String type)
	{
		if (simulator != null)
		{
			simulator.stopSimulator();
			simulator = null;
		}
		if (canvas == null)
		{
			canvas = new PanelCanvas(this);
		}
		if (type.toLowerCase().equals("aurora"))
		{
			simulator = new AuroraSimulator(canvas);
		}
		else if (type.toLowerCase().equals("canvas"))
		{
			simulator = new CanvasSimulator(canvas);
		}
		canvas.setSimulator(simulator);
		simulator.setPanels(panels);
	}
	
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
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
		startSimulator(panels, "aurora");
		
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		
		JMenu fileMenu = new JMenu("File");
		menu.add(fileMenu);
		
		JMenuItem openLayoutItem = new JMenuItem("Open Layout");
		openLayoutItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("Nanoleaf Layout Files", ".nleaf"));
				int code = chooser.showOpenDialog(Main.this);
				if (code == JFileChooser.APPROVE_OPTION)
				{
					File file = chooser.getSelectedFile();
					NLeafFile nleaf = NLeafFile.fromFile(file);
					startSimulator(nleaf.getPanels(), nleaf.getType());
				}
			}
		});
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
