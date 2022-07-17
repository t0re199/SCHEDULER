package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import backtracking.TaskManagerBacktracking;
import data_structures.Constraint;
import data_structures.Task;

class SchedulerTest
{
	private static TaskManagerBacktracking taskManager;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		taskManager = new TaskManagerBacktracking();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		if(taskManager.scheduleHasBeenFound())
		{
			for(Task t : taskManager.getTasks())
			{
				assertTrue(t.isCompleted());
			}
			for(Constraint c : taskManager.getConstraints())
			{
				assertTrue(c.isVerified());
			}
		}
	}

	@Test
	void test()
	{
		taskManager.newTask("0", 0x3);
		taskManager.newTask("1", 0x4);
		taskManager.newTask("2", 0x5);
		taskManager.newTask("3", 0x3);
		taskManager.newTask("4", 0x3);
		
		taskManager.newConstraint("i,1,d,f,0");
		taskManager.newConstraint("i,4,d,f,2");
		
		try
		{
			taskManager.solve();
			assertTrue(taskManager.scheduleHasBeenFound());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}