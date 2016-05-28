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
package acasx3d.generation;

public class State_Ctrl
{
	private final double h;
	private final double oVy;
	private final double iVy;
	private final int ra;
	private final int hashCode;
	private final int order;
	
	public State_Ctrl(int hIdx, int oVyIdx, int iVyIdx, int raIdx)
	{ 

		this.h= MDP.hRes*hIdx;
		this.oVy= MDP.oVRes*oVyIdx;
		this.iVy = MDP.iVRes*iVyIdx;
		this.ra = raIdx;	
		
		int a= hIdx +MDP.nh;
		int b= oVyIdx +MDP.noVy;		
		int c= iVyIdx +MDP.niVy;

		this.order=a*(2*MDP.noVy+1)*(2*MDP.niVy+1)*(MDP.nra)
					+ b*(2*MDP.niVy+1)*(MDP.nra)
					+ c*(MDP.nra)
					+ ra;

		this.hashCode=order;
	}
	
	public double getH() {
		return h;
	}



	public double getoVy() {
		return oVy;
	}



	public double getiVy() {
		return iVy;
	}


	public int getRa() {
		return ra;
	}

	public boolean equals(Object obj)
	{
		if (this==obj)
		{
			return true;
		}
		
		if(obj !=null && obj.getClass()==State_Ctrl.class)
		{
			State_Ctrl state = (State_Ctrl)obj;
			if(this.getH()==state.getH()
				&& this.getoVy()==state.getoVy()
				&& this.getiVy() == state.getiVy()
				&& this.getRa()==state.getRa())
			{
				return true;
			}
		}
		return false;
	}
	
	public int hashCode()
	{
		return this.hashCode;
	}
	
	public String toString()
	{
		return "("+h+","+oVy+","+iVy+","+ra+")";
	}

	public int getOrder() {
		return order;
	}

}
