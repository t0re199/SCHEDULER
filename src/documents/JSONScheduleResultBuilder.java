package documents;

import org.json.JSONArray;
import org.json.JSONObject;

import data_structures.Task;

public class JSONScheduleResultBuilder implements ScheduleResultBuilder
{
	private JSONObject head;
	private JSONArray content;
	
	private boolean done = false;
	
	@Override
	public void openDocument()
	{
		if(done)
		{
			throw new IllegalStateException();
		}
		head = new JSONObject();
		content = new JSONArray();
	}

	@Override
	public void addTask(Task task)
	{
		JSONArray taskData = new JSONArray();
		JSONObject taskObj = new JSONObject();
		taskData.put(new JSONObject().put(TaskDataKey.getKeyString(TaskDataKey.ID), task.getId()));
		taskData.put(new JSONObject().put(TaskDataKey.getKeyString(TaskDataKey.NAME), task.getName()));
		taskData.put(new JSONObject().put(TaskDataKey.getKeyString(TaskDataKey.LENGHT), task.lenght()));
		taskData.put(new JSONObject().put(TaskDataKey.getKeyString(TaskDataKey.START_INSTANT), task.getStartInstant()));
		taskData.put(new JSONObject().put(TaskDataKey.getKeyString(TaskDataKey.COMPLETATION_INSTANT), task.getCompletationInstant()));
		taskObj.put("task" + task.getId(), taskData);
		content.put(taskObj);
	}
	
	@Override
	public void closeDocument()
	{
		if(done)
		{
			throw new IllegalStateException();
		}
		head.put("tasks", content);
		done = true;
	}	

	@Override
	public ExportableDocument getDocument()
	{
		if(!done)
		{
			throw new RuntimeException("Document is not ready!");
		}
		return new JSONExportableDocument(head);
	}
}
