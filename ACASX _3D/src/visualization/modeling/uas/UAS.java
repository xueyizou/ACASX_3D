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
package visualization.modeling.uas;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.Double3D;
import visualization.modeling.SAAModel;
import visualization.modeling.env.Waypoint;
import visualization.saa.ACASX3DProxy;
import visualization.saa.AutoPilot;

/**
 *
 * @author Xueyi Zou
 */
public class UAS implements Steppable
{
	private static final long serialVersionUID = 1L;
	
	private String alias;
	//parameters for subsystems	
	private ACASX3DProxy acasx;
	private AutoPilot ap;

	//parameters for UAS movement	
	private Double3D oldLoc;
	private Double3D loc;
	private Double3D oldVel;		
	private Double3D vel;		

	//the set performance for the uas.
	private UASPerformance uasPerformance;	
	
	//parameters for navigation
	private Waypoint nextWp;
	private Waypoint apNextWp = null;//for auto-pilot
	
/*************************************************************************************************/
	//parameters for recording information about simulation
	private Proximity tempProximity = new Proximity(Double.MAX_VALUE,Double.MAX_VALUE); //records the closest distance to danger in each step
	private Proximity minProximity = new Proximity(Double.MAX_VALUE,Double.MAX_VALUE);//records the closest distance to danger experienced by the uas
	
/*************************************************************************************************/

	public int activeState;	//-1:collision, 0:active, 1:arrived target

	private SAAModel state;	


	public UAS(Double3D location, Double3D velocity, UASPerformance uasPerformance)
	{
		this.activeState=0;
		
		this.loc=location;		
		this.vel = velocity; 
		this.uasPerformance = uasPerformance;
		this.oldVel= velocity;
		this.oldLoc= location;
		
		nextWp=null;		
	}
	
	public void init(AutoPilot ap,ACASX3DProxy acasx)
	{
		this.ap = ap;
		this.acasx = acasx;		
	}
	

	@Override
	public void step(SimState simState)
	{
		state = (SAAModel) simState;		
		if(this.activeState == 0)
		{				
			if(apNextWp != null)
			{
				Double3D diff = apNextWp.getLoc().subtract(this.loc);
				Double2D velH=new Double2D(diff.x, diff.z);
				double gs= Math.sqrt(diff.x*diff.x + diff.z*diff.z);
				double velV = diff.y;
	
				if(gs>this.uasPerformance.getMaxGS())
				{
					velH=velH.resize(this.uasPerformance.getMaxGS());
				}
				if(velV>this.uasPerformance.getMaxVS())
				{
					velV=this.uasPerformance.getMaxVS();
				}
				if(velV<this.uasPerformance.getMinVS())
				{
					velV=this.uasPerformance.getMinVS();
				}
				nextWp = apNextWp;
				nextWp.setLoc(this.loc.add(new Double3D(velH.x, velV, velH.y)));
				state.environment3D.setObjectLocation(nextWp, nextWp.getLoc());
		
				this.setOldLoc(this.loc);
				this.setLoc(nextWp.getLoc());
				state.environment3D.setObjectLocation(this, this.loc);		
			}			
			else
			{
				System.out.println("approaching the destination (impossible)!");
			}
//			
//			if(loc.distanceSq(target.getLoc())<=5*5)
//			{
//				this.activeState=1;
//			}
			
		}	
		
    }


//**************************************************************************
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	
	public AutoPilot getAp() {
		return ap;
	}

	public void setAp(AutoPilot ap) {
		this.ap = ap;
	}
	
	public ACASX3DProxy getAcasx() {
		return acasx;
	}

	public void setAcasx(ACASX3DProxy acasx) {
		this.acasx = acasx;
	}
	
	public UASPerformance getUasPerformance() {
		return uasPerformance;
	}

	public void setUasPerformance(UASPerformance performance) {
		this.uasPerformance = performance;
	}
	
	public Double3D getOldVel() {
		return oldVel;
	}
	public void setOldVel(Double3D velocity) {
		oldVel=velocity;
	}

	public Double3D getVel() {
		return vel;
	}
	public void setVel(Double3D velocity) {
		vel=velocity;
	}

	public Double3D getOldLoc() {
		return oldLoc;
	}

	public void setOldLoc(Double3D oldLocation) {
		this.oldLoc = oldLocation;
	}
	
	public Double3D getLoc() {
		return loc;
	}

	public void setLoc(Double3D location) {
		this.loc = location;
	}

	public Proximity getTempProximity() {
		return tempProximity;
	}

	public void setTempProximity(Proximity tempProximity) {
		this.tempProximity = tempProximity;
	}

	public Proximity getMinProximity() {
		return minProximity;
	}

	public void setMinProximity(Proximity minProximity) {
		this.minProximity = minProximity;
	}

	public void setApNextWp(Waypoint apWp) {
		this.apNextWp = apWp;
	}


}
