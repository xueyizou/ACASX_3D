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
package visualization.modeling.observer;

import sim.engine.SimState;
import sim.engine.Steppable;
import visualization.configuration.Configuration;
import visualization.modeling.SAAModel;
import visualization.modeling.uas.UAS;

/**
 * @author xueyi
 *
 */
public class CollisionDetector implements Steppable
{
	private static final long serialVersionUID = 1L;
	
	public static final double thresholdH = 500;
	public static final double thresholdV = 100;
	
	private int numCollisions=0;		
	
	public CollisionDetector()
	{	
	}

	@Override
	public void step(SimState simState)
	{
		if(!Configuration.getInstance().globalConfig.accidentDetectorEnabler)
		{
			return;
		}
		SAAModel state = (SAAModel)simState;		
		
		UAS ownship=(UAS) state.uasBag.get(0);

		for (int k = 1; k<state.uasBag.size(); k++)
		{
			UAS intruder=(UAS)state.uasBag.get(k);
			if(intruder.activeState!=0)
			{
				continue;
			}
			if (detectCollisionBetweenUAS(ownship, intruder))
			{
				numCollisions++;
				ownship.activeState=-1;
				intruder.activeState=-1;
				break;
			}
		}

		dealWithTermination(state);		
	}

	
	private boolean detectCollisionBetweenUAS(UAS uas1, UAS uas2)
	{	
		double deltaHori=Math.pow((uas1.getLoc().x-uas2.getLoc().x),2)+Math.pow((uas1.getLoc().z-uas2.getLoc().z),2);
		double deltaVert=Math.abs(uas1.getLoc().y-uas2.getLoc().y);	
		return (deltaHori<=500*500)&&(deltaVert<=100);		
	}
	
	
    public void dealWithTermination(SAAModel state)
	{
    	int noActiveAgents =0;
    	for(Object o: state.uasBag)
    	{
    		if(((UAS)o).activeState==0)
    		{
    			noActiveAgents++;
    		}
    		
    	}
    	
		if(noActiveAgents < 1)
		{
			state.schedule.clear();
			state.kill();
		}
	 }

	public int getNumCollisions() 
	{
		return numCollisions;
	}
	
	public boolean hasAccident()
	{
		return (numCollisions>0);
	}

}
