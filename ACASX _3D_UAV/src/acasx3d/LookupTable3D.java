package acasx3d;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import acasx3d.generation.ACASX3DDTMC;
import acasx3d.generation.ACASX3DMDP;
import acasx3d.generation.MDPValueIteration;


public class LookupTable3D
{
	private static LookupTable3D lookupTable3D;
	private static String generatedFilesPrefix = "/home/xueyi/EclipseWorkSpace/Java/ACASX _3D_UAV/src/acasx3d/generation/generatedFiles/";
	
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
	
		numCStates = (2*ACASX3DMDP.nh+1)*(2*ACASX3DMDP.noVy+1)*(2*ACASX3DMDP.niVy+1)*(ACASX3DMDP.nra);
		int numEntries1=numCStates*(MDPValueIteration.T+2) + 1;

		numUStates = (ACASX3DDTMC.nr+1)*(ACASX3DDTMC.nrv+1)*(2*ACASX3DDTMC.ntheta+1);
		int numEntries2=numUStates*(MDPValueIteration.T+1);
		
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
