/**
 * 
 */
package acasx3d.generation;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author viki
 *
 */
public class MDP
{
	//"COC"-->0,"CL25"-->1, "DES25"-->2
	//"Loop"-->-1
	
	public static final double UPPER_H=600.0;
	public static final double UPPER_VY=70.0;
	
	public static final int nh = 10;//10
	public static final int noVy=7;//7
	public static final int niVy= 7;//7
	public static final int nra=7;
	
	public static final double hRes = UPPER_H/nh;
	public static final double oVRes = UPPER_VY/noVy;
	public static final double iVRes = UPPER_VY/niVy;

	private final int numCStates= (2*nh+1)*(2*noVy+1)*(2*niVy+1)*nra;
	private State_Ctrl[] cStates= new State_Ctrl[numCStates];
	
	public final static double WHITE_NOISE_SDEV=3.0;
	private ArrayList<ThreeTuple<Double, Double, Double>> sigmaPointsA = new ArrayList<>();
	private ArrayList<ThreeTuple<Double, Double, Double>> sigmaPointsB = new ArrayList<>();

	
	public MDP() 
	{		
		for(int hIdx=-nh; hIdx<=nh;hIdx++)//
		{
			for(int oVyIdx=-noVy; oVyIdx<=noVy;oVyIdx++)//
			{
				for(int iVyIdx=-niVy; iVyIdx<=niVy;iVyIdx++)
				{
					for(int raIdx=0; raIdx<nra;raIdx++)//
					{
						State_Ctrl state = new State_Ctrl(hIdx, oVyIdx, iVyIdx, raIdx);							
						cStates[state.getOrder()]=state;		
					}	
				}
			}
		}
				
		sigmaPointsA.add(new ThreeTuple<>(0.0,0.0,1.0/2));
		sigmaPointsA.add(new ThreeTuple<>(0.0,Math.sqrt(2.0)*WHITE_NOISE_SDEV,1.0/4));
		sigmaPointsA.add(new ThreeTuple<>(0.0,-Math.sqrt(2.0)*WHITE_NOISE_SDEV,1.0/4));
			
		sigmaPointsB.add(new ThreeTuple<>(0.0,0.0,1.0/3));
		sigmaPointsB.add(new ThreeTuple<>(Math.sqrt(3.0)*WHITE_NOISE_SDEV,0.0,1.0/6));
		sigmaPointsB.add(new ThreeTuple<>(-Math.sqrt(3.0)*WHITE_NOISE_SDEV,0.0,1.0/6));
		sigmaPointsB.add(new ThreeTuple<>(0.0,Math.sqrt(3.0)*WHITE_NOISE_SDEV,1.0/6));
		sigmaPointsB.add(new ThreeTuple<>(0.0,-Math.sqrt(3.0)*WHITE_NOISE_SDEV,1.0/6));
	}

	/**
	 * Get the set of states associated with the Markov decision process.
	 * 
	 * @return the set of states associated with the Markov decision process.
	 */
	public State_Ctrl[] states()
	{		
		return cStates;
	}


	/**
	 * Get the set of actions for state s.
	 * 
	 * @param s the state.
	 * @return the list of actions for state s.
	 */
	public ArrayList<Integer> actions(State_Ctrl cstate)
	{
		ArrayList<Integer> actions= new ArrayList<Integer>();			

		if(cstate.getH()==UPPER_H || cstate.getoVy()== UPPER_VY)
		{
			actions.add(0);//"COC"
			actions.add(4);//"SDES25"
			return actions;
		}
		if(cstate.getH()==-UPPER_H || cstate.getoVy()== -UPPER_VY)
		{
			actions.add(0);//"COC"
			actions.add(3);//"SCL25"
			return actions;
		}
		else if(cstate.getRa()==0)//COC
		{
			actions.add(0);//"COC"
			actions.add(1);//"CL25"
			actions.add(2);//"DES25"
			return actions;
		}
		else if(cstate.getRa()==1)//CL25
		{
			actions.add(0);//"COC"
			actions.add(1);//"CL25"
			actions.add(4);//"SDES25"
			actions.add(5);//"SCL42"
			return actions;
		}
		else if(cstate.getRa()==2)//DES25
		{
			actions.add(0);//"COC"
			actions.add(2);//"DES25"
			actions.add(3);//"SCL25"
			actions.add(6);//"SDES42"	
			return actions;
		}
		else if(cstate.getRa()==3)//"SCL25"
		{
			actions.add(0);//"COC"
			actions.add(3);//"SCL25"
			actions.add(4);//"SDES25"
			actions.add(5);//"SCL42"	
			return actions;
		}
		else if(cstate.getRa()==4)//"SDES25"
		{
			actions.add(0);//"COC"
			actions.add(3);//"SCL25"
			actions.add(4);//"SDES25"
			actions.add(6);//"SDES42"	
			return actions;
		}
		else if(cstate.getRa()==5)//"SCL42"
		{
			actions.add(0);//"COC"
			actions.add(3);//"SCL25"
			actions.add(4);//"SDES25"
			actions.add(5);//"SCL42"
			return actions;
		}
		else if(cstate.getRa()==6)//"SDES42"
		{
			actions.add(0);//"COC"
			actions.add(3);//"SCL25"	
			actions.add(4);//"SDES25"	
			actions.add(6);//"SDES42"
			return actions;
		}
							
		System.err.println("Something wrong happend in ACASXMDP.actions(State s).");
		return null;
	}
	
	public Map<State_Ctrl,Double> getTransitionStatesAndProbs(State_Ctrl cstate, int actionCode)
	{
		Map<State_Ctrl, Double> TransitionStatesAndProbs = new LinkedHashMap<State_Ctrl,Double>();

		double targetV=Utils.getActionV(actionCode);
		double accel=Utils.getActionA(actionCode);
		ArrayList<AbstractMap.SimpleEntry<State_Ctrl, Double>> nextStateMapProbabilities = new ArrayList<>();
		
		if( (accel>0 && targetV>cstate.getoVy() && cstate.getoVy()<UPPER_VY)
				|| (accel<0 && targetV<cstate.getoVy() && cstate.getoVy()>-UPPER_VY))
		{// own aircraft follows a RA other than COC			
			
			for(ThreeTuple<Double, Double, Double> sigmaPoint : sigmaPointsA)
			{
				double oAy=accel;
				double iAy=sigmaPoint.x2;
				double sigmaP=sigmaPoint.x3;
				
				double hP= cstate.getH()+ (cstate.getiVy()-cstate.getoVy()) + 0.5*(iAy-oAy);
				double oVyP= Math.max(-UPPER_VY, Math.min(UPPER_VY, cstate.getoVy()+oAy));
				double iVyP= Math.max(-UPPER_VY, Math.min(UPPER_VY, cstate.getiVy()+iAy));
				int raP=actionCode;
				
				int hIdxL = (int)Math.floor(hP/hRes);
				int oVyIdxL = (int)Math.floor(oVyP/oVRes);
				int iVyIdxL = (int)Math.floor(iVyP/iVRes);
				for(int i=0;i<=1;i++)
				{
					int hIdx = (i==0? hIdxL : hIdxL+1);
					int hIdxP= hIdx< -nh? -nh: (hIdx>nh? nh : hIdx);			
					for(int j=0;j<=1;j++)
					{
						int oVzIdx = (j==0? oVyIdxL : oVyIdxL+1);
						int oVzIdxP= oVzIdx<-noVy? -noVy: (oVzIdx>noVy? noVy : oVzIdx);
						for(int k=0;k<=1;k++)
						{
							int iVzIdx = (k==0? iVyIdxL : iVyIdxL+1);
							int iVzIdxP= iVzIdx<-niVy? -niVy: (iVzIdx>niVy? niVy : iVzIdx);
							
							State_Ctrl nextState= new State_Ctrl(hIdxP, oVzIdxP, iVzIdxP, raP);
							double probability= sigmaP*(1-Math.abs(hIdx-hP/hRes))*(1-Math.abs(oVzIdx-oVyP/oVRes))*(1-Math.abs(iVzIdx-iVyP/iVRes));
							nextStateMapProbabilities.add(new SimpleEntry<State_Ctrl, Double>(nextState,probability) );
						}
					}
				}

			}			
			
		}
		else
		{				
			for(ThreeTuple<Double, Double, Double> sigmaPoint : sigmaPointsB)
			{
				double oAy=sigmaPoint.x1;
				double iAy=sigmaPoint.x2;
				double sigmaP=sigmaPoint.x3;
				
				double hP= cstate.getH()+ (cstate.getiVy()-cstate.getoVy()) + 0.5*(iAy-oAy);
				double oVyP= Math.max(-UPPER_VY, Math.min(UPPER_VY, cstate.getoVy()+oAy));
				double iVyP= Math.max(-UPPER_VY, Math.min(UPPER_VY, cstate.getiVy()+iAy));
				int raP=actionCode;

				int hIdxL = (int)Math.floor(hP/hRes);
				int oVyIdxL = (int)Math.floor(oVyP/oVRes);
				int iVyIdxL = (int)Math.floor(iVyP/iVRes);
				for(int i=0;i<=1;i++)
				{
					int hIdx = (i==0? hIdxL : hIdxL+1);
					int hIdxP= hIdx< -nh? -nh: (hIdx>nh? nh : hIdx);			
					for(int j=0;j<=1;j++)
					{
						int oVyIdx = (j==0? oVyIdxL : oVyIdxL+1);
						int oVyIdxP= oVyIdx<-noVy? -noVy: (oVyIdx>noVy? noVy : oVyIdx);
						for(int k=0;k<=1;k++)
						{
							int iVyIdx = (k==0? iVyIdxL : iVyIdxL+1);
							int iVyIdxP= iVyIdx<-niVy? -niVy: (iVyIdx>niVy? niVy : iVyIdx);
							
							State_Ctrl nextState= new State_Ctrl(hIdxP, oVyIdxP, iVyIdxP,raP);
							double probability= sigmaP*(1-Math.abs(hIdx-hP/hRes))*(1-Math.abs(oVyIdx-oVyP/oVRes))*(1-Math.abs(iVyIdx-iVyP/iVRes));
							nextStateMapProbabilities.add(new SimpleEntry<State_Ctrl, Double>(nextState,probability) );
						}
					}
				}	
				
			}			
			
		}

		for(AbstractMap.SimpleEntry<State_Ctrl, Double> nextStateMapProb :nextStateMapProbabilities)
		{	
			State_Ctrl nextState=nextStateMapProb.getKey();
			if(TransitionStatesAndProbs.containsKey(nextState))
			{				
				TransitionStatesAndProbs.put(nextState, TransitionStatesAndProbs.get(nextState)+nextStateMapProb.getValue());
			}
			else
			{
				TransitionStatesAndProbs.put(nextState, nextStateMapProb.getValue());
			}		
			
		}
		
		return TransitionStatesAndProbs;
	}
	
		
	public double reward(State_Ctrl cstate,int actionCode)
	{
		if(actionCode==-1)//terminate 
		{
			if(Math.abs(cstate.getH())<100)
			{//NMAC
				return -10000;
			}
			else
			{
				return 0;
			}			
		}
		
		if(actionCode==1)
		{
			if(cstate.getoVy()>0)
			{
				return -50;
			}
			else if(cstate.getoVy()<0)
			{
				return -100;
			}
			
		}
		if(actionCode==2)
		{
			if(cstate.getoVy()>0)
			{
				return -100;
			}
			else if(cstate.getoVy()<0)
			{
				return -50;
			}
		}
				
		if(actionCode==0)//"COC"
		{//clear of conflict
			return 100;
		}
		
		if( (cstate.getRa()==1||cstate.getRa()==3)&& actionCode==5
				|| (cstate.getRa()==2||cstate.getRa()==4)&& actionCode==6)
		{//strengthening
			return -500;
		}
		
		if( (cstate.getRa()==1 || cstate.getRa()==3 || cstate.getRa()==5)&& actionCode==4 
				|| (cstate.getRa()==2 || cstate.getRa()==4 || cstate.getRa()==6)&& actionCode==3 )
		{//reversal
			return -1000;
		}
		
		return 0;
	}

}

class ThreeTuple<X1, X2, X3> 
{ 
	  public final X1 x1; 
	  public final X2 x2; 
	  public final X3 x3; 
	  public ThreeTuple(X1 x1, X2 x2, X3 x3) 
	  { 
	    this.x1 = x1; 
	    this.x2 = x2; 
	    this.x3 = x3;
	  } 
} 
