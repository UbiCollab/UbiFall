package com.example.falldetectioncentral;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import android.content.Context;
import android.util.Log;



public class Config {
	private SettingsObject settingsObject;
	private Context context;
	private String FILENAME = "AssociateSettingsApp";
	
	
	public Config(Context context)
	{
		this.context = context;
		loadSettingsFromFile();
		
	}
	
	public void setSettingsObject(SettingsObject settingsObject)
	{
		this.settingsObject = settingsObject;
	}
	
	public SettingsObject getSettingsObject()
	{
		return settingsObject;
	}
	
	public void saveSettingsToFile()
	{
		try
		{
			FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(settingsObject);
			os.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("Config","error: did not write to file");
		}
	}
	
	public void loadSettingsFromFile()
	{
		try
		{
			FileInputStream in = context.openFileInput(FILENAME);
			ObjectInputStream is = new ObjectInputStream(in);
			settingsObject = (SettingsObject)is.readObject();
			is.close();
		}
		catch(Exception e)
		{
			settingsObject = new SettingsObject();
		}
	}
	
}
