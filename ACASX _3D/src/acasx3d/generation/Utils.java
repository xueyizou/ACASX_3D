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

public class Utils 
{
	
	public static double getActionV(int actionCode)
	{
		if(actionCode==-1 )//"leaving"
		{
			System.err.println("no V in Loop action!");
			return Double.NaN;
		}	
		else if(actionCode==0)//"COC"
		{
			return Double.NaN;
		}
		else if(actionCode==1 || actionCode==3) //"CL25" "SCL25"
		{
			return 25;
		}
		else if(actionCode==2 || actionCode==4)//"DES25" "SDES25"
		{
			return -25;
		}		
		else if(actionCode==5)//"SCL42"
		{
			return 42;
		}
		else if(actionCode==6)//"SDES42"
		{
			return -42;
		}
		else
		{
			System.err.println("error happens in ACASXUtils.getActionV(int actionCode):an unknown aciton.");
			return Double.NaN;
		}
	}
	
	public static double getActionA(int actionCode)
	{
		if(actionCode==-1)//"leaving"
		{
			System.err.println("no A in Loop action!");
			return Double.NaN;
		}
		else if(actionCode==0)//"COC"
		{
			return 0.0;
		}
		else if(actionCode==1 )//"CL25"
		{
			return 8;
		}
		else if(actionCode==2 )//"DES25"
		{
			return -8;
		}		
		else if(actionCode==3 || actionCode==5)//"SCL42" "SCL25"
		{
			return 10.7;
		}
		else if(actionCode==4|| actionCode==6)//"SDES42" "SDES25"
		{
			return -10.7;
		}
		else
		{
			System.err.println("error happens in ACASXUtils.getActionA(int actionCode):an unknown aciton.");
			return Double.NaN;
		}
	}
}