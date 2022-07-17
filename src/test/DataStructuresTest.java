package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import backtracking.TaskManagerBacktracking;
import data_structures.Task;
import data_structures.TaskManager;
import exceptions.AlreadyExistingTaskException;
import exceptions.ConstraintContainsInvalidDataException;
import exceptions.ConstraintException;
import exceptions.TaskException;

class DataStructuresTest
{
	private static TaskManager taskManager;
	
	private static Random random;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		taskManager = new TaskManagerBacktracking();
		random = new Random();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		taskManager.clear();
		assertTrue(taskManager.getTasks().isEmpty());
	}

	@Test
	void testTasks()
	{
		int createdTasks = 0x0;
		for(int i = 0x0; i < 0x50; i++)
		{
			String taskName = Integer.toString(random.nextInt(500));
			int taskLenght = random.nextInt(0x20) + 0x1;
			
			try
			{
				Task task = taskManager.newTask(taskName, taskLenght);
				
				createdTasks++;
				
				assertEquals(taskName, task.getName());
				assertEquals(taskLenght, task.lenght());
				
				assertTrue(task.getId() >= 0x0);
				
				assertEquals(task, taskManager.getTask(taskName));
			}
			catch (TaskException te) 
			{
				if(te instanceof AlreadyExistingTaskException)
				{
					assertTrue(taskManager.taskExists(taskName));
				}
			}
			catch (Exception e) 
			{
				Assert.fail(e.toString());
			}
			assertEquals(createdTasks, taskManager.getTasks().size());
		}
	}
	
	@Test
	void testConstraints()
	{
		final char[] orders = {'p', 'd'};
		final char[] states = {'i', 'f'};
		
		final int bound = taskManager.getTasks().size() * 0x2;
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0x0; i < 0x20; i++)
		{
			sb.append(states[random.nextInt(0x2)]);
			sb.append(',');
			int t1 = random.nextInt(bound);
			sb.append(t1);
			sb.append(',');
			sb.append(orders[random.nextInt(0x2)]);
			sb.append(',');
			sb.append(states[random.nextInt(0x2)]);
			sb.append(',');
			int t2 = random.nextInt(bound);
			sb.append(t2);
			try
			{
				taskManager.newConstraint(sb.toString());
			}
			catch (ConstraintException ce) 
			{
				if(ce instanceof ConstraintContainsInvalidDataException)
				{
					boolean condition = !taskManager.taskExists(Integer.toString(t1)) ||
										!taskManager.taskExists(Integer.toString(t2));
					assertTrue(condition);
				}
			}
			catch (Exception e) 
			{
				Assert.fail(e.getMessage());
			}
			finally 
			{
				sb.setLength(0x0);
			}
		}
	}
}