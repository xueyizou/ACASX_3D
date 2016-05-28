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
package visualization.configuration;

public class GlobalConfig 
{
	public double worldX = 20000; 
	public double worldY = 5000; 
	public double worldZ = 10000; 
	
	public boolean collisionAvoidanceEnabler=true;
	public boolean accidentDetectorEnabler=true;
	public boolean whiteNoiseEnabler=true;	
	
	public double stdDevX =3;//[0,3]
	public double stdDevY =3;//[0,3]
	public double stdDevZ =3;//[0,3]
	
	public double maxGS =304;
	public double minGS =169;
	public double maxVS =58;
	public double minVS =-67;
}
