package documents;

import data_structures.Task;

public interface ScheduleResultBuilder
{
	void openDocument();
	
	void addTask(Task task);
	
	void closeDocument();
	
	ExportableDocument getDocument();
}
