package acasx3d.generation;

public class ACASX3DGen 
{

	public ACASX3DGen() 
	{
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("hi!Process starts...");
		
		long time0 = System.currentTimeMillis();		
		ACASX3DMDP mdp = new ACASX3DMDP();
		long time1 = System.currentTimeMillis();
		System.out.println("MDP building Done! The running time is "+ (time1-time0)/1000 +" senconds.");
		MDPValueIteration mdpVI = new MDPValueIteration(mdp, 1.0,0.5);
		long time2 = System.currentTimeMillis();
		System.out.println("MDP Value Iteration Done! The running time is "+ (time2-time1)/1000 +" senconds.");
		mdpVI.storeQValues();
		long time3 = System.currentTimeMillis();
		System.out.println("MDP QValue Store Done! The running time is "+ (time3-time2)/1000 +" senconds.");
		
		long time4 = System.currentTimeMillis();
		ACASX3DDTMC dtmc = new ACASX3DDTMC();
		long time5 = System.currentTimeMillis();
		System.out.println("DTMC building Done! The running time is "+ (time5-time4)/1000 +" senconds.");
		DTMCValueIteration dtmcVI = new DTMCValueIteration(dtmc);
		long time6 = System.currentTimeMillis();
		System.out.println("DTMC Value Iteration Done! The running time is "+ (time6-time5)/1000 +" senconds.");
		dtmcVI.storeValues("entryTimeDistributionFile");
		long time7 = System.currentTimeMillis();
		System.out.println("DTMC values Storage Done! The running time is "+ (time7-time6)/1000 +" senconds.");

	}

}
