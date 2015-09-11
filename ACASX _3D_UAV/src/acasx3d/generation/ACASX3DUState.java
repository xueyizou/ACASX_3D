package acasx3d.generation;

public class ACASX3DUState//uncontrolled state
{
	private final double r;
	private final double rv;
	private final double theta;
	private final int order;
	private final int hashCode;
	
	public ACASX3DUState(int rIdx, int rvIdx, int thetaIdx)
	{ 

		this.r= ACASX3DDTMC.rRes*rIdx;
		this.rv= ACASX3DDTMC.rvRes*rvIdx;
		this.theta = ACASX3DDTMC.thetaRes*thetaIdx;

		this.order=rIdx*(ACASX3DDTMC.nrv+1)*(2*ACASX3DDTMC.ntheta+1)
				+ rvIdx*(2*ACASX3DDTMC.ntheta+1)
				+ (thetaIdx+ACASX3DDTMC.ntheta);
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
		
		if(obj !=null && obj.getClass()==ACASX3DUState.class)
		{
			ACASX3DUState state = (ACASX3DUState)obj;
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
