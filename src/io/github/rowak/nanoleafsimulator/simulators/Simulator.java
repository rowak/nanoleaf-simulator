package io.github.rowak.nanoleafsimulator.simulators;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.rowak.nanoleafapi.Frame;
import io.github.rowak.nanoleafapi.Panel;
import io.github.rowak.nanoleafapi.tools.StaticAnimDataParser;
import io.github.rowak.nanoleafsimulator.net.DeviceHTTPServer;
import io.github.rowak.nanoleafsimulator.net.DeviceUDPServer;
import io.github.rowak.nanoleafsimulator.panelcanvas.PanelCanvas;

public abstract class Simulator
{
	private final int DEFAULT_HTTP_PORT = 7144;
	private final int DEFAULT_UDP_PORT = 7143;
	
	private final static String DEFAULT_NAME = "Generic Debug";
	private final static String DEFAULT_SERIAL_NO = "0123456789";
	private final static String DEFAULT_MANUFACTURER = "rowak";
	private final static String DEFAULT_MODEL = "NL29";
	
	protected int hue = 100, saturation = 100, brightness = 100;
	protected int colorTemp = 3400;
	protected int globalOrientation = 0;
	protected String effect = "*Solid*";
	protected List<String> effects;
	protected Panel[] panels;
	private DeviceHTTPServer httpServer;
	private DeviceUDPServer udpServer;
	protected PanelCanvas canvas;
	
	public Simulator(PanelCanvas canvas)
	{
		this.canvas = canvas;
		startHTTPServer();
		effects = new ArrayList<String>();
	}
	
	public void stopSimulator()
	{
		stopHTTPServer();
		stopUDPServer();
	}
	
	public String getName()
	{
		return DEFAULT_NAME;
	}
	
	public String getSerialNo()
	{
		return DEFAULT_SERIAL_NO;
	}
	
	public String getManufacturer()
	{
		return DEFAULT_MANUFACTURER;
	}
	
	public abstract String getFirmwareVersion();
	
	public abstract String getHardwareVersion();
	
	public String getModel()
	{
		return DEFAULT_MODEL;
	}
	
	public boolean getOn()
	{
		for (Panel p : panels)
		{
			if (p.getRed() != 0 || p.getGreen() != 0 ||
					p.getBlue() != 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public void setOn(boolean on)
	{
		Color c = on ? Color.WHITE : Color.BLACK;
		for (Panel p : panels)
		{
			p.setRGB(c.getRed(), c.getGreen(), c.getBlue());
		}
		hue = 0;
		saturation = 0;
		canvas.repaint();
	}
	
	public int getHue()
	{
		return hue;
	}
	
	public void setHue(int hue)
	{
		this.hue = hue;
		io.github.rowak.nanoleafapi.Color c = io.github.rowak.nanoleafapi.Color.fromHSB(
				hue, saturation, brightness);
		for (Panel p : panels)
		{
			p.setRGB(c.getRed(), c.getGreen(), c.getBlue());
		}
		canvas.repaint();
	}
	
	public int getSaturation()
	{
		return saturation;
	}
	
	public void setSaturation(int saturation)
	{
		this.saturation = saturation;
		io.github.rowak.nanoleafapi.Color c = io.github.rowak.nanoleafapi.Color.fromHSB(
				hue, saturation, brightness);
		for (Panel p : panels)
		{
			p.setRGB(c.getRed(), c.getGreen(), c.getBlue());
		}
		canvas.repaint();
	}
	
	public int getBrightness()
	{
		return brightness;
	}
	
	public void setBrightness(int brightness)
	{
		this.brightness = brightness;
		canvas.repaint();
	}
	
	public int getColorTemperature()
	{
		return colorTemp;
	}
	
	public void setColorTemperature(int colorTemp)
	{
		this.colorTemp = colorTemp;
		Color ctColor = colorTempToColor();
		for (Panel p : panels)
		{
			p.setRGB(ctColor.getRed(), ctColor.getGreen(),
					ctColor.getBlue());
		}
		canvas.repaint();
	}
	
	public String getSelectedEffect()
	{
		return effect;
	}
	
	public void setSelectedEffect(String effect)
	{
		// NOT SUPPORTED
	}
	
	public String[] getEffectsList()
	{
		return effects.toArray(new String[]{});
	}
	
	public Panel[] getPanels()
	{
		return panels;
	}
	
	public void setPanels(Panel[] panels)
	{
		this.panels = panels;
		canvas.setPanels(panels);
		canvas.repaint();
	}
	
	public int getGlobalOrientation()
	{
		return globalOrientation;
	}
	
	public void setGlobalOrientation(int globalOrientation)
	{
		this.globalOrientation = globalOrientation;
		canvas.repaint();
	}
	
	public boolean extStreamingEnabled()
	{
		return udpServer != null;
	}
	
	public void setPanelColor(Panel panel, Color color)
	{
		panel.setRGB(color.getRed(),
				color.getGreen(), color.getBlue());
		canvas.repaint();
	}
	
	public void transitionToColor(Panel p, Color color, int time)
			throws InterruptedException
	{
		final int NUM_COLORS = 300;
		int deltaRed = color.getRed() - p.getRed();
		int deltaGreen = color.getGreen() - p.getGreen();
		int deltaBlue = color.getBlue() - p.getBlue();
		
		if (deltaRed != 0 || deltaGreen != 0 || deltaBlue != 0)
		{
			for (int i = 0; i < NUM_COLORS; i++)
			{
				int red = p.getRed() + ((deltaRed * i) / NUM_COLORS);
				int green = p.getGreen() + ((deltaGreen * i) / NUM_COLORS);
				int blue = p.getBlue() + ((deltaBlue * i) / NUM_COLORS);
				
				if (red < 0 || red > 255 ||
						green < 0 || green > 255 ||
						blue < 0 || blue > 255)
				{
					break;
				}
				
				p.setRGB(red, green, blue);
				canvas.repaint();
				Thread.sleep((int)(time/30f));
			}
		}
	}
	
	public void setStaticEffect(String animData)
	{
		StaticAnimDataParser sadp = new StaticAnimDataParser(animData);
		System.out.println(animData);
		for (Panel p : panels)
		{
			if (sadp.getFrames().containsKey(p.getId()))
			{
				Frame f = sadp.getFrame(p);
				new Thread(() ->
				{
					try
					{
						transitionToColor(p, new Color(f.getRed(),
								f.getGreen(), f.getBlue()), f.getTransitionTime()*30);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}).start();
//				p.setRGB(f.getRed(), f.getGreen(), f.getBlue());
			}
		}
		canvas.repaint();
	}
	
	// Algorithm: http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
	private Color colorTempToColor()
	{
		int ct = colorTemp/100;
		int r = 0, g = 0, b = 0;
		
		// red
		if (ct <= 66)
		{
			r = 255;
		}
		else
		{
			r = ct-60;
			r = (int)(329.698727446 * Math.pow(r, -0.1332047592));
			if (r < 0)
			{
				r = 0;
			}
			else if (r > 255)
			{
				r = 255;
			}
		}
		
		// green
		if (ct <= 66)
		{
			g = ct;
			g = (int)(99.4708025861 * Math.log1p(g) - 161.1195681661);
			if (g < 0)
			{
				g = 0;
			}
			else if (g > 255)
			{
				g = 255;
			}
		}
		else
		{
			g = ct-60;
			g = (int)(288.1221695283 * Math.pow(g, -0.0755148492));
			if (g < 0)
			{
				g = 0;
			}
			else if (g > 255)
			{
				g = 255;
			}
		}
		
		// blue
		if (ct >= 66)
		{
			b = 255;
		}
		else
		{
			if (ct <= 19)
			{
				b = 0;
			}
			else
			{
				b = ct-10;
				b = (int)(138.5177312231 * Math.log1p(b) - 305.0447927307);
				if (b < 0)
				{
					b = 0;
				}
				else if (b > 255)
				{
					b = 255;
				}
			}
		}
		return new Color(r, g, b);
	}
	
	private void startHTTPServer()
	{
		try
		{
			httpServer = new DeviceHTTPServer(this, DEFAULT_HTTP_PORT, DEFAULT_UDP_PORT);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	private void stopHTTPServer()
	{
		if (httpServer != null)
		{
			httpServer.stop();
		}
	}
	
	public void startUDPServer()
	{
		udpServer = new DeviceUDPServer(this, DEFAULT_UDP_PORT);
	}
	
	public void stopUDPServer()
	{
		if (udpServer != null)
		{
			udpServer.stop();
		}
	}
}
