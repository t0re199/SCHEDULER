package io.transfer_in;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import data_structures.Task;
import exceptions.TaskParsingException;

public class JSONScheduleResultParser implements Iterable<Task>
{
	private File source;
	private JSONObject jsonObject;
	private JSONArray content;
	private boolean parsed = false;
	
	public JSONScheduleResultParser(String file)
	{
		this(new File(file));
	}
	
	public JSONScheduleResultParser(File file)
	{
		if(file == null || !file.exists() || file.isDirectory())
		{
			throw new IllegalArgumentException();
		}
		if(!file.getName().endsWith(".json"))
		{
			throw new IllegalArgumentException("Invalid File Format! A json was Expected");
		}
		this.source = file;
	}
	
	public void parse() throws IOException
	{
		jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(source.toURI()))));
		content = jsonObject.getJSONArray("tasks");
		parsed = true;
	}
	
	private class JSONTaskParserIterator implements Iterator<Task>
	{
		private Iterator<Object> it;
		
		public JSONTaskParserIterator()
		{
			if(!parsed)
			{
				throw new RuntimeException("Unparsed Context!");
			}
			it = content.iterator();
		}
		
		@Override
		public boolean hasNext()
		{
			return it.hasNext();
		}

		@Override
		public Task next()
		{
			JSONObject obj = (JSONObject) it.next();
			try 
			{
				return Task.parseJSONTask(obj);
			}
			catch (TaskParsingException e) 
			{
				throw e;
			}
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	@Override
	public Iterator<Task> iterator()
	{
		return new JSONTaskParserIterator();
	}
}