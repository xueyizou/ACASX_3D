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
