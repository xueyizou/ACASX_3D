package visualization.modeling;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous3D;
import sim.util.Bag;
import visualization.configuration.Configuration;
import visualization.modeling.uas.UAS;

public class SAAModel extends SimState
{
	private static final long serialVersionUID = 1L;
	
	public static Configuration config= Configuration.getInstance();

	public boolean runningWithUI = false; 
	public Continuous3D environment3D=null;
	public Bag uasBag = null;	
	public Bag observerBag = null;	
    public String information="no information now"; 

    /**
	 * @param seed for random number generator
	 * @param UI pass true if the simulation is being run with a UI, false if it is not.
	 */
	public SAAModel(long seed, boolean UI)
    {
		super(seed);		
		runningWithUI = UI;		
		environment3D = new Continuous3D(1.0, config.globalConfig.worldX, config.globalConfig.worldY, config.globalConfig.worldZ);
		uasBag=new Bag();
		observerBag=new Bag();
	}    
		
	
	public void start()
	{
		super.start();	
		loadEnvironment();
		loadSchedule();			
	}
	
	public void finish()
	{
		super.finish();		
	}		

	/**
	 * A method which resets the variables for the SAAModel and also clears
	 * the schedule and environment3D of any entities, to be called between simulations.	 * 
	 * This method is called by SAAModelWithUI.start()
	 */
	public void reset()
	{
		uasBag.clear();
		observerBag.clear();
		schedule.reset();
		environment3D.clear(); //clear the environment3D
	}
		
	
	/**
	 * A method which adds all of the entities to the simulations environment3D.
	 */
	public void loadEnvironment()
	{
		for(int i = 0; i < uasBag.size(); i++)
		{
			UAS uas =(UAS) uasBag.get(i);
			environment3D.setObjectLocation(uas, uas.getLoc());		
		}
	}
	
	
	/**
	 * A method which adds all the entities marked as requiring scheduling to the
	 * schedule for the simulation
	 */
	public void loadSchedule()
	{
		//loop across all items in toSchedule and add them all to the schedule
		int count = 0;	
		if (config.globalConfig.collisionAvoidanceEnabler)
		{
			for(int i = 0; i < uasBag.size(); ++i, ++count)
			{
				schedule.scheduleRepeating( ((UAS)uasBag.get(i)).getAcasx(), count, 1.0);
			}	
			
		}
		
		for(int i=0; i < uasBag.size(); ++i, ++count)
		{
			schedule.scheduleRepeating(((UAS)uasBag.get(i)).getAp(), count, 1.0);			
		}
		
		for(int i=0; i < uasBag.size(); ++i, ++count)
		{
			schedule.scheduleRepeating((UAS) uasBag.get(i), count, 1.0);
		}	
		
		for(int i=0; i < observerBag.size(); ++i, ++count)
		{
			schedule.scheduleRepeating((Steppable) observerBag.get(i), count, 1.0);
		}	
			
	}


	public String getInformation() 
	{
		return information;
	} 

}
