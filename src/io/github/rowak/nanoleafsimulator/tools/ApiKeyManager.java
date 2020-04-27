package io.github.rowak.nanoleafsimulator.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiKeyManager
{
	private static final String API_KEYS_PATH = "api_keys.txt";
	
	public static boolean hasKey(String key)
	{
		String[] keys = getKeys();
		for (String keyx : keys)
		{
			if (keyx.equals(key))
			{
				return true;
			}
		}
		return false;
	}
	
	public static String[] getKeys()
	{
		List<String> keys = new ArrayList<String>();
		BufferedReader reader = null;
		try
		{
			File file = new File(API_KEYS_PATH);
			if (!file.exists())
			{
				file.createNewFile();
			}
			reader = new BufferedReader(new FileReader(file));
			String key = "";
			while ((key = reader.readLine()) != null)
			{
				keys.add(key);
			}
			reader.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		return keys.toArray(new String[0]);
	}
	
	public static void addKey(String key)
	{
		BufferedWriter writer = null;
		try
		{
			File file = new File(API_KEYS_PATH);
			if (!file.exists())
			{
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write("\n" + key);
			writer.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
