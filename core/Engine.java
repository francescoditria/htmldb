package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Engine {

	private String dataPath;
	private String currDB;
	private String ext=".html";

	
	public String select(String tableName, String target, String expression)
	{
		//System.out.println("expr\t"+expression);

		String fileName=this.createFileName(tableName);
		ArrayList<String> arr = new ArrayList<String> ();
		ArrayList<Integer> ordine = new ArrayList<Integer> ();
		ArrayList<String> arr2 = new ArrayList<String> ();
		int i,j;
		String[] col = target.split(",");
		String msg="";
		String expressionMod;

		Storage store=new Storage();
		arr=store.getHeaders(fileName);
		Evaluator evaluator=new Evaluator();
		Utility utility=new Utility();
		ArrayList wherePos=utility.getWherePositions(expression, arr);
		//System.out.println(wherePos.toString());

		String operation;
		Pattern pattern;
		Matcher matcher;

		msg="<table border=1>";
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			
			//prima riga
			line = reader.readLine();			
			int n=arr.size();
			int m=col.length;

			//verifica ordine dei campi
			for(j=0;j<m;j++)
				for(i=0;i<n;i++)
				{
					//System.out.println(arr.get(i) +" "+  col[j]);
					String attrib= arr.get(i);
					String colonna= col[j];
					if(attrib.equals(colonna) || colonna.equals("*"))
					{
						//System.out.println(colonna +  " indice " +i);
						msg+="<th>"+attrib+"</th>";
						//int s=j+1;
						ordine.add(i);
					}
				}
			msg="<tr>"+msg+"</tr>";
			//System.out.println(msg);
			//msg="";
			
			while ((line = reader.readLine()) != null)
			{
				//System.out.println("riga "+linea);
				arr2.clear();
				expressionMod=expression;
				i=0;
				operation = "<td>(\\w+)</td>";
				pattern = Pattern.compile(operation);
				matcher = pattern.matcher(line);
				while(matcher.find())
				{
					String valore=matcher.group(1);
					arr2.add(valore);
					if(wherePos.contains(i))
					{
						//System.out.println("1\t"+expressionMod);
						if(!utility.isNumeric(valore))
							expressionMod=expressionMod.replace((String) arr.get(i),"'"+valore+"'");
						else
							expressionMod=expressionMod.replace((String) arr.get(i),valore);
						//System.out.println("2\t"+expressionMod);
					}
					i++;
				}
			
				expressionMod=expressionMod.replace("=", "==");
				String result;
				//System.out.println("Final\t"+expressionMod);
				if(!expressionMod.isEmpty())
					result=evaluator.evaluation(expressionMod, null);
				else
					result="true";
				//System.out.println(result);
				
				String temp2 = new String();
				temp2="";
				n=ordine.size();
				for(i=0;i<n;i++)
				{
					
					//System.out.println(ordine.get(i));
					int x=ordine.get(i);
					//System.out.println("ordine "+x);					
					temp2=temp2+"<td>"+arr2.get(x)+"</td>";
				}
		
			if(result=="true")
				msg+="<tr>"+temp2+"</tr>";
			
			
			
			}
			//System.out.println(msg);
			msg+="</table>";
			reader.close();
		}
		catch(Exception e) 	{e.printStackTrace(); return "Error";}
		return msg;
		//System.out.println("</table>");
		
		
	}

	public String delete(String tableName, String expression)
	{
		String msg="";
		
		//System.out.println(tableName+"\t"+colName+"\t"+newValue+"\t"+expression);
		Storage store=new Storage();
		Utility utility=new Utility();
		Evaluator evaluator=new Evaluator();
		
		String fileName=this.createFileName(tableName);
		String fileNameTemp=this.createFileNameTemp(tableName);
	    File fileTemp = new File(fileNameTemp);
	    File file = new File(fileName);
	    
	    ArrayList al=store.getHeaders(fileName);
		ArrayList wherePos=utility.getWherePositions(expression, al);
		//System.out.println(al.toString());
		
	    String operation;
	    Pattern pattern;
		Matcher matcher;
		String line;
		String colValue;
		boolean found=false;
		String expressionMod;
		int i=0;
		
		
	    try
		{
	    	
	    	FileWriter fw = new FileWriter(fileTemp,false);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			line = reader.readLine();
			fw.write(line+"\n");
			
			while ((line = reader.readLine()) != null)
			{
				i=0;
				line=line.substring(4, line.length()-5);
				operation = "<td>(\\w+)</td>";
				pattern = Pattern.compile(operation);
				matcher = pattern.matcher(line);
				expressionMod=expression;
				
				//System.out.println("Line\t"+line);
				while(matcher.find()) 
				{
					colValue=matcher.group(1);
					//System.out.println("Match\t"+i+"\t"+(String) al.get(i)+"\t"+colValue);
					if(wherePos.contains(i))
					{
						//System.out.println("1\t"+expressionMod);
						if(!utility.isNumeric(colValue))
							expressionMod=expressionMod.replace((String) al.get(i),"'"+colValue+"'");
						else
							expressionMod=expressionMod.replace((String) al.get(i),colValue);
						
						//System.out.println("2\t"+expressionMod);
					}
					i++;
				}
				expressionMod=expressionMod.replace("=", "==");
				//System.out.println("Final\t"+expressionMod);
				String result=evaluator.evaluation(expressionMod, null);
				//System.out.println(result);
				if(result=="false")
				{
					fw.write("<tr>"+line+"</tr>\n");
				}
				else
				{
					found=true;
				}

			    
			}

			//Thread.sleep(1000);
		    fw.flush();
		    fw.close();
		    //Thread.sleep(1000);
			reader.close();

			file.delete();
			fileTemp.renameTo(file);		
			if(found) msg="Row deleted";
			else msg="Row not deleted";

		    
		}
	    catch(Exception e) 	{fileTemp.delete();return "Row not deleted";}
	    return msg;

		
	    		
	}

	
	public String update(String tableName, String colName, String newValue, String expression)
	{
		String msg="";
		
		//System.out.println(tableName+"\t"+colName+"\t"+newValue+"\t"+expression);
		Storage store=new Storage();
		Utility utility=new Utility();
		Evaluator evaluator=new Evaluator();
		
		String fileName=this.createFileName(tableName);
		String fileNameTemp=this.createFileNameTemp(tableName);
	    File fileTemp = new File(fileNameTemp);
	    File file = new File(fileName);
	    
	    ArrayList al=store.getHeaders(fileName);
		ArrayList wherePos=utility.getWherePositions(expression, al);
		//System.out.println(al.toString());
		int updatePos=utility.getUpdatePosition(colName, al);
		if(updatePos<0) return "";
		
	    String operation;
	    Pattern pattern;
		Matcher matcher;
		String line;
		String colValue;
		boolean found=false;
		String expressionMod;
		int i=0;
		
		
	    try
		{
	    	
	    	FileWriter fw = new FileWriter(fileTemp,false);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			line = reader.readLine();
			fw.write(line+"\n");
			
			while ((line = reader.readLine()) != null)
			{
				i=0;
				line=line.substring(4, line.length()-5);
				operation = "<td>(\\w+)</td>";
				pattern = Pattern.compile(operation);
				matcher = pattern.matcher(line);
				expressionMod=expression;
				
				//System.out.println("Line\t"+line);
				while(matcher.find()) 
				{
					colValue=matcher.group(1);
					//System.out.println("Match\t"+i+"\t"+(String) al.get(i)+"\t"+colValue);
					if(wherePos.contains(i))
					{
						//System.out.println("1\t"+expressionMod);
						if(!utility.isNumeric(colValue))
							expressionMod=expressionMod.replace((String) al.get(i),"'"+colValue+"'");
						else
							expressionMod=expressionMod.replace((String) al.get(i),colValue);
							
						//System.out.println("2\t"+expressionMod);
					}
					i++;
				}
				expressionMod=expressionMod.replace("=", "==");
				//System.out.println("Final\t"+expressionMod);
				String result=evaluator.evaluation(expressionMod, null);
				//System.out.println(result);
				if(result=="true")
				{
					i=0;
					found=true;
					matcher.reset();
					fw.write("<tr>");
					while(matcher.find()) 
					{
						colValue=matcher.group(1);
						if(i==updatePos)
						{
							fw.write("<td>"+newValue.substring(1, newValue.length()-1)+"</td>");
						}
						else
						{
							fw.write("<td>"+colValue+"</td>");
						}
						i++;
					}
					fw.write("</tr>\n");
					
				}
				else
				{
					fw.write("<tr>"+line+"</tr>\n");
				}

			    
			}

			//Thread.sleep(1000);
		    fw.flush();
		    fw.close();
		    //Thread.sleep(1000);
			reader.close();

			file.delete();
			fileTemp.renameTo(file);		
			if(found) msg="Table updated";
			else msg="Table not updated";

		    
		}
	    catch(Exception e) 	{fileTemp.delete(); return "Table not updated";}
	    return msg;

		
	}
	
	public String insert(String tableName, String[] colValue)
	{
		if(currDB==null) return "No database selected";
		int n=colValue.length;
		if(n<=0) return "No columns";
		String msg="";
		
		String fileName = this.createFileName(tableName);
	    File file = new File(fileName);
	    
		Storage store=new Storage();
	    if(!store.matchColumns(fileName, colValue)) { return "Number of columns does not match";}
	    
	    if(!store.compare(fileName, colValue)) {return "Duplicate row";}
			
	    if (!file.exists()) {return "Table does not exists";};

		try {
		    if (store.writeFields(file,colValue,true,"td")) 
		    {
		    	msg="New row added";
		    }
		    else
		    {
		    	msg="Row not added";
		    }
		    
		    
		  }
		  catch (IOException e) {
		    e.printStackTrace();
			  return "Row not added";
		  }
		return msg;
	
	}

	public String modifyColumn(String tableName, String colName, String newName)
	{
		String msg="";
		
		String fileName=this.createFileName(tableName);
		String fileNameTemp=this.createFileNameTemp(tableName);
	    File fileTemp = new File(fileNameTemp);
	    File file = new File(fileName);
	    
	    String operation;
	    Pattern pattern;
		Matcher matcher;
		String line;
		String colValue;
		boolean found=false;

	    try
		{
			FileWriter fw = new FileWriter(fileTemp,true);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			//first line (header)
			line = reader.readLine();
			line=line.substring(4, line.length()-5);
			//System.out.println(line);
			
			operation = "<th>(\\w+)</th>";
			pattern = Pattern.compile(operation);
			matcher = pattern.matcher(line);

			fw.write("<tr>");
			while(matcher.find()) 
			{
				colValue=matcher.group(1);
				//System.out.println(colName);
				
				if(colName.equals(colValue))
				{
					fw.write("<th>"+newName+"</th>");
					found=true;
				}
				else
				{
					fw.write("<th>"+colValue+"</th>");
				}
								
			}
			fw.write("</tr>\n");

			while ((line = reader.readLine()) != null)
			{
				fw.write(line);
			}

			reader.close();
		    fw.flush();
		    fw.close();

		}
	    catch(Exception e) 	{e.printStackTrace();fileTemp.delete();return "Column not modified";}

	    
	    file.delete();
	    fileTemp.renameTo(file);
	    if(found) msg="Column modified";
	    msg="Column not found";
	    return msg;
	}
	
	public String addColumn(String tableName, String colName)
	{
		String msg="";
		
		String filename=this.createFileName(tableName);
		String filenameTemp=this.createFileName(tableName+"_temp");
		
	    File fileTemp = new File(filenameTemp);
	    File file = new File(filename);
		
		try
		{
			FileWriter fw = new FileWriter(fileTemp,true);

			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			line = reader.readLine();
			line=line.substring(0, line.length()-5);
			//System.out.println(line);
			line+="<th>"+colName+"</th></tr>";
			fw.write(line);

			
			while ((line = reader.readLine()) != null)
			{
				line=line.substring(0, line.length()-5);
				//System.out.println(line);
				line+="<td></td></tr>";
				fw.write(line);

			}
			
			reader.close();
		    fw.flush();
		    fw.close();
		    
		    file.delete();
		    fileTemp.renameTo(file);
		    msg="Column added to table";
		}
		catch(Exception e) 	{e.printStackTrace();fileTemp.delete();return "Column not added to table";}
		
		return msg;
		
	}

	
	public String dropColumn(String tableName, String colName)
	{
		//System.out.println("\tdrop column");
		String filename=this.createFileName(tableName);
		String filenameTemp=this.createFileName(tableName+"_temp");
		String msg="";
		
	    File fileTemp = new File(filenameTemp);
	    File file = new File(filename);
	    
	    int i=0;
	    int pos=0;
	    boolean found=false;

	    String operation;
	    Pattern pattern;
		Matcher matcher;
		
	    try
		{
			FileWriter fw = new FileWriter(fileTemp,true);

			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			String colValue;

			//first line (header)
			line = reader.readLine();
			line=line.substring(4, line.length()-5);
			//System.out.println(line);
			
			operation = "<th>(\\w+)</th>";
			pattern = Pattern.compile(operation);
			matcher = pattern.matcher(line);

			fw.write("<tr>");
			while(matcher.find()) 
			{
				colValue=matcher.group(1);
				//System.out.println(colName);
				
				if(colName.equals(colValue))
				{
					pos=i;
					found=true;
				}
				else
				{
					fw.write("<th>"+colValue+"</th>");
				}
				i++;
				
			}
			fw.write("</tr>\n");

			//body
			i=0;
			while ((line = reader.readLine()) != null)
			{
				line=line.substring(4, line.length()-5);

				operation = "<td>(\\w+)</td>";
				pattern = Pattern.compile(operation);
				matcher = pattern.matcher(line);

				fw.write("<tr>");
				while(matcher.find()) 
				{
					
					colValue=matcher.group(1);
					//System.out.println(colName);
					
					if(pos!=i)
					{
						fw.write("<td>"+colValue+"</td>");
					}
				i++;	
				}
				fw.write("</tr>\n");

				
			}
			

			reader.close();
		    fw.flush();
		    fw.close();
		    
		    file.delete();
		    fileTemp.renameTo(file);
		    if(found) msg="Column deleted";
		    else msg="Column not found";

		}
		catch(Exception e) 	{e.printStackTrace();fileTemp.delete();return "Column not deleted";}
	    return msg;
	    
	}

	
	public String showColumns(String tableName)
	{
		String operation=new String();
		String filename=this.createFileName(tableName);
		String msg="";
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String linea;
			linea = reader.readLine();
			{
				operation = "<th>(\\w+)</th>";
				Pattern pattern = Pattern.compile(operation);
				Matcher matcher = pattern.matcher(linea);
				while(matcher.find()) {
					String colName=matcher.group(1);
					msg+=colName;
				}
			}
			reader.close();
		}
		catch(Exception e) 	{
			//e.printStackTrace();
			}
		return msg;
	}

	
	
	public String showTables()
	{
		String msg="";
		File path = new File(dataPath+currDB);
		if(path.exists()) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                  if(!files[i].isDirectory()) {
                      msg+=files[i].getName().substring(0, files[i].getName().toString().length()-5)+"<br>";
                  }
            }    		
		}
		return msg;
				
	}

	public String dropTable(String tableName)
	{
		String fileName=this.createFileName(tableName);
		String msg="";
		//System.out.println(fileName);
		boolean result=false;
		File path = new File(fileName);
		if(path.exists()) 
		{
			//path.deleteOnExit();
			//System.out.println("file exists");
        	result = path.delete();
		}
		
		if(result==true)
		{
			msg="Table deleted";
		}
		else
		{
			msg="Table not deleted";			
		}
		return msg;
				
	}

	public String createTable(String tableName,String[] colName)
	{
		if(currDB==null) return "";
		int n=colName.length;
		if(n<=0) return "";
		String msg="";
		
		String filename = this.createFileName(tableName);
	    File file = new File(filename);
	    Storage store=new Storage();
	    Utility ut=new Utility();
	    boolean check=ut.checkNames(colName);
	    if(check==false) {return "Duplicate column";};
	    	
	    if(file.exists()) {return "Table already exists";};

		try {
		    if (file.createNewFile() && store.writeFields(file,colName,false,"th")) 
		    {
		    	msg="Table created";		    
		    }
		    else
		    {
		    	msg="Table not created";
		    	file.delete();
		    }
		 }
		  catch (IOException e) {
		    e.printStackTrace();}
		
		return msg;
		
	}
	

	
	public String useDatabase(String dbName)
	{
		File path = new File(dataPath+dbName);
		String msg="";
		
		boolean success = path.exists();
	    if (success)
	    {
	    	msg="Database changed";
	    	currDB=dbName;
	    }
	    else
	    {
	    	msg="Database does not exist";
	    }
	    return msg;
		
				
	}

	public String showDatabases()
	{
		String msg="";
	    
		//int n=dataPath.length();
		File path = new File(dataPath);
		if(path.exists()) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                  if(files[i].isDirectory()) {
                	  msg+=files[i].getName()+"<br>";
                  }
            }
    		
		}
				
		return msg;
	}

	
	public String dropDatabase(String dbName)
	{
		boolean result=false;
		String msg="";
		
		File path = new File(this.dataPath+dbName);
		if(path.exists()) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                  if(files[i].isDirectory()) {
                      this.dropDatabase(files[i].toString());
                  }
                  else {
                      files[i].delete();
                  }
            }
    		result = path.delete();
		}
		
		if(result==true)
		{
			msg="Database deleted";
		}
		else
		{
			msg="Database not deleted";
		}
	
		return msg;
				
	}

	public String createDatabase(String dbName)
	{
		String msg="";
		boolean result = (new File(this.dataPath+dbName)).mkdir();
		if(result==true)
		{
			msg="Database created";
		}
		else
		{
			msg="Database not created";
		}
		return msg;
		
	}
	
	private String createFileName(String tableName)
	{
		String filename = dataPath+currDB + File.separator + tableName+ext;
		return filename;	
	}

	private String createFileNameTemp(String tableName)
	{
		Utility ut=new Utility();
		String ts=ut.timeStamp();
		String filename = dataPath+currDB + File.separator + tableName+"_"+ts+ext;
		return filename;	
	}

	public Engine()
	{
		String dataDir="data";
		boolean result = (new File(dataDir)).mkdir();
		dataPath=System.getProperty("user.dir")+File.separator+dataDir+File.separator;
		currDB=null;
		
		
	}
}
