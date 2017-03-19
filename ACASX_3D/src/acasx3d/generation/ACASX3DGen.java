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
package acasx3d.generation;

public class ACASX3DGen 
{

	public ACASX3DGen() 
	{
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("hi!Process starts...");
		
		long time0 = System.currentTimeMillis();		
		MDP mdp = new MDP();
		long time1 = System.currentTimeMillis();
		System.out.println("MDP building Done! The running time is "+ (time1-time0)/1000 +" senconds.");
		MDPVI mdpVI = new MDPVI(mdp);
		long time2 = System.currentTimeMillis();
		System.out.println("MDP Value Iteration Done! The running time is "+ (time2-time1)/1000 +" senconds.");
		mdpVI.storeQValues();
		long time3 = System.currentTimeMillis();
		System.out.println("MDP QValue Store Done! The running time is "+ (time3-time2)/1000 +" senconds.");
		
		long time4 = System.currentTimeMillis();
		DTMC dtmc = new DTMC();
		long time5 = System.currentTimeMillis();
		System.out.println("DTMC building Done! The running time is "+ (time5-time4)/1000 +" senconds.");
		DTMCVI dtmcVI = new DTMCVI(dtmc);
		long time6 = System.currentTimeMillis();
		System.out.println("DTMC Value Iteration Done! The running time is "+ (time6-time5)/1000 +" senconds.");
		dtmcVI.storeValues("entryTimeDistributionFile");
		long time7 = System.currentTimeMillis();
		System.out.println("DTMC values Storage Done! The running time is "+ (time7-time6)/1000 +" senconds.");

	}

}
