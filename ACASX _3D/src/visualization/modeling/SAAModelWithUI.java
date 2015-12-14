package visualization.modeling;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.swing.JFrame;
import javax.vecmath.Color3f;

import sim.display.Console;
import sim.display.Controller;
import sim.display.GUIState;
import sim.display3d.Display3D;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal3d.continuous.ContinuousPortrayal3D;
import sim.portrayal3d.simple.SpherePortrayal3D;
import sim.portrayal3d.simple.WireFrameBoxPortrayal3D;
import visualization.configuration.Configuration;
import visualization.configuration.SAAConfigurator;
import visualization.modeling.SAAModel;
import visualization.modeling.SAAModelWithUI;
import visualization.modeling.SimInitializer;
import visualization.modeling.env.Waypoint;
import visualization.modeling.uas.UAS;

/**
 * A class for running a simulation with a UI, run to see a simulation with a UI
 * showing it running.
 * 
 * @author Xueyi Zou
 */
public class SAAModelWithUI extends GUIState
{	
	public static Configuration config= Configuration.getInstance();
	
	public Display3D display3D;
	public JFrame display3DFrame;	
	ContinuousPortrayal3D environment3DPortrayal = new ContinuousPortrayal3D();
	WireFrameBoxPortrayal3D wireFrameP = new WireFrameBoxPortrayal3D(-0.65*config.globalConfig.worldX,-0.5*config.globalConfig.worldY,-0.5*config.globalConfig.worldZ,  0.35*config.globalConfig.worldX,0.5*config.globalConfig.worldY,0.5*config.globalConfig.worldZ);
   
	SpherePortrayal3D uasPortrayal=null;
	SpherePortrayal3D wpPortrayal=null;
	
    public SAAModelWithUI() 
    {   
        super(new SAAModel(785945568, true)); 	
    }
  
    
    public void init(Controller c)
    {
        super.init(c);     
        // make the 3D display
        display3D = new Display3D(config.globalConfig.worldX,config.globalConfig.worldY,this);           
        display3D.scale(1.2 / config.globalConfig.worldX);        
        
        display3DFrame = display3D.createFrame();
        display3DFrame.setTitle("ACAS X Simulation");
        c.registerFrame(display3DFrame);   // register the frame so it appears in the "Display" list        
        display3DFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        display3DFrame.setVisible(true);
        
		//adding the different layers to the display3D
        display3D.attach(environment3DPortrayal, "Environment3D" );
        display3D.attach(wireFrameP, "WireFrame");

        final double k=10;
        uasPortrayal = new SpherePortrayal3D(Color.CYAN, 15*k, 10);		
		
		wpPortrayal = new SpherePortrayal3D(new Color(0, 255, 0), 3, 10)
		{
			public javax.media.j3d.TransformGroup getModel(java.lang.Object obj, javax.media.j3d.TransformGroup j3dModel)
			{
				Color3f col=new Color3f(0, 0, 0);
				double scale=3;
				
				
				int actionCode = ((Waypoint) obj).getAction();
				if(actionCode==31||actionCode==33 ||actionCode==35)
				{//climb					
					switch(actionCode)
					{
					case 31:
						col = new Color3f(255,0,0);//CL25
						scale =3*k;
						break;
					case 33:
						col = new Color3f(255, 0, 0);//SCL25
						scale =6*k;
						break;
					case 35:
						col = new Color3f(255, 0, 0);//SCL42	
						scale =9*k;
					}
				}
				else if(actionCode==32||actionCode==34 ||actionCode==36)
				{//descend						
					switch(actionCode)
					{
					case 32:
						col = new Color3f(0,255,0);//DES25
						scale =3*k;
						break;
					case 34:
						col = new Color3f(0, 255, 0);//SDES25
						scale =6*k;
						break;
					case 36:
						col = new Color3f(0, 255, 0);//SDES42	
						scale =9*k;
					}
				}
				else if(actionCode==30)
				{//COC
					col = new Color3f(0, 0, 255);
					scale = 3*k;
				}
				else
				{
					//white for normal
					col = new Color3f(255, 255, 255);
					scale = 3*k;
				}

				//Create the coloring attributes
				ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
				//Add the attributes to the appearance
				Appearance ap = new Appearance();
				ap.setColoringAttributes(ca);			
				//setTransform(j3dModel,javax.media.j3d.Transform3D transform);
				setAppearance(j3dModel, ap);				
				setScale(j3dModel,scale);
				return  super.getModel(obj, j3dModel);
			}
		};

    }


	public void start()
	{		
		((SAAModel)state).reset();		
		SimInitializer.generateSimulation((SAAModel)state);						
		super.start();
		setupPortrayals();	
		
	}

	public void load(SimState state)
	{
		((SAAModel)state).reset();
		SimInitializer.generateSimulation((SAAModel)state);		
		super.load(state);
		setupPortrayals();
	}

	
	
	/**
	 * A method which sets up the portrayals of the different layers in the UI,
	 * this is where details of the simulation are coloured and set to different
	 * parts of the UI
	 */
	public void setupPortrayals()
	{		
		SAAModel simulation = (SAAModel) state;	
		
		// tell the portrayals what to portray and how to portray them
		environment3DPortrayal.setField( simulation.environment3D );		
		
		environment3DPortrayal.setPortrayalForClass(UAS.class, uasPortrayal);
		
		environment3DPortrayal.setPortrayalForClass(Waypoint.class,wpPortrayal);				

		// reschedule the displayer
		display3D.reset();
		
	}
	
	

    public void quit()
    {
        super.quit();      
        
        if (display3DFrame!=null) display3DFrame.dispose();
        display3DFrame = null;
        display3D = null;
    }
    
    
    public static String getName() { return "ACAS X-Sim"; }

	public Object getSimulationInspectedObject(){return state;}
    
    public Inspector getInspector()
    {
    	Inspector i = super.getInspector();
    	i.setVolatile(true);
    	return i;
    }   
    
    public static void main(String[] args)
    {		
    	SAAModelWithUI saaModelWithUI = new SAAModelWithUI();
    	
    	Console c = new Console(saaModelWithUI); 
    	c.setBounds(1580, 50, 340, 300);
    	c.setAlwaysOnTop(true);
		c.setVisible(true);		
		c.setWhenShouldEnd(100);
		
		SAAConfigurator configurator = new SAAConfigurator(saaModelWithUI.state, saaModelWithUI);
		configurator.setBounds(1580, 380, 340,650); 
		configurator.setAlwaysOnTop(true);
		configurator.setVisible(true);
		configurator.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
    }	

}
