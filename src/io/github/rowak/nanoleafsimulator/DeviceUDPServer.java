package io.github.rowak.nanoleafsimulator;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DeviceUDPServer
{
	public DeviceUDPServer(Simulator simulator, int port)
	{
		run(simulator, port);
	}
	
	private void run(Simulator simulator, int port)
	{
		new Thread(() ->
		{
			DatagramSocket sock = null;
			try
			{
				sock = new DatagramSocket(port);
			}
			catch (SocketException se)
			{
				se.printStackTrace();
			}
			byte[] rData = new byte[1024];
			while (true)
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
