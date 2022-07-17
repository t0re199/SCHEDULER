package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import data_structures.Task;
import data_structures.TaskManager;
import documents.ExportableDocument;
import documents.ScheduleResultBuilder;
import documents.SchedulerResultBuilderFactory;
import documents.SchedulerResultBuilderFactoryImpl;
import io.FileUtils;
import io.transfer_out.Externalizer;
import io.transfer_out.JSONExternalizer;

public class ResultUi
{
	private static final Dimension WINDOW_DIMENSION = new Dimension(700, 450);
	
	private final Dimension	SCREEN_DIMENSION;
	
	private LinkedList<Task> tasks;
	
	private JFrame frame = new JFrame();
	
	private JMenuBar menuBar = new JMenuBar();

	private JMenu menuFile = new JMenu("File");
	
	private JMenuItem exportItem = new JMenuItem("Export..."),
			  		  exportAsItem = new JMenuItem("Export as..."),
			  		  closeItem = new JMenuItem("Close...");
	
	private SchedulerResultBuilderFactory builderFactory = SchedulerResultBuilderFactoryImpl.getInstance();
	
	
	public ResultUi(TaskManager taskManager)
	{
		this(taskManager.getTasks(), taskManager.getRequiredTime(), taskManager.getLegend());
	}
	
	public ResultUi(Collection<Task> tasks, int maxTaskLenght, Map<String,Integer> map)
	{
		setLookAndFeel();
		
		this.tasks = new LinkedList<>(tasks);
	
		SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.getContentPane().setLayout(new GridLayout(0x2, 0x1, 0x5, 0xf));
		
		exportItem.addActionListener(e ->
		{
			ExportableDocument doc = getExternableDocument();
			FileUtils.export(doc);
		});
		
		exportAsItem.addActionListener(e ->
		{
			ExportableDocument doc = getExternableDocument();
			FileUtils.exportWithName(doc);
		});
		
		closeItem.addActionListener(e ->
		{
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		});
		
		menuFile.add(exportItem);
		menuFile.add(exportAsItem);
		menuFile.add(closeItem);
		
		menuBar.add(menuFile);
		
		frame.setJMenuBar(menuBar);
		
		GanttDiagramPanel ganttDiagramPanel = new GanttDiagramPanel(tasks, maxTaskLenght);
		
		JScrollPane jspDiagram = new JScrollPane();
		jspDiagram.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspDiagram.getViewport().add(ganttDiagramPanel);
		
		frame.add(jspDiagram);
		
		LegendPanel legendPanel = new LegendPanel(tasks, map);
		
		frame.add(legendPanel, BorderLayout.SOUTH);
		
		frame.setTitle("Scheduler Result");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setBounds(SCREEN_DIMENSION.width / 0x2 - WINDOW_DIMENSION.width / 0x2,
				SCREEN_DIMENSION.height / 0x2 - WINDOW_DIMENSION.height / 0x2, 
				WINDOW_DIMENSION.width, WINDOW_DIMENSION.height);
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
	
	private ExportableDocument getExternableDocument()
	{
		Externalizer externalizer = new JSONExternalizer(tasks);
		ScheduleResultBuilder builder = builderFactory.newBuilder(SchedulerResultBuilderFactory.Type.JSON);
		externalizer.build(builder);
		return builder.getDocument();
	}
}
