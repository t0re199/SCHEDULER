package io.transfer_in;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import data_structures.Task;

public interface ScheduleResultLoader
{
	void load(File file);
	
	Collection<Task> getTask();
	
	Map<String, Integer> getMap();
}
