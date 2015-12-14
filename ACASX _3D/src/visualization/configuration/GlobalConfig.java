package visualization.configuration;

public class GlobalConfig 
{
	public double worldX = 20000; 
	public double worldY = 5000; 
	public double worldZ = 10000; 
	
	public boolean collisionAvoidanceEnabler=true;
	public boolean accidentDetectorEnabler=true;
	public boolean whiteNoiseEnabler=true;	
	
	public double stdDevX =3;//[0,3]
	public double stdDevY =3;//[0,3]
	public double stdDevZ =3;//[0,3]
	
	public double maxGS =304;
	public double minGS =169;
	public double maxVS =58;
	public double minVS =-67;
}
