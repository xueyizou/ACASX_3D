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
package acasx3d;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileCheckers
{
	static String indexFile1 = "/home/xueyi/git/ACASX_3D/ACASX _3D/src/acasx3d/generation/generatedFiles/indexFile";
	static String indexFile2 = "/home/xueyi/QtLib/ACASX/Debug/indexFile";
	
	static String costFile1 = "/home/xueyi/git/ACASX_3D/ACASX _3D/src/acasx3d/generation/generatedFiles/costFile";
	static String costxFile2 = "/home/xueyi/QtLib/ACASX/Debug/costFile";
	
	static String actionFile1 = "/home/xueyi/git/ACASX_3D/ACASX _3D/src/acasx3d/generation/generatedFiles/actionFile";
	static String actionFile2 = "/home/xueyi/QtLib/ACASX/Debug/actionFile";
	
	static String entryTimeDistributionFile1 = "/home/xueyi/git/ACASX_3D/ACASX _3D/src/acasx3d/generation/generatedFiles/entryTimeDistributionFile";
	static String entryTimeDistributionFile2 = "/home/xueyi/QtLib/ACASX/Debug/entryTimeDistributionFile";
	
	public static boolean compareIndexTable()
	{
		System.out.println("Reading index look-up table...!");
		long startTime = System.currentTimeMillis();	
		long endTime;	
		BufferedReader indexFileReader1=null;
		BufferedReader indexFileReader2=null;
		ArrayList<Integer>	indexArr1= new ArrayList<>();
		ArrayList<Integer>	indexArr2= new ArrayList<>();
		try 
        {
			indexFileReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile1)));			
			String buffer=null;
			while((buffer=indexFileReader1.readLine())!=null && !buffer.isEmpty())
			{
				indexArr1.add(Integer.parseInt(buffer));

			}
			
			indexFileReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile2)));			
			while((buffer=indexFileReader2.readLine())!=null && !buffer.isEmpty())
			{
				indexArr2.add(Integer.parseInt(buffer));

			}
			
			if(indexArr1.size()!=indexArr2.size())
			{
				System.err.println(indexArr1.size()+" entries isn't equal to "+indexArr2.size());
				return false;
			}
			
			for(int i=0;i<indexArr1.size();i++)
			{
				if(!indexArr1.get(i).equals(indexArr2.get(i)))
				{
					System.out.println(indexArr1.get(i)+ "  "+ indexArr2.get(i));
					endTime = System.currentTimeMillis();
					System.out.println("Files are equal? ["+false+"], difference in "+ i+ " Done! The time for comparing index look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
					return false;
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
	                if(indexFileReader1!=null)
	                {
	                	indexFileReader1.close();
	                }
	                if(indexFileReader2!=null)
	                {
	                	indexFileReader2.close();
	                }	               
	         } 
	         catch (IOException e) 
	         {
	             e.printStackTrace();
	         }
		}
		
		endTime = System.currentTimeMillis();
		System.out.println("Files are equal? ["+true+"] Done! The time for comparing index look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
		return true;
	}
	
	public static boolean compareCostTable()
	{
		System.out.println("Reading cost look-up table...!");
		long startTime = System.currentTimeMillis();
		long endTime ;
		BufferedReader costFileReader1=null;
		BufferedReader costFileReader2=null;
		ArrayList<Double>	costArr1= new ArrayList<>();
		ArrayList<Double>	costArr2= new ArrayList<>();
		try 
        {
			costFileReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(costFile1)));			
			String buffer=null;
			while((buffer=costFileReader1.readLine())!=null && !buffer.isEmpty())
			{
				costArr1.add(Double.parseDouble(buffer));

			}
			
			costFileReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(costxFile2)));			
			while((buffer=costFileReader2.readLine())!=null && !buffer.isEmpty())
			{
				costArr2.add(Double.parseDouble(buffer));

			}
			
			if(costArr1.size()!=costArr2.size())
			{
				System.err.println(costArr1.size()+" entries isn't equal to "+costArr2.size());
				return false;
			}
			
			for(int i=0;i<costArr1.size();i++)
			{
				if(Math.abs(costArr1.get(i)-costArr2.get(i))>0.1)
				{
					System.out.println(costArr1.size()+" total entries");
					System.out.println(costArr1.get(i)+ "  "+ costArr2.get(i));
					endTime = System.currentTimeMillis();
					System.out.println("Files are equal? ["+false+"], difference in "+ i+ "Done! The time for comparing cost look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
					return false;
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
	                if(costFileReader1!=null)
	                {
	                	costFileReader1.close();
	                }
	                if(costFileReader2!=null)
	                {
	                	costFileReader2.close();
	                }
	         } 
	         catch (IOException e) 
	         {
	             e.printStackTrace();
	         }
		}

		endTime = System.currentTimeMillis();
		System.out.println("Files are equal? ["+true+"] Done! The time for comparing cost look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
		return true;
	}
	
	public static boolean compareActionTable()
	{
		System.out.println("Reading action look-up  table...!");
		long startTime = System.currentTimeMillis();	
		long endTime;
		BufferedReader actionFileReader1=null;
		BufferedReader actionFileReader2=null;
		ArrayList<Integer>	actionArr1= new ArrayList<>();
		ArrayList<Integer>	actionArr2= new ArrayList<>();
		try 
        {
			actionFileReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(actionFile1)));			
			String buffer=null;
			while((buffer=actionFileReader1.readLine())!=null && !buffer.isEmpty())
			{
				actionArr1.add(Integer.parseInt(buffer));

			}
			
			actionFileReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(actionFile2)));			
			while((buffer=actionFileReader2.readLine())!=null && !buffer.isEmpty())
			{
				actionArr2.add(Integer.parseInt(buffer));

			}
			
			if(actionArr1.size()!=actionArr2.size())
			{
				System.err.println(actionArr1.size()+" entries isn't equal to "+actionArr2.size());
				return false;
			}
			
			for(int i=0;i<actionArr1.size();i++)
			{
				if(!actionArr1.get(i).equals(actionArr2.get(i)))
				{
					System.out.println(actionArr1.get(i)+ "  "+ actionArr2.get(i));
					endTime = System.currentTimeMillis();
					System.out.println("Files are equal? ["+false+"], difference in "+ i+ " Done! The time for comparing action look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
					return false;
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
	                if(actionFileReader1!=null)
	                {
	                	actionFileReader1.close();
	                }
	                if(actionFileReader2!=null)
	                {
	                	actionFileReader2.close();
	                }	               
	         } 
	         catch (IOException e) 
	         {
	             e.printStackTrace();
	         }
		}

		endTime = System.currentTimeMillis();
		System.out.println("Files are equal? ["+true+"] Done! The time for comparing action look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
		return true;
	}
	
	public static boolean compareEntryTimeDistributionTable()
	{
		System.out.println("Reading entryTimeDistribution look-up table...!");
		long startTime = System.currentTimeMillis();
		long endTime ;
		BufferedReader entryTimeDistributionFileReader1=null;
		BufferedReader entryTimeDistributionFileReader2=null;
		ArrayList<Double> entryTimeDistributionArr1= new ArrayList<>();
		ArrayList<Double> entryTimeDistributionArr2= new ArrayList<>();
		try 
        {
			entryTimeDistributionFileReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(entryTimeDistributionFile1)));			
			String buffer=null;
			while((buffer=entryTimeDistributionFileReader1.readLine())!=null && !buffer.isEmpty())
			{
				entryTimeDistributionArr1.add(Double.parseDouble(buffer));

			}
			
			entryTimeDistributionFileReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(entryTimeDistributionFile1)));			
			while((buffer=entryTimeDistributionFileReader2.readLine())!=null && !buffer.isEmpty())
			{
				entryTimeDistributionArr2.add(Double.parseDouble(buffer));

			}
			
			if(entryTimeDistributionArr1.size()!=entryTimeDistributionArr2.size())
			{
				System.err.println(entryTimeDistributionArr1.size()+" entries isn't equal to "+entryTimeDistributionArr2.size());
				return false;
			}
			
			for(int i=0;i<entryTimeDistributionArr1.size();i++)
			{
				if(Math.abs(entryTimeDistributionArr1.get(i)-entryTimeDistributionArr2.get(i))>0.01)
				{
					System.out.println(entryTimeDistributionArr1.get(i)+ "  "+ entryTimeDistributionArr2.get(i));
					endTime = System.currentTimeMillis();
					System.out.println("Files are equal? ["+false+"], difference in "+ i+ "  Done! The time for comparing cost look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
					return false;
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
	                if(entryTimeDistributionFileReader1!=null)
	                {
	                	entryTimeDistributionFileReader1.close();
	                }
	                if(entryTimeDistributionFileReader2!=null)
	                {
	                	entryTimeDistributionFileReader2.close();
	                }
	         } 
	         catch (IOException e) 
	         {
	             e.printStackTrace();
	         }
		}

		endTime = System.currentTimeMillis();
		System.out.println("Files are equal? ["+true+"] Done! The time for comparing cost look-up tables is "+ (endTime-startTime)/1000 +" senconds.");
		return true;
	}
	
	public static void main(String[] args) 
	{
		compareIndexTable();
		compareCostTable();
		compareActionTable();
		compareEntryTimeDistributionTable();
	}

}
