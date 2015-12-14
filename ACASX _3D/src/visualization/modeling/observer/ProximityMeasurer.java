package visualization.modeling.observer;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double3D;
import visualization.modeling.SAAModel;
import visualization.modeling.uas.Proximity;
import visualization.modeling.uas.UAS;


public class ProximityMeasurer implements Steppable
{
	private static final long serialVersionUID = 1L;
	
	public ProximityMeasurer()
	{
	}
	
	@Override
	public void step(SimState simState) 
	{
		SAAModel state = (SAAModel)simState;	
		UAS ownship=(UAS) state.uasBag.get(0);
		Double3D ownshipLoc=ownship.getLoc();
		
		Proximity tempP=new Proximity(Double.MAX_VALUE,Double.MAX_VALUE);
		for(int i=1; i<state.uasBag.size(); i++)//loop all the intruders
		{		    	
			UAS intruder= (UAS)state.uasBag.get(i);
			if(intruder.activeState!=0)
			{
				continue;
			}
			Double3D intruderLoc=intruder.getLoc();			
			Proximity p= new Proximity(ownshipLoc,intruderLoc);
	    	if(p.lessThan(tempP))
	    	{
	    		tempP=p;	    		
	    	}	    
		}
	    ownship.setTempProximity(tempP);
		if (tempP.lessThan( ownship.getMinProximity()))
		{
			ownship.setMinProximity(tempP);	
		}
		
//		UAS uas1;
//	    for(int i=0; i<state.uasBag.size(); i++)
//		{		    	
//			uas1= (UAS)state.uasBag.get(i);
//			if(!uas1.isActive)
//			{
//				continue;
//			}
//			
//			double tempDistanceToDanger=Double.MAX_VALUE;
//			for (int k = i+1; k<state.uasBag.size(); k++)
//			{
//				UAS uas2=(UAS)state.uasBag.get(k);
//				if(!uas2.isActive)
//				{
//					continue;
//				}
//				double d= uas1.getLocation().distance(uas2.getLocation());
//		    	if(d<tempDistanceToDanger)
//		    	{
//		    		tempDistanceToDanger=d;
//		    		
//		    	}
//			}
//			uas1.setTempDistanceToDanger(tempDistanceToDanger);	
//
//	    	if (tempDistanceToDanger < uas1.getMinDistanceToDanger())
//			{
//				uas1.setMinDistanceToDanger(tempDistanceToDanger);	
//			}
//		}
		
	}	

}
