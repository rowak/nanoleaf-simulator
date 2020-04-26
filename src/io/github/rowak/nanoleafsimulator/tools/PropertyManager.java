package io.github.rowak.nanoleafsimulator.tools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PropertyManager
{
	private File file;
	
	public PropertyManager()
	{
		file = new File(getPropertiesFilePath());
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public static String getPropertiesFilePath()
	{
		String dir = "";
		final String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win"))
		{
			dir = System.getenv("APPDATA") + "/Nanoleaf for Desktop";
		}
		else if (os.contains("mac"))
		{
			dir = System.getProperty("user.home") +
					"/Library/Application Support/Nanoleaf for Desktop";
		}
		else if (os.contains("nux"))
		{
			dir = System.getProperty("user.home") + "/.Nanoleaf for Desktop";
		}
		
		File dirFile = new File(dir);
		if (!dirFile.exists())
		{
			dirFile.mkdir();
		}
		
		return dir + "/preferences.txt";
	}
	
	public String getProperty(String key)
	{
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] data = line.split("" + (char)28);
				String xKey = data[0];
				String xValue = data[1];
				if (key.equals(xKey))
				{
					return xValue;
				}
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		return null;
	}
	
	public void setProperty(String key, Object value)
	{
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try
		{
			String properties = "";
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null)
			{
				String xKey = line.split("" + (char)28)[0];
				if (!key.equals(xKey))
				{
					properties += line + "\n";
				}
			}
			properties += (key + (char)28 + value) + "\n";
			
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(properties);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
				writer.flush();
				writer.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}
}
