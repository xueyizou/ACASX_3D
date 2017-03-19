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
package visualization.modeling.encountergenerator;

import sim.util.Double3D;
import visualization.configuration.Configuration;
import visualization.configuration.EncounterConfig;
import visualization.configuration.GlobalConfig;
import visualization.modeling.SAAModel;
import visualization.modeling.uas.UAS;
import visualization.modeling.uas.UASPerformance;
import visualization.saa.ACASX3DProxy;
import visualization.saa.AutoPilot;


public class EncounterGenerationFactory {
	private static GlobalConfig globalConfig = Configuration.getInstance().globalConfig;
	
	public static UAS generateIntruder(SAAModel state, UAS ownship, String intruderAlias, EncounterConfig encounterConfig)
	{			
		Double3D intruderVel = new Double3D(encounterConfig.CAPGs*Math.cos(Math.toRadians(encounterConfig.CAPBearing)), encounterConfig.CAPVy, encounterConfig.CAPGs*Math.sin(Math.toRadians(encounterConfig.CAPBearing)));
		Double3D ownshipCAP = ownship.getLoc().add(ownship.getVel().multiply(encounterConfig.CAPT));
		Double3D intruderCAP =ownshipCAP.add(new Double3D(encounterConfig.CAPR*Math.cos(Math.toRadians(encounterConfig.CAPTheta)), encounterConfig.CAPY, encounterConfig.CAPR*Math.sin(Math.toRadians(encounterConfig.CAPTheta))));
		Double3D location=intruderCAP.add(intruderVel.negate().multiply(encounterConfig.CAPT));		

		UASPerformance intruderPerformance = new UASPerformance(globalConfig.stdDevX, globalConfig.stdDevY,globalConfig.stdDevZ,
				globalConfig.maxGS, globalConfig.minGS, globalConfig.maxVS, globalConfig.minVS);
		
		UAS intruder = new UAS(location, intruderVel,intruderPerformance);
		intruder.setAlias(intruderAlias);			
		
		AutoPilot ap= new AutoPilot(state, intruder, Configuration.getInstance().globalConfig.whiteNoiseEnabler,"WhiteNoise");
		ACASX3DProxy acasx= new ACASX3DProxy(state, intruder);
		
		intruder.init(ap,acasx);
		
		return intruder;	
	}

}
