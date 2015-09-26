package acasx3d.generation;

public class State_UCtrl
{
	private final double r;
	private final double rv;
	private final double theta;
	private final int order;
	private final int hashCode;
	
	public State_UCtrl(int rIdx, int rvIdx, int thetaIdx)
	{ 

		this.r= DTMC.rRes*rIdx;
		this.rv= DTMC.rvRes*rvIdx;
		this.theta = DTMC.thetaRes*thetaIdx;

		this.order=rIdx*(DTMC.nrv+1)*(2*DTMC.ntheta+1)
				+ rvIdx*(2*DTMC.ntheta+1)
				+ (thetaIdx+DTMC.ntheta);
		this.hashCode=order;
	}


	public double getR() {
		return r;
	}

	public double getRv() {
		return rv;
	}

	public double getTheta() {
		return theta;
	}

	public boolean equals(Object obj)
	{
		if (this==obj)
		{
			return true;
		}
		
		if(obj !=null && obj.getClass()==State_UCtrl.class)
		{
			State_UCtrl state = (State_UCtrl)obj;
			if(this.getR()==state.getR()
					&& this.getRv()==state.getRv()
					&& this.getTheta() == state.getTheta())
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
		return "("+r+","+rv+","+theta+")";
	}

	public int getOrder() {
		return order;
	}

}
