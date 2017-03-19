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
package visualization.modeling.env;

import sim.util.Double3D;

public class Target
{	
	private Double3D loc;

	public Target(Double3D loc)
	{
		this.loc=loc;
	}
	
	public String toString()
	{
		return "Target"+this.loc.toString();
	}

	public Double3D getLoc() {
		return loc;
	}
	
	public void setLoc(Double3D loc)
	{
		this.loc=loc;
	}

}
