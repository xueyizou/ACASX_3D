package acasx3d.generation;

public class ACASX3DCState//controlled state
{
	private final double h;
	private final double oVy;
	private final double iVy;
	private final int ra;
	private final int hashCode;
	private final int order;
	
	public ACASX3DCState(int hIdx, int oVyIdx, int iVyIdx, int raIdx)
	{ 

		this.h= ACASX3DMDP.hRes*hIdx;
		this.oVy= ACASX3DMDP.oVRes*oVyIdx;
		this.iVy = ACASX3DMDP.iVRes*iVyIdx;
		this.ra = raIdx;	
		
		int a= hIdx +ACASX3DMDP.nh;
		int b= oVyIdx +ACASX3DMDP.noVy;		
		int c= iVyIdx +ACASX3DMDP.niVy;

		this.order=a*(2*ACASX3DMDP.noVy+1)*(2*ACASX3DMDP.niVy+1)*(ACASX3DMDP.nra)
					+ b*(2*ACASX3DMDP.niVy+1)*(ACASX3DMDP.nra)
					+ c*(ACASX3DMDP.nra)
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
		
		if(obj !=null && obj.getClass()==ACASX3DCState.class)
		{
			ACASX3DCState state = (ACASX3DCState)obj;
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
