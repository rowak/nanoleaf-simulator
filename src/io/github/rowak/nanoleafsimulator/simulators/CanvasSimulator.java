package io.github.rowak.nanoleafsimulator.simulators;
import java.awt.Polygon;
import java.awt.Component;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.rowak.nanoleafapi.Panel;
import io.github.rowak.nanoleafsimulator.panelcanvas.PanelCanvas;

public class CanvasSimulator extends Simulator
{
	private final static String DEFAULT_NAME = "Canvas Debug";
	private final static String DEFAULT_FIRMWARE_VERSION = "1.4.0";
	private final static String DEFAULT_HARDWARE_VERSION = "2.0.4";
	
	public CanvasSimulator(PanelCanvas canvas)
	{
		super(canvas);
	}
	
	@Override
	public String getName()
	{
		return DEFAULT_NAME;
	}

	@Override
	public String getFirmwareVersion()
	{
		return DEFAULT_FIRMWARE_VERSION;
	}

	@Override
	public String getHardwareVersion()
	{
		return DEFAULT_HARDWARE_VERSION;
	}
}
