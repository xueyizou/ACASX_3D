package visualization.modeling.env;

import sim.util.Double3D;


/**
 *
 * @author Xueyi Zou
 */
public class Waypoint
{
	private Double3D loc;
	private int action=-1;
	
	/** Constructor for Waypoint
	 * 
	 * @param id the id of the waypoint
	 * @param loc the location of the point
	 */
	public Waypoint(Double3D loc)
	{
		this.loc=loc;
	}
	

	public Double3D getLoc()
	{
		return loc;
	}
	
	public void setLoc(Double3D loc)
	{
		this.loc=loc;
	}
	
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
