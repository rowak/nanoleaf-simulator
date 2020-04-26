package io.github.rowak.nanoleafsimulator;
public class AuroraSimulator extends Simulator
{
	private final static String DEFAULT_NAME = "Aurora Debug";
	private final static String DEFAULT_FIRMWARE_VERSION = "1.4.0";
	private final static String DEFAULT_HARDWARE_VERSION = "2.0.4";
	
	public AuroraSimulator(PanelCanvas canvas)
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
