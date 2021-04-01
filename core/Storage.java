package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage {

	public boolean compare(String fileName, String[] colValue)
	{
		String line;
		int n=colValue.length;
		int i;
		String newLine="<tr>";
		for(i=0;i<n;i++)
		{
			newLine+="<td>"+colValue[i]+"</td>";
		}
		newLine+="</tr>";
		
		
		try
		{
	    	BufferedReader reader = new BufferedReader(new FileReader(fileName));

			while ((line = reader.readLine()) != null)
			{
				if(line.equals(newLine))
				{
					reader.close();
					return false;
				}
			}
			reader.close();
		}
		catch(Exception e) 	{return false;}
		
		return true;
	}
	
	public ArrayList getHeaders(String fileName)
	{
		ArrayList al=new ArrayList();
	    File file = new File(fileName);

		String line;
	    String operation;
	    Pattern pattern;
		Matcher matcher;
		String colName;
		
	    try
		{
	    	BufferedReader reader = new BufferedReader(new FileReader(fileName));
			line = reader.readLine();
			line=line.substring(4, line.length()-5);
			
			operation = "<th>(\\w+)</th>";
			pattern = Pattern.compile(operation);
			matcher = pattern.matcher(line);
			while(matcher.find()) 
			{
				colName=matcher.group(1);
				al.add(colName);
			}

			reader.close();
		}
		catch(Exception e) 	{e.printStackTrace();return null;}
		
		return al;
	}
	
	private boolean isSafe(String[] col)
	{
		String[] unsafe={".","[","]","{","}","*",";",":"};
		int n=col.length;
		int i;
		int j;
		int m=unsafe.length;
 
		for(i=0;i<n;i++)
		{
			String name=col[i];
			for(j=0;j<m;j++)
			{
				if(name.contains(unsafe[j]))
					return false;
			}
		}
		return true;
	
		
	}

	public boolean matchColumns(String fileName, String[] colValue)
	{
		int n=colValue.length;
	    File file = new File(fileName);
	    
	    String operation;
	    Pattern pattern;
		Matcher matcher;
		String line;
		String value;
		int i=0;
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			line = reader.readLine();
			line=line.substring(4, line.length()-5);
		
			operation = "<th>(\\w+)</th>";
			pattern = Pattern.compile(operation);
			matcher = pattern.matcher(line);

			while(matcher.find()) 
			{
				value=matcher.group(1);
				i++;
			}
			reader.close();

		}
		catch(Exception e) 	{e.printStackTrace();return false;}

		if(i==n)
			return true;
		else
			return false;
	}
	
	public boolean writeFields(File file, String[] col,boolean append, String tag) throws IOException
	{
		boolean result=false;
		boolean isSafe=this.isSafe(col);
		if(isSafe==false) return false;
		
		FileWriter fw = new FileWriter(file,append);
		Integer n=col.length;
		Integer i;
		String field;
		fw.write("<tr>");
		for (i=0;i<n;i++)
		{
			field="<"+tag+">"+col[i]+"</"+tag+">";
			fw.write(field);	
		}
		fw.write("</tr>\n");

		fw.flush();
	    fw.close();
	    
	    result=true;
		return result;
	}

	

}
