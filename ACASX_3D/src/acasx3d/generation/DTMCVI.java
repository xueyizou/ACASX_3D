/*******************************************************************************
 *  Copyright (C) Xueyi Zou - All Rights Reserved
 *  Written by Xueyi Zou <xz972@york.ac.uk>, 2015
 *  You are free to use/modify/distribute this file for whatever purpose!
 *  -----------------------------------------------------------------------
 *  |THIS FILE IS DISTRIBUTED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 *  |WARRANTY. THE USER WILL USE IT AT HIS/HER OWN RISK. THE ORIGINAL
 *  |AUTHORS AND COPPELIA ROBOTICS GMBH WILL NOT BE LIABLE FOR DATA LOSS,
 *  |DAMAGES, LOSS OF PROFITS OR ANY OTHER KIND OF LOSS WHILE USING OR
 *  |MISUSING THIS SOFTWARE.
 *  ------------------------------------------------------------------------
 *******************************************************************************/
package acasx3d.generation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class DTMCVI
{
	private State_UCtrl[] ustates;
	
	private final int T=MDPVI.T;
	
	private final double COLLISION_R=500;

	private final int numUStates=(DTMC.nr+1)*(DTMC.nrv+1)*(2*DTMC.ntheta+1);
	
	private double[][] U= new double[T+1][numUStates];

	
	public DTMCVI(DTMC dtmc) 
	{
		ustates=dtmc.states();
		//initialisation
		for (int i=0; i<numUStates;i++)
		{
			State_UCtrl s=ustates[i];
			if(s.getR()<=COLLISION_R)
			{
				U[0][i]=1.0;
			}
			else
			{
				U[0][i]=0.0;
			}
			
		}

		Map<State_UCtrl,Double> TransitionStatesAndProbs;
		
		// repeat				
		for(int iteration=1;iteration<=T;iteration++) 
		{
			System.out.println(iteration);		
			for (int i=0; i<numUStates;i++)
			{
				State_UCtrl s=ustates[i];
				assert (s.getOrder()==i);
			
				double prob=0;	
				if(s.getR()>COLLISION_R)
				{
					TransitionStatesAndProbs= dtmc.getTransitionStatesAndProbs(s);		
					
					Set<Entry<State_UCtrl,Double>> entrySet = TransitionStatesAndProbs.entrySet();							
					for (Entry<State_UCtrl,Double> entry : entrySet) 
					{						
						State_UCtrl nextState = entry.getKey();
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

