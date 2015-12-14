package visualization.modeling.uas;


import sim.util.Double3D;
import visualization.modeling.observer.CollisionDetector;

public class Proximity
{
	public double distH;
	public double distV;
	
	public Proximity(double distH, double distV)
	{
		this.distH=distH;
		this.distV=distV;
	}
	
	public Proximity(Double3D loc1, Double3D loc2)
	{
		this.distH=Math.sqrt(Math.pow(loc1.x-loc2.x,2) + Math.pow(loc1.z-loc2.z,2));
		this.distV=Math.abs(loc1.y-loc2.y);
	}
		
	public double toValue()
	{
		return Math.max(0,distH-CollisionDetector.thresholdH)+ Math.max(0,distV-CollisionDetector.thresholdV);//Math.sqrt(distH*distH + distV*distV);
	}
	public String toString()
	{
		return String.format("[%.1f, %.1f]%.1f", distH, distV, this.toValue());
	}	

	
	public boolean lessThan(Proximity thatProx)
	{
//		return (this.distH<thatProx.distH) && (this.distV<thatProx.distV);
		return this.toValue()<thatProx.toValue();
	}
}
