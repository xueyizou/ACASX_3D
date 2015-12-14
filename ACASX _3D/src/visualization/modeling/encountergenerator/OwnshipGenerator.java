/**
 * 
 */
package visualization.modeling.encountergenerator;

import sim.util.Double3D;
import visualization.configuration.Configuration;
import visualization.configuration.GlobalConfig;
import visualization.modeling.SAAModel;
import visualization.modeling.uas.UAS;
import visualization.modeling.uas.UASPerformance;
import visualization.saa.ACASX3DProxy;
import visualization.saa.AutoPilot;


/**
 * @author Xueyi
 *
 */
public class OwnshipGenerator
{
	private SAAModel state;
	private String alias;
	GlobalConfig globalConfig = Configuration.getInstance().globalConfig;
	private double locX=-0.45*globalConfig.worldX;
	private double locY=0;
	private double locZ=0;
	private double Vy;
	private double Gs;
		
	public OwnshipGenerator(SAAModel state, String ownshipAlias, double ownshipVy, double ownshipGs) 
	{		
		this.state=state;
		this.alias=ownshipAlias;		
		this.Vy=ownshipVy;
		this.Gs=ownshipGs;
	}
	
	public UAS execute()
	{		
		Double3D location = new Double3D(locX,locY,locZ);
		Double3D uasVelocity = new Double3D(Gs,Vy,0);
		UASPerformance uasPerformance = new UASPerformance(globalConfig.stdDevX, globalConfig.stdDevY,globalConfig.stdDevZ,
				globalConfig.maxGS, globalConfig.minGS, globalConfig.maxVS, globalConfig.minVS);
		
		UAS ownship = new UAS(location, uasVelocity,uasPerformance);
		ownship.setAlias(alias);
		
		AutoPilot ap= new AutoPilot(state, ownship, Configuration.getInstance().globalConfig.whiteNoiseEnabler, "WhiteNoise");		
		
		ACASX3DProxy acasx= new ACASX3DProxy(state, ownship);		
		
		ownship.init(ap,acasx);	
		return ownship;
		
	}
}
