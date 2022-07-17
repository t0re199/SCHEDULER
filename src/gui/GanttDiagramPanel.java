package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;

import data_structures.Task;

public final class GanttDiagramPanel extends JPanel
{
	private static final long serialVersionUID = -4320909436860117780L;

	private LinkedList<Task> tasks;
	
	private final int MAX_TASK_LENGHT,
					  POINT_OFFSET,
				      Y_BORDER_OFFSET = 0xf,
					  Y_TASK_OFFSET = 0x32;
	
	private final Dimension PREFERRED_SIZE;
	
	public GanttDiagramPanel(Collection<Task> tasks, int maxTaskLenght)
	{
		if(tasks == null || maxTaskLenght < 0x0)
		{
			throw new IllegalArgumentException();
		}
		this.tasks = new LinkedList<>(tasks);
		this.MAX_TASK_LENGHT = maxTaskLenght;
		POINT_OFFSET = (0xf * Integer.toString(maxTaskLenght).length()) + 0xa;
		int dimX = (MAX_TASK_LENGHT * 0x2) + (POINT_OFFSET) * MAX_TASK_LENGHT;
		int dimY = (tasks.size() * 0x30) + Y_TASK_OFFSET;
		PREFERRED_SIZE = new Dimension(dimX > 0x0 ? dimX : 400, dimY > 0x0 ? dimY : 200);
		setBackground(Color.WHITE);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(15f));
		drawInstants(g2D);
		drawTasks(g2D);
	}
	
	private void drawInstants(Graphics g)
	{
		for(int i = 0x0; i < MAX_TASK_LENGHT; i++)
 		{
 			g.drawString(Integer.toString(i), (POINT_OFFSET * i) + POINT_OFFSET, Y_BORDER_OFFSET);
 		}
	}
	
	private void drawTasks(Graphics g)
	{
		int i = 0x1;
		for(Task task : tasks)
		{
			drawTask(g, task, i++);
		}
	}
	
	private void drawTask(Graphics g, Task task, int i)
	{
		g.drawString(task.getId() + "",  0x0, Y_TASK_OFFSET * i);
		g.drawLine((POINT_OFFSET * task.getStartInstant()) + POINT_OFFSET, 
					(Y_TASK_OFFSET * i) - 0x5, 
					(POINT_OFFSET * task.getCompletationInstant()) + POINT_OFFSET, 
					(Y_TASK_OFFSET * i) - 0x5);
	}
	
	@Override
 	public final Dimension getPreferredSize()
 	{
 		return PREFERRED_SIZE;
 	}
}