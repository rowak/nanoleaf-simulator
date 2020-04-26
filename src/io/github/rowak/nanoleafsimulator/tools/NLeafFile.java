package io.github.rowak.nanoleafsimulator.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.rowak.nanoleafapi.Panel;

public class NLeafFile
{
	private String type;
	private Panel[] panels;
	
	public String getType()
	{
		return type;
	}
	
	public Panel[] getPanels()
	{
		return panels;
	}
	
	public static NLeafFile fromFile(File file)
	{
		NLeafFile nleaf = new NLeafFile();
		List<Panel> panels = new ArrayList<Panel>();
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String data[];
			String line;
			while ((line = reader.readLine()) != null)
			{
				data = line.split(" ");
				if (data[0].equals("type"))
				{
					nleaf.type = data[1];
				}
				else if (data[0].equals("panel"))
				{
					try
					{
						Panel panel = new Panel(Integer.parseInt(data[1]),
								Integer.parseInt(data[2]), Integer.parseInt(data[3]),
								Integer.parseInt(data[4]));
						panels.add(panel);
					}
					catch (NumberFormatException nfe)
					{
						nfe.printStackTrace();
					}
				}
			}
			nleaf.panels = panels.toArray(new Panel[0]);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		return nleaf;
	}
}
