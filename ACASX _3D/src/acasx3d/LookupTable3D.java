package acasx3d;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import acasx3d.generation.DTMC;
import acasx3d.generation.MDP;
import acasx3d.generation.MDPVI;


public class LookupTable3D
{
	private static LookupTable3D lookupTable3D;
	private static String generatedFilesPrefix = "./src/acasx3d/generation/generatedFiles/";
	
	public int numCStates;
	public int numUStates;

	public ArrayList<Integer> indexArr=new ArrayList<>();
	public ArrayList<Double> costArr=new ArrayList<>();
	public ArrayList<Integer> actionArr=new ArrayList<>();
	public ArrayList<Double> entryTimeDistributionArr=new ArrayList<>();
	
	public BufferedReader indexFileReader = null;
	public BufferedReader costFileReader = null;
	public BufferedReader actionFileReader = null;
	public BufferedReader entryTimeDistributionFileReader = null;
	
	private LookupTable3D()
	{
		System.out.println("Reading look-up table...!");
		long startTime = System.currentTimeMillis();		
	
		numCStates = (2*MDP.nh+1)*(2*MDP.noVy+1)*(2*MDP.niVy+1)*(MDP.nra);
		int numEntries1=numCStates*(MDPVI.T+2) + 1;

		numUStates = (DTMC.nr+1)*(DTMC.nrv+1)*(2*DTMC.ntheta+1);
		int numEntries2=numUStates*(MDPVI.T+1);
		
		try
        {			
			indexFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(generatedFilesPrefix+"indexFile")));
			costFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(generatedFilesPrefix+"costFile")));
			actionFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(generatedFilesPrefix+"actionFile")));
			entryTimeDistributionFileReader= new BufferedReader(new InputStreamReader(new FileInputStream(generatedFilesPrefix+"entryTimeDistributionFile")));

			String buffer=null;
			while((buffer=indexFileReader.readLine())!=null)
			{
				indexArr.add(Integer.parseInt(buffer));

			}
			
			if(indexArr.size()!=numEntries1)
			{
				System.err.println(indexArr.size()+" entries, duplicates in indexFile, should be"+numEntries1);
			}
			
			while((buffer=costFileReader.readLine())!=null)
			{
				costArr.add(Double.parseDouble(buffer));
			}
			
			while((buffer=actionFileReader.readLine())!=null)
			{
				actionArr.add(Integer.parseInt(buffer));
			}
			
			while((buffer=entryTimeDistributionFileReader.readLine())!=null)
			{
				entryTimeDistributionArr.add(Double.parseDouble(buffer));
			}
			if(entryTimeDistributionArr.size()!=numEntries2)
			{
				System.err.println(entryTimeDistributionArr.size()+" entries, duplicates in entryTimeDistributionFile, should be"+numEntries2);
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
	                if(indexFileReader!=null)
	                {
	                	indexFileReader.close();
	                }
	                if(costFileReader!=null)
	                {
	                	costFileReader.close();
	                }
	                if(actionFileReader!=null)
	                {
	                	actionFileReader.close();
	                }
	                if(entryTimeDistributionFileReader!=null)
	                {
	                	entryTimeDistributionFileReader.close();
	                }
	         } 
	         catch (IOException e) 
	         {
	             e.printStackTrace();
	         }
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Done! The time for reading look-up table is "+ (endTime-startTime)/1000 +" senconds.");
	}

	public static LookupTable3D getInstance()
	{
		if(lookupTable3D==null)
		{
			lookupTable3D= new LookupTable3D();
		}
		return lookupTable3D;
	}
}
