package acasx3d.generation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class DTMCValueIteration
{
	private ACASX3DUState[] ustates;
	
	private final int T=MDPValueIteration.T;
	
	private final double COLLISION_R=500;

	private final int numUStates=(ACASX3DDTMC.nr+1)*(ACASX3DDTMC.nrv+1)*(2*ACASX3DDTMC.ntheta+1);
	
	private double[][] U= new double[T+1][numUStates];

	
	public DTMCValueIteration(ACASX3DDTMC dtmc) 
	{
		ustates=dtmc.states();
		//initialisation
		for (int i=0; i<numUStates;i++)
		{
			ACASX3DUState s=ustates[i];
			if(s.getR()<=COLLISION_R)
			{
				U[0][i]=1.0;
			}
			else
			{
				U[0][i]=0.0;
			}
			
		}

		Map<ACASX3DUState,Double> TransitionStatesAndProbs;
		
		// repeat				
		for(int iteration=1;iteration<=T;iteration++) 
		{
			System.out.println(iteration);		
			for (int i=0; i<numUStates;i++)
			{
				ACASX3DUState s=ustates[i];
				assert (s.getOrder()==i);
			
				double prob=0;	
				if(s.getR()>COLLISION_R)
				{
					TransitionStatesAndProbs= dtmc.getTransitionStatesAndProbs(s);		
					
					Set<Entry<ACASX3DUState,Double>> entrySet = TransitionStatesAndProbs.entrySet();							
					for (Entry<ACASX3DUState,Double> entry : entrySet) 
					{						
						ACASX3DUState nextState = entry.getKey();
						int nextStateOrder = nextState.getOrder();
						prob += entry.getValue() * U[iteration-1][nextStateOrder];
					}
				}
				U[iteration][i]=prob;
				
			}				
		}	

	}
	
	public void storeValues(String entryTimeDistributionFileName) 
	{	
		FileWriter entryTimeDistributionFileWriter = null;	
		try 
        {
            entryTimeDistributionFileWriter = new FileWriter("./src/acasx3d/generation/generatedFiles/"+entryTimeDistributionFileName,false);

            for(int k=0; k<=T;k++ )
            {
            	for (int su=0; su<numUStates;su++)
    			{
            		entryTimeDistributionFileWriter.write(U[k][su]+"\n"); 
    			}
            }  
   		
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
	                if(entryTimeDistributionFileWriter!=null)
	                {
	                	entryTimeDistributionFileWriter.close();
	                }
	               
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
				
	}	
	
}

