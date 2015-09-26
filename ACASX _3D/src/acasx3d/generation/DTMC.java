/**
 * 
 */
package acasx3d.generation;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import sim.util.Double2D;

/**
 * @author viki
 *
 */
public class DTMC
{
	public static final double UPPER_R=12200;//20000
	public static final double UPPER_RV=610;//1000
	public static final double UPPER_THETA=180.0;
	
	public static final int nr = 61;//61
	public static final int nrv= 61;//61
	public static final int ntheta= 36;//36
	
	public static final double rRes = UPPER_R/nr;
	public static final double rvRes = UPPER_RV/nrv;
	public static final double thetaRes = UPPER_THETA/ntheta;
	
	public final static double WHITE_NOISE_SDEV=3.0;
	public final static double WHITE_NOISE_SDEV_Angle=2.0;

	private final int numUStates= (nr+1)*(nrv+1)*(2*ntheta+1);
	private State_UCtrl[] uStates= new State_UCtrl[numUStates];
	

	private ArrayList<ThreeTuple<Double, Double, Double>> sigmaPoints1 = new ArrayList<>();
	private ArrayList<ThreeTuple<Double, Double, Double>> sigmaPoints2 = new ArrayList<>();
	
	public DTMC() 
	{		
		for(int rIdx=0; rIdx<=nr;rIdx++)//
		{
			for(int rvIdx=0; rvIdx<=nrv;rvIdx++)//
			{
				for(int thetaIdx=-ntheta; thetaIdx<=ntheta;thetaIdx++)
				{
					State_UCtrl state = new State_UCtrl(rIdx, rvIdx, thetaIdx);							
					uStates[state.getOrder()]=state;						
				}
			}
		}
		
		sigmaPoints1.add(new ThreeTuple<>(0.0,0.0,1.0/3));
		sigmaPoints1.add(new ThreeTuple<>(Math.sqrt(3.0)*2*WHITE_NOISE_SDEV,0.0,1.0/6));
		sigmaPoints1.add(new ThreeTuple<>(-Math.sqrt(3.0)*2*WHITE_NOISE_SDEV,0.0,1.0/6));
		sigmaPoints1.add(new ThreeTuple<>(0.0,Math.sqrt(3.0)*2*WHITE_NOISE_SDEV,1.0/6));
		sigmaPoints1.add(new ThreeTuple<>(0.0,-Math.sqrt(3.0)*2*WHITE_NOISE_SDEV,1.0/6));
		
		sigmaPoints2.add(new ThreeTuple<>(0.0,0.0,1.0/3));
		sigmaPoints2.add(new ThreeTuple<>(Math.sqrt(3.0)*2*WHITE_NOISE_SDEV,0.0,1.0/6));
		sigmaPoints2.add(new ThreeTuple<>(-Math.sqrt(3.0)*2*WHITE_NOISE_SDEV,0.0,1.0/6));
		sigmaPoints2.add(new ThreeTuple<>(0.0,Math.sqrt(3.0)*2*WHITE_NOISE_SDEV_Angle,1.0/6));
		sigmaPoints2.add(new ThreeTuple<>(0.0,-Math.sqrt(3.0)*2*WHITE_NOISE_SDEV_Angle,1.0/6));
	}

	/**
	 * Get the set of states associated with the DTMC. 
	 * @return the set of states associated with the DTMC
	 */
	public State_UCtrl[] states()
	{		
		return uStates;
	}

	/*white noise along r and perpendicular to r*/
	public Map<State_UCtrl,Double> getTransitionStatesAndProbs(State_UCtrl ustate)
	{
		Map<State_UCtrl, Double> TransitionStatesAndProbs = new LinkedHashMap<State_UCtrl,Double>();

		ArrayList<AbstractMap.SimpleEntry<State_UCtrl, Double>> nextStateMapProbabilities = new ArrayList<>();
		
		for(ThreeTuple<Double, Double, Double> sigmaPoint : sigmaPoints1)
		{
			double ra_r=sigmaPoint.x1;
			double ra_pr=sigmaPoint.x2;
			double sigmaP=sigmaPoint.x3;
			
			double r=ustate.getR();
			double rv=ustate.getRv();
			double theta=ustate.getTheta();
						
			Double2D vel= new Double2D(rv*Math.cos(Math.toRadians(theta)), rv*Math.sin(Math.toRadians(theta)));
			Double2D velP= new Double2D(Math.max(-UPPER_RV, Math.min(UPPER_RV, vel.x+ra_r)), Math.max(-UPPER_RV, Math.min(UPPER_RV, vel.y+ra_pr)));
			if(velP.length()>UPPER_RV)
			{
				velP=velP.resize(UPPER_RV);
			}
			double rvP=velP.length();
			
			Double2D pos= new Double2D(r,0);
			Double2D posP= new Double2D(pos.x+0.5*(vel.x+velP.x), pos.y+0.5*(vel.y+velP.y));
			if(posP.length()>UPPER_R)
			{
				posP=posP.resize(UPPER_R);
			}
			double rP=posP.length();
			
			double alpha=velP.angle()-posP.angle();
			if(alpha> Math.PI)
	 	   	{
				alpha= -2*Math.PI +alpha; 
	 	   	}
			if(alpha<-Math.PI)
	 	   	{
				alpha=2*Math.PI+alpha; 
	 	   	}
			double thetaP = Math.toDegrees(alpha);
		

			int rIdxL = (int)Math.floor(rP/rRes);
			int rvIdxL = (int)Math.floor(rvP/rvRes);
			int thetaIdxL = (int)Math.floor(thetaP/thetaRes);
			for(int i=0;i<=1;i++)
			{
				int rIdx = (i==0? rIdxL : rIdxL+1);
				int rIdxP= rIdx< 0? 0: (rIdx>nr? nr : rIdx);			
				for(int j=0;j<=1;j++)
				{
					int rvIdx = (j==0? rvIdxL : rvIdxL+1);
					int rvIdxP= rvIdx<0? 0: (rvIdx>nrv? nrv : rvIdx);
					for(int k=0;k<=1;k++)
					{
						int thetaIdx = (k==0? thetaIdxL : thetaIdxL+1);
						int thetaIdxP= thetaIdx<-ntheta? -ntheta: (thetaIdx>ntheta? ntheta : thetaIdx);
						
						State_UCtrl nextState= new State_UCtrl(rIdxP, rvIdxP, thetaIdxP);
						double probability= sigmaP*(1-Math.abs(rIdx-rP/rRes))*(1-Math.abs(rvIdx-rvP/rvRes))*(1-Math.abs(thetaIdx-thetaP/thetaRes));
						nextStateMapProbabilities.add(new SimpleEntry<State_UCtrl, Double>(nextState,probability) );
					}
				}
			}	
			
		}			

		for(AbstractMap.SimpleEntry<State_UCtrl, Double> nextStateMapProb :nextStateMapProbabilities)
		{	
			State_UCtrl nextState=nextStateMapProb.getKey();
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
	
	/*white noise for relative velocity and turning angle*/
//	public Map<ACASX3DUState,Double> getTransitionStatesAndProbs(ACASX3DUState ustate)
//	{
//		Map<ACASX3DUState, Double> TransitionStatesAndProbs = new LinkedHashMap<ACASX3DUState,Double>();
//
//		ArrayList<AbstractMap.SimpleEntry<ACASX3DUState, Double>> nextStateMapProbabilities = new ArrayList<>();
//		
//		for(ThreeTuple<Double, Double, Double> sigmaPoint : sigmaPoints2)
//		{
//			double ra=sigmaPoint.x1;//relative velocity acceleration
//			double alpha=sigmaPoint.x2;//relative angular acceleration
//			double sigmaP=sigmaPoint.x3;
//			
//			double r=ustate.getR();
//			double rv=ustate.getRv();
//			double theta=ustate.getTheta();
//			
//			double rvP= Math.max(-UPPER_RV, Math.min(UPPER_RV, rv+ra));
//			double thetaP=theta+alpha;
//			if(thetaP> Math.PI)
//	 	   	{
//				thetaP= -2*Math.PI +thetaP; 
//	 	   	}
//			if(thetaP<-Math.PI)
//	 	   	{
//				thetaP=2*Math.PI+thetaP; 
//	 	   	}
//					
//			double rP= Math.max(-UPPER_R, Math.min(UPPER_R,  r+rv*Math.cos(Math.toRadians(theta))));
//			
//			int rIdxL = (int)Math.floor(rP/rRes);
//			int rvIdxL = (int)Math.floor(rvP/rvRes);
//			int thetaIdxL = (int)Math.floor(thetaP/thetaRes);
//			for(int i=0;i<=1;i++)
//			{
//				int rIdx = (i==0? rIdxL : rIdxL+1);
//				int rIdxP= rIdx< 0? 0: (rIdx>nr? nr : rIdx);			
//				for(int j=0;j<=1;j++)
//				{
//					int rvIdx = (j==0? rvIdxL : rvIdxL+1);
//					int rvIdxP= rvIdx<0? 0: (rvIdx>nrv? nrv : rvIdx);
//					for(int k=0;k<=1;k++)
//					{
//						int thetaIdx = (k==0? thetaIdxL : thetaIdxL+1);
//						int thetaIdxP= thetaIdx<-ntheta? -ntheta: (thetaIdx>ntheta? ntheta : thetaIdx);
//						
//						ACASX3DUState nextState= new ACASX3DUState(rIdxP, rvIdxP, thetaIdxP);
//						double probability= sigmaP*(1-Math.abs(rIdx-rP/rRes))*(1-Math.abs(rvIdx-rvP/rvRes))*(1-Math.abs(thetaIdx-thetaP/thetaRes));
//						nextStateMapProbabilities.add(new SimpleEntry<ACASX3DUState, Double>(nextState,probability) );
//					}
//				}
//			}	
//			
//		}			
//
//		for(AbstractMap.SimpleEntry<ACASX3DUState, Double> nextStateMapProb :nextStateMapProbabilities)
//		{	
//			ACASX3DUState nextState=nextStateMapProb.getKey();
//			if(TransitionStatesAndProbs.containsKey(nextState))
//			{				
//				TransitionStatesAndProbs.put(nextState, TransitionStatesAndProbs.get(nextState)+nextStateMapProb.getValue());
//			}
//			else
//			{
//				TransitionStatesAndProbs.put(nextState, nextStateMapProb.getValue());
//			}		
//			
//		}
//		
//		return TransitionStatesAndProbs;
//	}	
	
}
