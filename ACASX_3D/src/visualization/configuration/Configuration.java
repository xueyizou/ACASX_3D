/*******************************************************************************
 *  Copyright (C) Xueyi Zou - All Rights Reserved
 *  Written by Xueyi Zou <xz972@york.ac.uk>, 2015
 *  You are free to use/modify/distribute this file for whatever purpose!
 *  -----------------------------------------------------------------------
 *  |THIS FILE IS DISTRIBUTED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 *  |WARRANTY. THE USER WILL USE IT AT HIS/HER OWN RISK. THE ORIGINAL
 *  |AUTHORS AND COPPELIA ROBOTICS GMBH WILL NOT BE LIABLE FOR DATA LOSS,
 *  |DAMAGES, LOSS OF PROFITS OR ANY OTHER KIND OF LOSS WHILE USING OR
 *  |MISUSING THIS SOFTWARE.
 *  ------------------------------------------------------------------------
 *******************************************************************************/
/**
 * 
 */
package visualization.configuration;

import java.util.HashMap;


/**
 * @author Xueyi
 *
 */
public class Configuration
{
	public GlobalConfig globalConfig;
	public OwnshipConfig ownshipConfig;
	public HashMap<String,EncounterConfig> encountersConfig;
	
	private static Configuration config=null;
	
	public static Configuration getInstance()
	{
		if( config==null)
		{
			config=new Configuration();
		}
		return config;
	}


	private Configuration() 
	{
		globalConfig = new GlobalConfig();
		ownshipConfig = new OwnshipConfig();
		encountersConfig = new HashMap<>();
	}
	
	public void addIntruderConfig(String alias, EncounterConfig encounterConfig)
	{
		encountersConfig.put(alias,encounterConfig);
	}
		
	public String toString()
	{
		StringBuilder str = new StringBuilder();
    	str.append(ownshipConfig.ownshipVy+" ");
    	str.append(ownshipConfig.ownshipGs+" ");
    	for (EncounterConfig encounterConfig: encountersConfig.values() )
    	{
    		str.append(encounterConfig.CAPY+" ");
    		str.append(encounterConfig.CAPR+" ");
    		str.append(encounterConfig.CAPTheta+" ");
    		str.append(encounterConfig.CAPVy+" ");
    		str.append(encounterConfig.CAPGs+" ");
    		str.append(encounterConfig.CAPBearing+" ");    		
    	}
		return str.toString();
	}
}
