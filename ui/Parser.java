package ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Engine;

public class Parser {

	Engine db=new Engine();
	
	public String parse(String command)
	{

		String operation;
		Pattern pattern;
		Matcher matcher;
		boolean result;

		String dbName;
		String tableName;
		String colsName;
		String colName;
		String values;
		String newValue;
		String expression;
		String newName;
		String target;
		
		operation = "select (.+) from (\\w+) where (.+)";		
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			target=matcher.group(1);
			tableName=matcher.group(2);
			expression=matcher.group(3);
			return db.select(tableName, target,expression);			
			//return true;
		}
		
		operation = "select (.+) from (\\w+)";		
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			target=matcher.group(1);
			tableName=matcher.group(2);
			expression="";
			return db.select(tableName, target,expression);			
			//return true;
		}

		operation = "delete from (\\w+) where (.+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			expression=matcher.group(2);
			return db.delete(tableName, expression);
			//return true;
		}

		operation = "update (\\w+) set (\\w+)=(.+) where (.+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			colName=matcher.group(2);
			newValue=matcher.group(3);
			expression=matcher.group(4);
			return db.update(tableName, colName, newValue, expression);
			//return true;
		}

		operation = "insert into (\\w+) value\\((.+)\\)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			values=matcher.group(2);
			String[] colValue = values.toString().split(",");
			return db.insert(tableName, colValue);
			//return true;
		}

		operation = "alter table (\\w+) modify column (\\w+) (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			colName=matcher.group(2);
			newName=matcher.group(3);
			return db.modifyColumn(tableName, colName, newName);
			//return true;
		}

		operation = "alter table (\\w+) add column (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			colName=matcher.group(2);
			return db.addColumn(tableName, colName);
			//return true;
		}

		operation = "alter table (\\w+) drop column (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			colName=matcher.group(2);
			return db.dropColumn(tableName, colName);
			//return true;
		}

		operation = "drop table (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			return db.dropTable(tableName);
			//return true;
		}

		operation = "create table (\\w+)\\((.+)\\)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			colsName=matcher.group(2);
			String[] column = colsName.toString().split(",");
			return db.createTable(tableName, column);
			//return true;
		}

		operation = "use (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			dbName=matcher.group(1);
			return db.useDatabase(dbName);
			//return true;
		}

		operation = "show databases";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			return db.showDatabases();
			//return true;
		}

		operation = "show tables";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			return db.showTables();
			//return true;
		}

		operation = "show columns from (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			tableName=matcher.group(1);
			return db.showColumns(tableName);
			//return true;
		}

		operation = "drop database (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			dbName=matcher.group(1);
			return db.dropDatabase(dbName);
			//return true;
		}

		operation = "create database (\\w+)";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			dbName=matcher.group(1);
			return db.createDatabase(dbName);
		}

		
		operation = "quit";
		pattern = Pattern.compile(operation);
		matcher = pattern.matcher(command);
		while(matcher.find()) {
			System.out.println("Bye");
			System.exit(1);
		}
		
		return "";
		
	}
}
