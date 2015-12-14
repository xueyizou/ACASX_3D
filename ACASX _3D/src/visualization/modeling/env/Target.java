package visualization.modeling.env;

import sim.util.Double3D;

public class Target
{	
	private Double3D loc;

	public Target(Double3D loc)
	{
		this.loc=loc;
	}
	
	public String toString()
	{
		return "Target"+this.loc.toString();
	}

	public Double3D getLoc() {
		return loc;
	}
	
	public void setLoc(Double3D loc)
	{
		this.loc=loc;
	}

}
