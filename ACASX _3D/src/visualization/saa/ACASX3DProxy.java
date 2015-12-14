/**
 * 
 */
package visualization.saa;

import java.util.Map;
import java.util.Map.Entry;

import sim.engine.SimState;
import sim.engine.Steppable;
import visualization.modeling.SAAModel;
import visualization.modeling.uas.UAS;
import acasx3d.ACASX3D_SingleThreat;

/**
 * @author Xueyi
 *
 */
public class ACASX3DProxy implements Steppable
{	
	private static final long serialVersionUID = 1L;
	private SAAModel state; 
	private UAS hostUAS;
	ACASX3D_SingleThreat acas;
	int lastRA=0;
	public ACASX3DProxy(SimState simstate, UAS uas) 
	{
		state = (SAAModel) simstate;
		hostUAS = uas;	
		acas=new ACASX3D_SingleThreat();
	}
	
	
	
	public void step(SimState simState)
	{
		if(hostUAS.activeState == 0)
		{	
			execute();			
		}		 
	}
	
	
	public void execute()
	{
		int numUAS=state.uasBag.size();
		double maxRisk = Double.MIN_VALUE;
		double tempRisk;
		UAS urgentestIntruder=null;
		for(int i=0; i<numUAS; i++)
		{
			UAS uas= (UAS)state.uasBag.get(i);
			if(uas == hostUAS)
			{
				continue;
			}
			else
			{
				tempRisk =calculateCollisionRisk(uas);
				if(tempRisk>maxRisk)
				{
					maxRisk = tempRisk;
					urgentestIntruder=uas;
				}
			}			
		
		}
		int newRA;
		if(urgentestIntruder!=null)
		{
			acas.update(hostUAS.getLoc(), hostUAS.getVel(), urgentestIntruder.getLoc(), urgentestIntruder.getVel(),lastRA);
			
			newRA=acas.execute();				
		}
		else
		{
			newRA=0;
		}
		hostUAS.getAp().setActionCode(newRA);
		lastRA=newRA;
	}

	public double calculateCollisionRisk(UAS uas)
	{
		Map<Integer, Double> entryTimeDistribution=acas.calculateEntryTimeDistributionDTMC(hostUAS.getLoc(), hostUAS.getVel(), uas.getLoc(), uas.getVel());
		double aveTime=0;
		for(Entry<Integer, Double> entry :entryTimeDistribution.entrySet())
		{	
			aveTime+=entry.getKey()*entry.getValue();
		}
		return 1.0/(1+aveTime);
	}


	public double getActionA(int actionCode) {
		return acas.getActionA(actionCode);
	}


	public double getActionV(int actionCode) {
		return acas.getActionV(actionCode);
	}


	public void init() {
		
	}	
	
}
