package io.transfer_in;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.SwingUtilities;

import data_structures.Task;
import exceptions.JSONFileLoadingException;
import exceptions.TaskParsingException;
import gui.ResultUi;

public class JSONScheduleResultLoader implements ScheduleResultLoader
{
	private JSONScheduleResultParser parser;
	
	private File source;
	private boolean done = false;
	
	private LinkedList<Task> list = new LinkedList<>();
	private HashMap<String, Integer> map = new HashMap<>();
	
	public JSONScheduleResultLoader(String path)
	{
		this(new File(path));
	}
	
	public JSONScheduleResultLoader(File file)
	{
		if(file == null || !file.exists() || file.isDirectory())
		{
			throw new IllegalArgumentException();
		}
		source = file;
		parser = new JSONScheduleResultParser(source);
	}
	
	public void load()
	{
		load(source);
	}
	
	@Override
	public void load(File file)
	{
		if(done)
		{
			throw new RuntimeException("Already Loaded!");
		}
		try
		{
			parser.parse();
			Iterator<Task> it = parser.iterator();
			while(it.hasNext())
			{
				Task t = it.next();
				list.add(t);
				map.put(t.getName(), t.getId());
			}
			done = true;
		}
		catch (IOException e) 
		{
			throw new JSONFileLoadingException();
		}
		catch (TaskParsingException tpe) 
		{
			throw new TaskParsingException();
		}
	}

	@Override
	public Collection<Task> getTask()
	{
		if(!done)
		{
			throw new IllegalStateException();
		}
		return list;
	}

	@Override
	public Map<String, Integer> getMap()
	{
		if(!done)
		{
			throw new IllegalStateException();
		}
		return map;
	}
	
	public void showUi()
	{
		Comparator<Task> comparator = (t1,t2) ->
		{
			return t1.getCompletationInstant() - t2.getCompletationInstant();
		};
		
		SwingUtilities.invokeLater(() ->
		{
			new ResultUi(list, Collections.max(list, comparator).getCompletationInstant() + 0x1, map).show();
		});
	}
}
