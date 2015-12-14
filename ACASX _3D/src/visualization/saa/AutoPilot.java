package visualization.saa;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.Double3D;
import visualization.configuration.Configuration;
import visualization.modeling.SAAModel;
import visualization.modeling.env.Waypoint;
import visualization.modeling.uas.UAS;
import visualization.modeling.uas.UASPerformance;

public class AutoPilot implements Steppable
{
	private static final long serialVersionUID = 1L;
	
	private SAAModel state; 
	private UAS hostUAS;
	
	private UASPerformance uasPerformance;
	private double SDX=0;
	private double SDY=0;
	private double SDZ=0;
	
	private String normativeMode=null;//normative manoeuvre normativeMode
	private int acasxActionCode=-999;//action code from collision avoidance algorithm

	

	public AutoPilot(SimState simstate, UAS uas, Boolean enableWhiteNoise, String mode) 
	{
		state = (SAAModel)simstate;
		hostUAS = uas;	

		uasPerformance=hostUAS.getUasPerformance();
		if(enableWhiteNoise)
		{
			this.SDX=uasPerformance.getStdDevX();
			this.SDY=uasPerformance.getStdDevY();
			this.SDZ=uasPerformance.getStdDevZ();
		}		
		
		normativeMode=mode;
	}


	public void step(SimState simState) 
	{
		if(hostUAS.activeState==0)
		{	
			if(Configuration.getInstance().globalConfig.collisionAvoidanceEnabler && acasxActionCode>=0)
			{
				hostUAS.setApNextWp(executeActionCode(acasxActionCode));				
			}			
			else if(normativeMode=="WhiteNoise")
			{
				hostUAS.setApNextWp(executeWhiteNoise());
			}
			else
			{
				System.err.println("Something wrong with public class AutoPilot implements Steppable/public void step(SimState simState) ");
			}
			
		}		
	}

	private Waypoint executeActionCode(int actionCode)
	{			
		double ay=hostUAS.getAcasx().getActionA(actionCode);		
		double ax=SDX* state.random.nextGaussian();
		double az=SDZ* state.random.nextGaussian();
		double vx=hostUAS.getVel().x;
		double vy=hostUAS.getVel().y;
		double vz=hostUAS.getVel().z;

		double targetV= hostUAS.getAcasx().getActionV(actionCode);
		if(actionCode==0)
		{		
			ay = SDY* state.random.nextGaussian();
		}
		else if(ay>0 && targetV<vy)
		{
			ay = -Math.abs(SDY* state.random.nextGaussian());
		}
		else if(ay<0 && targetV>vy)
		{
			ay = Math.abs(SDY* state.random.nextGaussian());
		}

		Double2D groundVelocity = new Double2D(vx+ax,vz+az);
		if(groundVelocity.length()>uasPerformance.getMaxGS())
		{
			groundVelocity= groundVelocity.resize(uasPerformance.getMaxGS());
		}
		else if(groundVelocity.length()<uasPerformance.getMinGS())
		{
			groundVelocity= groundVelocity.resize(uasPerformance.getMinGS());
		}
		
		double newVy=vy+ay;
		if(newVy>uasPerformance.getMaxVS())
		{
			newVy=uasPerformance.getMaxVS();
		}
		else if(newVy<-uasPerformance.getMaxVS())
		{
			newVy=-uasPerformance.getMaxVS();
		}
		
		double x= hostUAS.getLoc().x + 0.5*(vx+groundVelocity.x);				
		double y= hostUAS.getLoc().y + 0.5*(vy + newVy);
		double z= hostUAS.getLoc().z + 0.5*(vz+groundVelocity.y);
		hostUAS.setOldVel(new Double3D(vx,vy,vz));
		hostUAS.setVel(new Double3D(groundVelocity.x, newVy,groundVelocity.y));
		Waypoint wp = new Waypoint(new Double3D(x, y,z));	
		wp.setAction(actionCode+30);//30 for ACASX3D
		return wp;
	}
	

	public Waypoint executeWhiteNoise()
	{		
		double vx=hostUAS.getVel().x;
		double vy=hostUAS.getVel().y;
		double vz=hostUAS.getVel().z;
		double ax = SDX * state.random.nextGaussian();
		double ay = SDY * state.random.nextGaussian();
		double az = SDZ * state.random.nextGaussian();
		Double2D groundVelocity = new Double2D(vx+ax,vz+az);
		if(groundVelocity.length()>uasPerformance.getMaxGS())
		{
			groundVelocity= groundVelocity.resize(uasPerformance.getMaxGS());
		}
		else if(groundVelocity.length()<uasPerformance.getMinGS())
		{
			groundVelocity= groundVelocity.resize(uasPerformance.getMinGS());
		}
		
		double newVy=vy+ay;
		if(newVy>uasPerformance.getMaxVS())
		{
			newVy=uasPerformance.getMaxVS();
		}
		else if(newVy<-uasPerformance.getMaxVS())
		{
			newVy=-uasPerformance.getMaxVS();
		}
		
		double x= hostUAS.getLoc().x + 0.5*(vx+groundVelocity.x);				
		double y= hostUAS.getLoc().y + 0.5*(vy + newVy);
		double z= hostUAS.getLoc().z + 0.5*(vz+groundVelocity.y);
		hostUAS.setOldVel(new Double3D(vx,vy,vz));
		hostUAS.setVel(new Double3D(groundVelocity.x, newVy,groundVelocity.y));
		Waypoint wp = new Waypoint(new Double3D(x , y, z));		
		return wp;
	}
	
	public String getNormativeMode() {
		return normativeMode;
	}


	public void setNormativeMode(String normativeMode) {
		this.normativeMode = normativeMode;
	}
	
	public int getActionCode() {
		return acasxActionCode;
	}
	public void setActionCode(int actionCode) {
		this.acasxActionCode = actionCode;
	}	
}
