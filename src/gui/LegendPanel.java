package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import data_structures.Task;

public class LegendPanel extends JPanel
{
	private static final long serialVersionUID = -203966022509875007L;
	
	private transient JTable table;
	private transient JScrollPane jsp = new JScrollPane();
	private String[] clmns = {"TaskName", "TaskId", "Started At", "Completed At"};
	private String[][] matrix;
	
	private final Dimension PREFERRED_SIZE;
	
	public LegendPanel(Collection<Task> tasks, Map<String, Integer> map)
	{
		setLayout(new BorderLayout());
		final int N = map.keySet().size();
		matrix = new String[N][0x4];
		int i = 0x0;
		for(Task task : tasks)
		{
			matrix[i][0x0] = task.getName();
			matrix[i][0x1] = Integer.toString(map.get(task.getName()));
			matrix[i][0x2] = Integer.toString(task.getStartInstant());
			matrix[i][0x3] = Integer.toString(task.getCompletationInstant());
			i++;
		}
		DefaultTableModel defaultTableModel = new DefaultTableModel(matrix, clmns);
		table = new JTable(defaultTableModel);
		table.setEnabled(false);
//		table.setFont(new Font("Tahoma", Font.BOLD, 16));
		table.setRowHeight(25);

		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setViewportView(table);
		
		add(jsp,BorderLayout.CENTER);
		
		PREFERRED_SIZE = new Dimension(250 * matrix.length, 400);
	}
	
	@Override
 	public final Dimension getPreferredSize()
 	{
 		return PREFERRED_SIZE;
 	}
}
