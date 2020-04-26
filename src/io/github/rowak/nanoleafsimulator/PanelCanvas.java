package io.github.rowak.nanoleafsimulator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import io.github.rowak.nanoleafapi.Panel;

public class PanelCanvas extends JPanel
{
	private boolean initialize = true;
	private boolean showPanelIds;
	private Panel[] panels;
	private Map<Panel, Point> panelLocations;
	private Map<Panel, Point> panelLocationsTemp;
	private List<Polygon> squares;
	private Point mouseLast;
	private Polygon square;
	private Polygon uprightTriangle;
	private Polygon invertedTriangle;
	private BufferedImage buff;
	private Graphics buffG;
	private Component parent;
	private Simulator simulator;
	
	public PanelCanvas(Component parent)
	{
		this.parent = parent;
	}
	
	public void setPanels(Panel[] panels)
	{
		this.panels = panels;
		initPanels(panels);
	}
	
	public void setSimulator(Simulator simulator)
	{
		this.simulator = simulator;
		initUI();
	}
	
	public void setShowPanelIds(boolean showPanelIds)
	{
		this.showPanelIds = showPanelIds;
	}
	
	private void initUI()
	{
		setVisible(true);
		setSize(parent.getWidth(), parent.getHeight());
		addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				mouseLast = e.getPoint();
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				panelLocationsTemp = new HashMap<Panel, Point>(panelLocations);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		});
		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				Point mouse = e.getPoint();
				
				int xdiff = mouse.x - mouseLast.x;
				int ydiff = mouse.y - mouseLast.y;
				
				for (Panel p : panels)
				{
					int x = (panelLocationsTemp.get(p).x) + xdiff;
					int y = (panelLocationsTemp.get(p).y) + ydiff;
					panelLocations.put(p, new Point(x, y));
				}
				repaint();
			}
			
			public void mouseMoved(MouseEvent e){}
		});
	}
	
	private void initPanels(Panel[] panels)
	{
		final int X_OFFSET = 0;
		final int Y_OFFSET = 0;
		panelLocations = new HashMap<Panel, Point>();
		for (Panel p : panels)
			panelLocations.put(p, new Point(p.getX() + getWidth()/2 + X_OFFSET,
					-p.getY() + getHeight()/2 + Y_OFFSET));
		
		final int XPADDING = 75;
		final int YPADDING = 50;
		Point smallest = getSmallestXY();
		for (int i = 0; i < panels.length; i++)
		{
			Point loc = panelLocations.get(panels[i]);
			loc.setLocation(loc.x - smallest.x + XPADDING, loc.y - smallest.y + YPADDING);
		}
		panelLocationsTemp = new HashMap<Panel, Point>(panelLocations);
		squares = new ArrayList<Polygon>();
		
		if (simulator != null && simulator instanceof AuroraSimulator)
		{
			this.uprightTriangle = new UprightTriangle();
			this.invertedTriangle = new InvertedTriangle();
			this.square = null;
		}
		else if (simulator != null && simulator instanceof CanvasSimulator)
		{
			this.square = new Square();
			this.uprightTriangle = null;
			this.invertedTriangle = null;
		}
		adjustWindowSize();
	}
	
	private void adjustWindowSize()
	{
		Point largest = getLargestXY();
		Point smallest = getSmallestXY();
		final int PADDING = 150;  // 75px on both sizes
		final int MENU_BAR = 30;
		int sizex = largest.x - smallest.x + PADDING;
		int sizey = largest.y - smallest.y + PADDING + MENU_BAR;
		parent.setSize(sizex, sizey);
	}
	
	private Point getLargestXY()
	{
		int xlargest = 0, ylargest = 0;
		for (int i = 0; i < panels.length; i++)
		{
			Point loc = panelLocations.get(panels[i]);
			if (loc.x > xlargest)
			{
				xlargest = loc.x;
			}
			if (loc.y > ylargest)
			{
				ylargest = loc.y;
			}
		}
		return new Point(xlargest, ylargest);
	}
	
	private Point getSmallestXY()
	{
		int xsmallest = panelLocations.get(panels[0]).x;
		int ysmallest = panelLocations.get(panels[0]).y;
		for (int i = 0; i < panels.length; i++)
		{
			Point loc = panelLocations.get(panels[i]);
			if (loc.x < xsmallest)
			{
				xsmallest = loc.x;
			}
			if (loc.y < ysmallest)
			{
				ysmallest = loc.y;
			}
		}
		return new Point(xsmallest, ysmallest);
	}
	
	@Override
	public void paint(Graphics g)
	{
		buff = new BufferedImage(parent.getWidth(), parent.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		buffG = buff.getGraphics();
		
		buffG.setColor(Color.WHITE);
		buffG.fillRect(0, 0, getWidth(), getHeight());
		
		buffG.setColor(Color.BLACK);
		
		if (panels != null)
		{
			Graphics2D g2d = (Graphics2D)buffG;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			// Draw the panels
			for (Panel panel : panels)
			{
				int x = panelLocations.get(panel).x;
				int y = panelLocations.get(panel).y;
				int o = panel.getOrientation();
	//			if (initialize)
	//				panel.setRGBW(255, 255, 255, 0);
				
				// Draw the "centroids" (these are invisible, since
				// the panel colors are drawn after this line
	//			buffG.drawRect(x, y, 2, 2);
				
				Polygon tri = new Polygon();
				if (initialize)
					squares.add(tri);
				
				if (uprightTriangle != null && invertedTriangle != null)
				{
					// Create the panel outline shapes (regular and inverted)
					// 0, 120, 240, 360    (0 <= x <= 360)
					if (o == 0 || Math.abs(o) % 120 == 0)
					{
						for (int i = 0; i < uprightTriangle.npoints; i++)
						{
							tri.addPoint(x + uprightTriangle.xpoints[i],
									y + uprightTriangle.ypoints[i]);
						}
					}
					// 60, 180, 300   (0 <= x <= 360)
					else
					{
						for (int i = 0; i < invertedTriangle.npoints; i++)
						{
							tri.addPoint(x + invertedTriangle.xpoints[i],
									y + invertedTriangle.ypoints[i]);
						}
					}
				}
				else if (square != null)
				{
					for (int i = 0; i < square.npoints; i++)
					{
						tri.addPoint(x + square.xpoints[i], y + square.ypoints[i]);
					}
				}
				buffG.setColor(getPanelColorWithBrightness(panel));
				fillRotatedPanel(tri, (Graphics2D)buffG);
				buffG.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(4));
				drawRotatedPanel(tri, (Graphics2D)buffG);
				g2d.setStroke(new BasicStroke(1));
				
				if (showPanelIds)
				{
					buffG.setColor(Color.BLUE);
					buffG.setFont(new Font("Tahoma", Font.PLAIN, 25));
					buffG.drawString(panel.getId() + "", x, y);
				}
			}
		}
		initialize = false;
		
		g.drawImage(buff, 0, 0, this);
	}
	
	private Color getPanelColorWithBrightness(Panel p)
	{
		float factor = simulator.getBrightness()/100f;
		return new Color((int)(p.getRed()*factor), (int)(p.getGreen()*factor),
				(int)(p.getBlue()*factor));
	}
	
	public Point getCentroid()
	{
		int centroidX = 0, centroidY = 0;
		int numXPoints = 0, numYPoints = 0;
		List<Integer> xpoints = new ArrayList<Integer>();
		List<Integer> ypoints = new ArrayList<Integer>();
		
		for (Panel p : panels)
		{
			int x = panelLocations.get(p).x;
			int y = panelLocations.get(p).y;
			if (!xpoints.contains(x))
			{
				centroidX += x;
				xpoints.add(x);
				numXPoints++;
			}
			if (!ypoints.contains(y))
			{
				centroidY += y;
				ypoints.add(y);
				numYPoints++;
			}
		}
		centroidX /= numXPoints;
		centroidY /= numYPoints;
		return new Point(centroidX, centroidY);
	}
	
	private void drawRotatedPanel(Polygon panel, Graphics2D g2d)
	{
		AffineTransform original = g2d.getTransform();
		try
		{
			AffineTransform scaled = new AffineTransform();
			Point centroid = getCentroid();
			scaled.translate(centroid.getX(), centroid.getY());
			scaled.rotate(Math.toRadians(simulator.getGlobalOrientation()));
			scaled.translate(-centroid.getX(), -centroid.getY());
			g2d.setTransform(scaled);
			g2d.drawPolygon(panel);
		}
		finally
		{
			g2d.setTransform(original);
		}
	}
	
	private void fillRotatedPanel(Polygon panel, Graphics2D g2d)
	{
		AffineTransform original = g2d.getTransform();
		try
		{
			AffineTransform scaled = new AffineTransform();
			Point centroid = getCentroid();
			scaled.translate(centroid.getX(), centroid.getY());
			scaled.rotate(Math.toRadians(simulator.getGlobalOrientation()));
			scaled.translate(-centroid.getX(), -centroid.getY());
			g2d.setTransform(scaled);
			g2d.fillPolygon(panel);
		}
		finally
		{
			g2d.setTransform(original);
		}
	}
}
