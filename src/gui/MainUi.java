package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import backtracking.TaskManagerBacktracking;
import data_structures.Constraint;
import data_structures.Task;
import data_structures.TaskManager;
import exceptions.ConstraintException;
import exceptions.MalformedConstraintException;
import exceptions.MalformedTaskException;
import exceptions.ScheduleNotExistingException;
import exceptions.TaskException;

public final class MainUi
{
	private static final int TASK_NAME_MAX_LENGHT = 0x14;
	private static final Dimension WINDOW_DIMENSION = new Dimension(720, 350);
	
	private static final String SCHEDULE_TEXT = "SCHEDULATE",
			 					CLEAR_TEXT = "CLEAR";
	
	private TaskManager taskManager = new TaskManagerBacktracking();
	
	private JFrame frame = new JFrame();
	
	private JTextField taskField, 
					   constraintField;
	
	private JMenuBar menuBar = new JMenuBar();

	private JMenu menuFile = new JMenu("File"),
				  menuEdit = new JMenu("Edit"),
				  menuUtil = new JMenu("Util");
	
	private JMenuItem closeItem = new JMenuItem("Close..."),
					  openItem = new JMenuItem("Open Schedule Result..."),
					  clearItem = new JMenuItem("Clear...");
	
	private JButton btnAddTask,
					btnAddConstraint,
					btnSchedulate;
	
	private JTextArea displayTasks,
					  displayConstraints;
	
	private final Dimension SCREEN_DIMENSION;
	
	
	public MainUi() 
	{
		setLookAndFeel();
		
		final Font font = new Font("Tahoma", Font.PLAIN, 0xc);
		
		SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
	
		menuFile.add(closeItem);
		menuUtil.add(openItem);
		menuEdit.add(clearItem);
		
		openItem.addActionListener(e ->
		{
			new LoadScheduleResultUi().show();
		});
		
		clearItem.addActionListener(e ->
		{
			reset();
		});
		
		closeItem.addActionListener(e -> 
		{
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		});
		
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		menuBar.add(menuUtil);
		
		frame.setJMenuBar(menuBar);
		
		frame.getContentPane().setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		
		topPanel.setLayout(new GridLayout(0x2, 0x2, 0x2, 0x2));
		
		JPanel panelTasks = new JPanel();
		panelTasks.setLayout(new FlowLayout(FlowLayout.CENTER, 0x5, 0x5));
		panelTasks.setPreferredSize(new Dimension(350, 0xa));
		
		JLabel lblTask = new JLabel("Task:");
		panelTasks.add(lblTask);
		
		taskField = new JTextField();
		taskField.setColumns(0xa);
		taskField.addKeyListener(new KeyListenerAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					addTask();
				}
			}
		});
		panelTasks.add(taskField);
		
		btnAddTask = new JButton("Add Task");
		btnAddTask.addActionListener(e ->
		{
			addTask();
		});
		panelTasks.add(btnAddTask);
		
		topPanel.add(panelTasks);
		
		JPanel panelConstraints = new JPanel();
		panelConstraints.setLayout(new FlowLayout(FlowLayout.CENTER, 0x5, 0x5));
		panelConstraints.setPreferredSize(new Dimension(350, 0xa));
		
		JLabel lblConstraint = new JLabel("Constraint:");
		panelConstraints.add(lblConstraint);
		
		constraintField = new JTextField();
		constraintField.setColumns(0xa);
		constraintField.addKeyListener(new KeyListenerAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					addConstraint();
				}
			}
		});
		panelConstraints.add(constraintField);
		
		btnAddConstraint = new JButton("Add Constraint");
		btnAddConstraint.addActionListener(e -> 
		{
			addConstraint();
		});
		panelConstraints.add(btnAddConstraint);

		topPanel.add(panelConstraints);
		
		JPanel panelOutputTask = new JPanel();
		panelOutputTask.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelOutputTask.setLayout(new BorderLayout(0x5, 0x5));
		
		JLabel lblCreatedTasks = new JLabel("Tasks:");
		panelOutputTask.add(lblCreatedTasks, BorderLayout.NORTH);
		
		displayTasks = new JTextArea();
		displayTasks.setEditable(false);
		displayTasks.setFont(font);
		panelOutputTask.add(new JScrollPane(displayTasks), BorderLayout.CENTER);
		
		topPanel.add(panelOutputTask);
		
		frame.getContentPane().add(topPanel, BorderLayout.CENTER);
		
		JPanel panelOutputConstraint = new JPanel();
		panelOutputConstraint.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelOutputConstraint.setLayout(new BorderLayout(0x5, 0x5));
		
		JLabel lblCreatedConstraints = new JLabel("Constraints:");
		panelOutputConstraint.add(lblCreatedConstraints, BorderLayout.NORTH);
		
		displayConstraints = new JTextArea();
		displayConstraints.setEditable(false);
		displayConstraints.setFont(font);
		panelOutputConstraint.add(new JScrollPane(displayConstraints), BorderLayout.CENTER);
		
		topPanel.add(panelOutputConstraint);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		btnSchedulate = new JButton("SCHEDULATE");
		btnSchedulate.addActionListener(e ->
		{
			if(btnSchedulate.getText().equals(SCHEDULE_TEXT))
			{
				invokeResolve();
			}
			else
			{
				reset();
			}
		});
		controlPanel.add(btnSchedulate);
		
		frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
		
		frame.setTitle("Task Scheduler by @t0re199");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setBounds(SCREEN_DIMENSION.width / 0x2 - WINDOW_DIMENSION.width / 0x2,
				SCREEN_DIMENSION.height / 0x2 - WINDOW_DIMENSION.height / 0x2, 
				WINDOW_DIMENSION.width, WINDOW_DIMENSION.height);
	}
	
	private void invokeResolve()
	{
		setAddButtonsStatus(false);
		btnSchedulate.setEnabled(false);
		try
		{
			((TaskManagerBacktracking) taskManager).solve();
			if(taskManager.scheduleHasBeenFound())
			{
				SwingUtilities.invokeLater(() ->
				{
					new ResultUi(taskManager).show();
				});
			}
			else
			{
				throw new ScheduleNotExistingException();
			}
		}
		catch (ScheduleNotExistingException snee)
		{
			JOptionPane.showMessageDialog(frame, "Check your constraints, then retry!",
					 "No Schdule Was Found", JOptionPane.WARNING_MESSAGE);
		}
		btnSchedulate.setText(CLEAR_TEXT);
		btnSchedulate.setEnabled(true);
	}
	
	private void addTask()
	{
		String data = taskField.getText().trim();
		if(data.length() > TASK_NAME_MAX_LENGHT)
		{
			JOptionPane.showMessageDialog(frame, "Max supported lenght is " + TASK_NAME_MAX_LENGHT + "chars",
					 "Task name is too long", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try
		{
			Task task = taskManager.newTask(data);
			displayTasks.append(task.toString() + "\n\n");
			taskField.setText("");
		}
		catch (TaskException te) 
		{
			if(te instanceof MalformedTaskException)
			{
				JOptionPane.showMessageDialog(frame, "Tasks must be expressed in the following form: (name, lenght) or name, lenght",
							"Malformed Task", JOptionPane.ERROR_MESSAGE);
			}
			else 
			{
				JOptionPane.showMessageDialog(frame, "Reason: " +te.getMessage(),
					 "Task Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void addConstraint()
	{
		String data = constraintField.getText().trim();
		try
		{
			Constraint constraint = taskManager.newConstraint(data);
			displayConstraints.append(constraint.toString() + "\n\n");
			constraintField.setText("");
		}
		catch (ConstraintException ce) 
		{
			if(ce instanceof MalformedConstraintException)
			{
				JOptionPane.showMessageDialog(frame, "Contraints must be expressed in the following form: i|f,t1,p|d,i|f,t2",
						 "Malformed Constraint", JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Reason: " + ce.getMessage(),
						 "Constraint Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void setAddButtonsStatus(boolean flag)
	{
		btnAddTask.setEnabled(flag);
		btnAddConstraint.setEnabled(flag);
	}
	
	private void reset()
	{
		displayConstraints.setText("");
		displayTasks.setText("");
		taskField.setText("");
		constraintField.setText("");
		btnSchedulate.setText(SCHEDULE_TEXT);
		btnSchedulate.setEnabled(true);
		taskManager.clear();
		setAddButtonsStatus(true);
	}
	
	public void show()
	{
		frame.setVisible(true);
	}
	
	private static final void setLookAndFeel()
	{	
		try
		{	
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)
	{
		new MainUi().show();
	}
}