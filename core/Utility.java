package core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utility {

	public String timeStamp()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy H:mm:ss"); 
	    return dateFormat.format(new Date()).toString();

	}

	
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
	}
	
	public boolean checkNames(String[] colName)
	{
		int n=colName.length;
		int i,j;
		
		for(i=0;i<n-1;i++)
		{
			for(j=i+1;j<n;j++)
			{
				if(colName[i].equals(colName[j]))
					return false;
			}
		}
		return true;
		
	}
	
	public int getUpdatePosition(String attribute, ArrayList alH)
	{
		
		ArrayList al=new ArrayList();
		
		int i;
		int n=alH.size();
		int pos=-1;
		
		for(i=0;i<n;i++)
		{
			String name= (String) alH.get(i);
			if(attribute.equals(name))
			{
				//System.out.println(expression+"\t"+name);
				pos=i;
			}
		}
		//System.out.println(pos);
		
		return pos;
	}

	public ArrayList getWherePositions(String expression, ArrayList alH)
	{
		
		ArrayList al=new ArrayList();
		
		int i;
		int n=alH.size();
		
		for(i=0;i<n;i++)
		{
			String name= (String) alH.get(i);
			if(expression.contains(name))
			{
				//System.out.println(expression+"\t"+name);
				al.add(i);
			}
		}
		//System.out.println(al.toString());

		
		return al;
	}
	
}
