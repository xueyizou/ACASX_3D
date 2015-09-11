package acasx3d.generation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class MDPValueIteration
{
	private ACASX3DMDP mdp;
	
	public static final int T=20;
	
	private ACASX3DCState[] states;

	private final int numCStates=(2*ACASX3DMDP.nh+1)*(2*ACASX3DMDP.noVy+1)*(2*ACASX3DMDP.niVy+1)*(ACASX3DMDP.nra);
	
	private double[][] U= new double[T+2][numCStates];//U[T+1] is for the K-step expected cost when k>T (i.e. k>K), denoted JkBar

	/**
	 * Constructor.
	 * 
	 * @param gamma discount &gamma; to be used.
	 */
	public MDPValueIteration(ACASX3DMDP mdp, double gamma,double epsilon) 
	{
		this.mdp=mdp;
		if (gamma > 1.0 || gamma <= 0.0) 
		{
			throw new IllegalArgumentException("Gamma must be > 0 and <= 1.0");
		}
		
		if (epsilon < 0.0) 
		{
			throw new IllegalArgumentException("epsilon must be >= 0");
		}

		states=mdp.states();
		//initialisation
		for (int i=0; i<numCStates;i++)
		{
			ACASX3DCState s=states[i];
			U[0][i]=mdp.reward(s, -1);
			U[T+1][i]=0;
		}

		Map<ACASX3DCState,Double> TransitionStatesAndProbs;
		double[] JkBar= new double[numCStates];
		// repeat				
		for(int iteration=1;iteration<=T;iteration++) 
		{
			System.out.println(iteration);		
			for (int i=0; i<numCStates;i++)
			{
				ACASX3DCState s=states[i];
				if(s.getOrder()!=i)
				{
					System.err.println("error happens in valueIteration() + s.getOrder()!=i");
				}
				ArrayList<Integer> actions= mdp.actions(s);

				double aMax1 = Double.NEGATIVE_INFINITY;
				double aMax2 = Double.NEGATIVE_INFINITY;
				for (Integer a : actions) 
				{
					double aSum1=mdp.reward(s, a);	
					double aSum2=mdp.reward(s, a);	
					TransitionStatesAndProbs= mdp.getTransitionStatesAndProbs(s, a);		
					
					Set<Entry<ACASX3DCState,Double>> entrySet = TransitionStatesAndProbs.entrySet();							
					for (Entry<ACASX3DCState,Double> entry : entrySet) 
					{						
						ACASX3DCState nextState = entry.getKey();
						int nextStateOrder = nextState.getOrder();
						aSum1 += entry.getValue() * gamma *  U[iteration-1][nextStateOrder];
						aSum2 += entry.getValue() * gamma *  U[T+1][nextStateOrder];
					}
						
					if (aSum1 > aMax1) 
					{
						aMax1 = aSum1;
					}	
					
					if (aSum2 > aMax2) 
					{
						aMax2 = aSum2;
					}

				}
				U[iteration][i]=aMax1;	
				JkBar[i]=aMax2;
			}
			U[T+1]=JkBar.clone();
		}	

	}
		

	public double getQValue(int k,ACASX3DCState state, int action) 
	{	
		double QValue = 0;
		Map<ACASX3DCState,Double> TransitionStatesAndProbs= mdp.getTransitionStatesAndProbs(state, action);
		Set<Entry<ACASX3DCState,Double>> entrySet = TransitionStatesAndProbs.entrySet();

		for (Entry<ACASX3DCState,Double> entry : entrySet) 
		{
			if(entry.getValue()>1)
			{
				System.err.println(entry.getValue()+"greater than 1");
			}
			ACASX3DCState nextState = entry.getKey();
			int nextStateOrder=nextState.getOrder();
			QValue += entry.getValue()*  U[k-1][nextStateOrder];
		
		}
		return QValue;		
	}
	
	public void storeQValues() 
	{	
		FileWriter indexFileWriter = null;
		FileWriter costFileWriter = null;
		FileWriter actionFileWriter = null;
	
		try 
        {
            indexFileWriter = new FileWriter("./src/acasx3d/generation/generatedFiles/indexFile",false);
            costFileWriter = new FileWriter("./src/acasx3d/generation/generatedFiles/costFile",false);
            actionFileWriter = new FileWriter("./src/acasx3d/generation/generatedFiles/actionFile",false);
      
            int index=0;
            for (int i=0; i<numCStates;i++)//T=k=0
			{		
				indexFileWriter.write(index+"\n");    			 	
				actionFileWriter.write(0+"\n");  //leaving states, using O to avoid arrayIndexOutofRange problem
				index++;   
				costFileWriter.write(U[0][i]+"\n");
			}
            
            for(int k=1; k<=T+1;k++ )
            {
            	for (int i=0; i<numCStates;i++)
    			{
    				ACASX3DCState s=states[i];
    				ArrayList<Integer> actions=mdp.actions(s);
					indexFileWriter.write(index+"\n");        			
        			for (int a :actions ) 
        			{
        				actionFileWriter.write(a+"\n");    				
        				double QValue = getQValue(k, s, a);
        				costFileWriter.write(QValue+"\n");
        			}	
        			index+=actions.size();  
    			}
            }  
			indexFileWriter.write(index+"\n");
    		
        }
        catch(Exception e) 
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally 
        {
            try 
            {
	                if(indexFileWriter!=null)
	                {
	                	indexFileWriter.close();
	                }
	                if(costFileWriter!=null)
	                {
	                	costFileWriter.close();
	                }
	                if(actionFileWriter!=null)
	                {
	                	actionFileWriter.close();
	                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
				
	}	
	
}

