package io.transfer_out;

import java.util.Collection;
import java.util.LinkedList;

import data_structures.Task;
import documents.ScheduleResultBuilder;

public class JSONExternalizer implements Externalizer
{
	private LinkedList<Task> tasks;

	private boolean done = false;
	
	public JSONExternalizer(Collection<Task> tasks)
	{
		this.tasks = new LinkedList<>(tasks);
	}
	
	@Override
	public void build(ScheduleResultBuilder builder)
	{
		if(builder == null)
		{
			throw new IllegalArgumentException();
		}

		if(done)
		{
			return;
		}
		
		builder.openDocument();
		for(Task task : tasks)
		{
			builder.addTask(task);
		}
		builder.closeDocument();
		done = true;
	}
}
