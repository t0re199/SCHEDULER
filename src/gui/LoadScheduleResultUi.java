package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import exceptions.JSONFileLoadingException;
import io.FileUtils;
import io.transfer_in.JSONScheduleResultLoader;

public final class LoadScheduleResultUi
{
	private static final Dimension WINDOW_DIMENSION = new Dimension(400, 400);
	
	private final Dimension SCREEN_DIMENSION;
	
	private JFrame frame = new JFrame();
	
	private JSONScheduleResultLoader loader;
	
	public LoadScheduleResultUi()
	{
		setLookAndFeel();
		
		SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.getContentPane().setLayout(new BorderLayout());
		
		JButton btnDragHere = new JButton("Drag A File Here Or Click To Select");
		btnDragHere.addActionListener(e ->
		{
			File file = FileUtils.askJSONFile();
			if(file != null)
			{
				loadFile(file);
				frame.dispose();
			}
		});
		frame.add(btnDragHere, BorderLayout.CENTER);
		
		frame.setDropTarget(new DropTarget()
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) 
			{
				try 
				{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					File file = ((List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)).get(0x0);
					loadFile(file);
					frame.dispose();
				}
				catch (JSONFileLoadingException fle) 
				{
					throw fle;
				}
		        catch(Exception e)
				{
		        	JOptionPane.showMessageDialog(frame, "An error occurred while loading data.", 
		        									"Data Unaviable", JOptionPane.ERROR_MESSAGE);
		        	e.printStackTrace();
				}
			 }
		});
		
		frame.setTitle("Schedule Result Loader");
		
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
	
	private void loadFile(File file)
	{
		if(!file.isFile() || !file.getName().endsWith(".json"))
		{
			throw new RuntimeException();
		}
		loader = new JSONScheduleResultLoader(file);
		loader.load();
		loader.showUi();
	}
	
	public static void main(String[] args)
	{
		new LoadScheduleResultUi().show();
	}
}
