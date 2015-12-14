package visualization.modeling;

import visualization.configuration.Configuration;
import visualization.modeling.encountergenerator.EncounterGenerationFactory;
import visualization.modeling.encountergenerator.OwnshipGenerator;
import visualization.modeling.observer.CollisionDetector;
import visualization.modeling.observer.ProximityMeasurer;
import visualization.modeling.uas.UAS;
/**
 *
 * @author Xueyi Zou
 * This class is used to build/initialize the simulation.
 * Called for by SAAModelWithUI class
 */
public class SimInitializer
{	
	public static Configuration config= Configuration.getInstance();
	
	public static void generateSimulation(SAAModel state)
	{	
		UAS ownship = new OwnshipGenerator(state,"ownship",config.ownshipConfig.ownshipVy, config.ownshipConfig.ownshipGs).execute();
		state.uasBag.add(ownship);
		
		for(String intruderAlias: config.encountersConfig.keySet())
		{
			UAS intruder=EncounterGenerationFactory.generateIntruder(state, ownship, intruderAlias,config.encountersConfig.get(intruderAlias));
			state.uasBag.add(intruder);
		}		

	    ProximityMeasurer pMeasurer= new ProximityMeasurer();
	    CollisionDetector aDetector= new CollisionDetector();
	    state.observerBag.add(pMeasurer);
	    state.observerBag.add(aDetector);// index is 1
			
	}
	
}
