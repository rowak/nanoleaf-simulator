package io.github.rowak.nanoleafsimulator.net;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import io.github.rowak.nanoleafsimulator.simulators.Simulator;

public class DeviceUDPServer
{
	private boolean running;
	private DatagramSocket sock;
	
	public DeviceUDPServer(Simulator simulator, int port)
	{
		run(simulator, port);
	}
	
	public void stop()
	{
		if (running && sock != null)
		{
			running = false;
			sock.disconnect();
			sock.close();
		}
	}
	
	private void run(Simulator simulator, int port)
	{
		running = true;
		new Thread(() ->
		{
			try
			{
				sock = new DatagramSocket(port);
			}
			catch (SocketException se)
			{
				se.printStackTrace();
			}
			byte[] rData = new byte[1024];
			while (running)
			{
				DatagramPacket rPacket =
						new DatagramPacket(rData, rData.length);
				try
				{
					sock.receive(rPacket);
				}
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
				String animData = bytesToAnimData(rPacket.getData(),
						rPacket.getLength());
				simulator.setStaticEffect(animData);
			}
		}).start();
	}
	
	private String bytesToAnimData(byte[] bytes, int bufferLength)
	{
		String animData = "";
		for (int i = 0; i < bufferLength; i++)
			animData += ((int)(bytes[i]&0xFF)) + " ";
		return animData;
	}
}
