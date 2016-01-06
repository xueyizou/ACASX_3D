package acasx3d;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import sim.util.Double2D;
import sim.util.Double3D;
import acasx3d.generation.State_Ctrl;
import acasx3d.generation.DTMC;
import acasx3d.generation.MDP;
import acasx3d.generation.State_UCtrl;
import acasx3d.generation.Utils;
import acasx3d.generation.MDPVI;

/**
 * @author Xueyi
 *
 */
public class ACASX3D_MultiThreats 
{
	private LookupTable3D lookupTable3D;
	
	private int lastRA;
	private Double3D ownshipLoc;
	private Double3D ownshipVel;
	private Double3D[] intrudersLocs;
	private Double3D[] intrudersVels;
	
	public ACASX3D_MultiThreats()
	{
		lookupTable3D=LookupTable3D.getInstance();			
	}	
	
	public void update(Double3D ownshipLoc, Double3D ownshipVel, Double3D[] intrudersLocs, Double3D[] intrudersVels, int lastRA) 
	{
		if(intrudersLocs.length!=intrudersVels.length)
		{
			System.err.println("the size of intrudersLocs should be equal to the size of intrudersVels!");
			return;
		}
		this.ownshipLoc = ownshipLoc;	
		this.ownshipVel= ownshipVel;
		this.intrudersLocs = intrudersLocs;	
		this.intrudersVels= intrudersVels;
		this.lastRA = lastRA;
	}
	
	public int execute()
	{
		Map<Integer, Double> qValuesMap = new TreeMap<>();
		for(int i=0; i<intrudersLocs.length; ++i)
		{
			Double2D vctDistance = new Double2D(intrudersLocs[i].x-ownshipLoc.x, intrudersLocs[i].z-ownshipLoc.z);
			double r=vctDistance.length();
			double h=(intrudersLocs[i].y-ownshipLoc.y);			
			
			if(Math.abs(h)<=MDP.UPPER_H && r<=DTMC.UPPER_R)
			{
				Map<Integer, Double> entryTimeDistribution =calculateEntryTimeDistributionDTMC(intrudersLocs[i],intrudersVels[i]);						
				Map<Integer, Double> qValuesMap2 = calculateQValuesMap(intrudersLocs[i],intrudersVels[i], entryTimeDistribution);
				for (Integer action : qValuesMap2.keySet()) 
				{
					if(qValuesMap.containsKey(action))
					{
						double a=qValuesMap.get(action);
						double b=qValuesMap2.get(action);
						qValuesMap.put(action, Math.min(a, b));
//						qValuesMap.put(action, a+b);
					}
					else
					{
						qValuesMap.put(action, qValuesMap2.get(action));						
					}
				}						
			}
			else
			{
				continue;			
			}		
		}		
		
		
		double maxQValue=Double.NEGATIVE_INFINITY;
		int bestActionCode=-1;
		Set<Entry<Integer,Double>> entrySet = qValuesMap.entrySet();
		for (Entry<Integer,Double> entry : entrySet) 
		{
			double value=entry.getValue();
			if(value-maxQValue>=0.0001)
			{
				maxQValue=value;
				bestActionCode=entry.getKey();
			}
		}	
		lastRA=bestActionCode;	
		return lastRA;
	}

	private Map<Integer, Double> calculateEntryTimeDistributionDTMC(Double3D intruderLoc, Double3D intruderVel)
	{
		return calculateEntryTimeDistributionDTMC(ownshipLoc, ownshipVel, intruderLoc, intruderVel);

	}
	
	public Map<Integer, Double> calculateEntryTimeDistributionDTMC(Double3D ownshipLoc, Double3D ownshipVel, Double3D intruderLoc, Double3D intruderVel)
	{
		Double2D vctDistance = new Double2D(intruderLoc.x-ownshipLoc.x, intruderLoc.z-ownshipLoc.z);
		Double2D vctVelocity = new Double2D(intruderVel.x-ownshipVel.x, intruderVel.z-ownshipVel.z);
		double r=vctDistance.length();
		double rv=vctVelocity.length();
		double theta_in_rad=vctVelocity.angle()-vctDistance.angle();
		if(theta_in_rad> Math.PI)
 	   	{
			theta_in_rad= -2*Math.PI +theta_in_rad; 
 	   	}
		if(theta_in_rad<-Math.PI)
 	   	{
			theta_in_rad=2*Math.PI+theta_in_rad; 
 	   	}
		double theta = Math.toDegrees(theta_in_rad);
		
		double rRes=DTMC.rRes;
		double rvRes=DTMC.rvRes;
		double thetaRes=DTMC.thetaRes;
		
		ArrayList<AbstractMap.SimpleEntry<Integer, Double>> entryTimeMapProbs = new ArrayList<AbstractMap.SimpleEntry<Integer, Double>>();
		Map<Integer, Double> entryTimeDistribution = new TreeMap<>();// must be a sorted map

		assert (r<=DTMC.UPPER_R);
		assert (rv<=DTMC.UPPER_RV);
		assert (theta>=-180 && theta<=180);
	
		int rIdxL = (int)Math.floor(r/rRes);
		int rvIdxL = (int)Math.floor(rv/rvRes);
		int thetaIdxL = (int)Math.floor(theta/thetaRes);
		for(int i=0;i<=1;i++)
		{
			int rIdx = (i==0? rIdxL : rIdxL+1);
			int rIdxP= rIdx< 0? 0: (rIdx>DTMC.nr? DTMC.nr : rIdx);			
			for(int j=0;j<=1;j++)
			{
				int rvIdx = (j==0? rvIdxL : rvIdxL+1);
				int rvIdxP= rvIdx<0? 0: (rvIdx>DTMC.nrv? DTMC.nrv : rvIdx);
				for(int k=0;k<=1;k++)
				{
					int thetaIdx = (k==0? thetaIdxL : thetaIdxL+1);
					int thetaIdxP= thetaIdx<-DTMC.ntheta? -DTMC.ntheta: (thetaIdx>DTMC.ntheta? DTMC.ntheta : thetaIdx);
					
					State_UCtrl approxUState= new State_UCtrl(rIdxP, rvIdxP, thetaIdxP);
					int approxUStateOrder = approxUState.getOrder();
					double probability= (1-Math.abs(rIdx-r/rRes))*(1-Math.abs(rvIdx-rv/rvRes))*(1-Math.abs(thetaIdx-theta/thetaRes));
					for(int t=0;t<=MDPVI.T;t++)
					{
						entryTimeMapProbs.add(new SimpleEntry<Integer, Double>(t,probability*lookupTable3D.entryTimeDistributionArr.get((t*lookupTable3D.numUStates)+ approxUStateOrder)) );
					}
					
				}
			}
		}
		double entryTimeLessThanTProb=0;
		for(AbstractMap.SimpleEntry<Integer, Double> entryTime_prob :entryTimeMapProbs)
		{			
			if(entryTimeDistribution.containsKey(entryTime_prob.getKey()))
			{
				entryTimeDistribution.put(entryTime_prob.getKey(), entryTimeDistribution.get(entryTime_prob.getKey())+entryTime_prob.getValue());
			}
			else
			{
				entryTimeDistribution.put(entryTime_prob.getKey(), entryTime_prob.getValue());
			}
			entryTimeLessThanTProb+=entryTime_prob.getValue();
		}
		entryTimeDistribution.put(MDPVI.T+1, 1-entryTimeLessThanTProb);
		return entryTimeDistribution;
	}
	
	private Map<Integer, Double> calculateQValuesMap(Double3D intruderLoc, Double3D intruderVel, Map<Integer, Double> entryTimeDistribution)
	{
		double h=(intruderLoc.y-ownshipLoc.y);
		double oVy=ownshipVel.y;
		double iVy=intruderVel.y;
//		state.information=String.format( "(%.1f, %.1f, %.1f, %d)",h,oVy,iVy,ra);
		
		double hRes=MDP.hRes;
		double oVRes=MDP.oVRes;
		double iVRes=MDP.iVRes;
		
		assert (Math.abs(h)<=MDP.UPPER_H);
		assert (Math.abs(oVy)<=MDP.UPPER_VY);
		assert (Math.abs(iVy)<=MDP.UPPER_VY);
		assert (lastRA>=0);
		
		Map<Integer, Double> qValuesMap = new TreeMap<>();
		ArrayList<AbstractMap.SimpleEntry<Integer, Double>> actionMapValues = new ArrayList<AbstractMap.SimpleEntry<Integer, Double>>();

		int hIdxL = (int)Math.floor(h/hRes);
		int oVyIdxL = (int)Math.floor(oVy/oVRes);
		int iVyIdxL = (int)Math.floor(iVy/iVRes);
		for(int i=0;i<=1;i++)
		{
			int hIdx = (i==0? hIdxL : hIdxL+1);
			int hIdxP= hIdx< -MDP.nh? -MDP.nh: (hIdx>MDP.nh? MDP.nh : hIdx);			
			for(int j=0;j<=1;j++)
			{
				int oVyIdx = (j==0? oVyIdxL : oVyIdxL+1);
				int oVyIdxP= oVyIdx<-MDP.noVy? -MDP.noVy: (oVyIdx>MDP.noVy? MDP.noVy : oVyIdx);
				for(int k=0;k<=1;k++)
				{
					int iVyIdx = (k==0? iVyIdxL : iVyIdxL+1);
					int iVyIdxP= iVyIdx<-MDP.niVy? -MDP.niVy: (iVyIdx>MDP.niVy? MDP.niVy : iVyIdx);
					
					State_Ctrl approxCState= new State_Ctrl(hIdxP, oVyIdxP, iVyIdxP, lastRA);
					int approxCStateOrder = approxCState.getOrder();
					double probability= (1-Math.abs(hIdx-h/hRes))*(1-Math.abs(oVyIdx-oVy/oVRes))*(1-Math.abs(iVyIdx-iVy/iVRes));

					for(Entry<Integer, Double> entryTime_prob :entryTimeDistribution.entrySet())
					{
						int t=entryTime_prob.getKey();
						double entryTimeProb= entryTime_prob.getValue();					

						int index=0, numActions=0;
						try
						{
							index =lookupTable3D.indexArr.get((t*lookupTable3D.numCStates)+ approxCStateOrder);
							numActions = lookupTable3D.indexArr.get((t*lookupTable3D.numCStates)+approxCStateOrder+1)-index;	
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							System.out.println((t*lookupTable3D.numCStates)+"    "+ approxCState+"     "+approxCStateOrder);
							System.exit(-1);
						}
														
						for (int n=0;n<numActions;n++) 
						{
							double qValue= lookupTable3D.costArr.get(index+n);
							int actionCode= lookupTable3D.actionArr.get(index+n);							
							actionMapValues.add(new SimpleEntry<Integer, Double>(actionCode,probability*entryTimeProb*qValue) );
						}			
					}														
				}
			}
		}
					
		for(AbstractMap.SimpleEntry<Integer, Double> action_value :actionMapValues)
		{
			if(qValuesMap.containsKey(action_value.getKey()))
			{
				qValuesMap.put(action_value.getKey(), qValuesMap.get(action_value.getKey())+action_value.getValue());
			}
			else
			{
				qValuesMap.put(action_value.getKey(), action_value.getValue());
			}
		}
		
		return qValuesMap;
	}
	
	public double getActionV(int actionCode)
	{
		return Utils.getActionV(actionCode);
	
	}
	
	public double getActionA(int actionCode)
	{
		return Utils.getActionA(actionCode);
	
	}
	
}
