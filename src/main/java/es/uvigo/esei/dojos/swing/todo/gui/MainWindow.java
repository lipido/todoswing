package es.uvigo.esei.dojos.swing.todo.gui;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.Box.createVerticalStrut;
import static java.lang.Math.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import es.uvigo.esei.dojos.swing.todo.core.TodoList;

public class MainWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JPanel mainContentPane;
	private JPanel newTaskControls;
	private JButton addTaskButton;
	private JTextField newTaskField;
	private JScrollPane taskListScrollPane;
	private JPanel taskListControls;
	private JButton upButton;
	private JButton deleteButton;
	private JButton downButton;
	private JList<String> taskList;
	private JLabel statusBar;
		
	private TodoList todoList;
	private TodoListModel todoListModel;

	public MainWindow(){
		
		this.todoList = new TodoList();
		this.todoListModel = new TodoListModel(this.todoList);
		
		this.setContentPane( this.getMainContentPane() );
		
		this.setTitle("Todo list");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setMinimumSize(new Dimension(250, 250));
		
		this.pack();
	}

	private Container getMainContentPane() {
		if (mainContentPane == null) {
			this.mainContentPane = new JPanel();
			this.mainContentPane.setLayout(new BorderLayout());
			
			this.mainContentPane.add(getNewTaskControls(), BorderLayout.NORTH);
			this.mainContentPane.add(getTasksListScrollPane(), BorderLayout.CENTER);
			this.mainContentPane.add(getTasksListControls(), BorderLayout.EAST);
			this.mainContentPane.add(getStatusBar(), BorderLayout.SOUTH);
			
		}
		return this.mainContentPane;
	}

	private Component getNewTaskControls() {
		if (this.newTaskControls == null) {
			this.newTaskControls = new JPanel();
			
			BorderLayout layout = new BorderLayout();
			this.newTaskControls.setLayout(layout);
			layout.setHgap(5);
			this.newTaskControls.setBorder(createEmptyBorder(10,0,10,10));
			
			this.newTaskControls.add(getNewTaskField(), BorderLayout.CENTER);
			this.newTaskControls.add(getAddTaskButton(), BorderLayout.EAST);
		}
		
		return this.newTaskControls;
	}

	private JTextField getNewTaskField() {
		if (this.newTaskField == null) {
			this.newTaskField = new JTextField();
		}
		return this.newTaskField;
	}

	private Component getTasksListScrollPane() {
		if (this.taskListScrollPane == null) {
			this.taskListScrollPane = new JScrollPane(getTaskList());			
		}
		
		return this.taskListScrollPane;
	}

	private JList<String> getTaskList() {
		if (this.taskList == null) {
			this.taskList = new JList<>();
			this.taskList.setModel(this.todoListModel);
		}
		
		return this.taskList;
	}

	private Component getTasksListControls() {
		if (this.taskListControls == null) {
			this.taskListControls = new JPanel();
			
			BoxLayout layout = new BoxLayout(this.taskListControls, BoxLayout.Y_AXIS);
			this.taskListControls.setLayout(layout);
			this.taskListControls.setBorder(createEmptyBorder(5, 5, 5, 5));
			
			JButton button = getUpButton();
			button.setAlignmentX(CENTER_ALIGNMENT);
			this.taskListControls.add(button);
			
			this.taskListControls.add(createVerticalStrut(10));
			
			button = getDeleteButton();
			button.setAlignmentX(CENTER_ALIGNMENT);
			this.taskListControls.add(button);
			
			this.taskListControls.add(createVerticalStrut(10));
			
			button = getDownButton();
			button.setAlignmentX(CENTER_ALIGNMENT);
			this.taskListControls.add(button);
		}
		
		return this.taskListControls;
	}

	private JButton getUpButton() {
		if (this.upButton == null) {
			this.upButton = new JButton("Up");
			this.upButton.setIcon(createIcon("up.png"));
			
			this.upButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					int pos = getTaskList().getSelectedIndex();
					todoListModel.moveUp(pos);
					
					getTaskList().setSelectedIndex(max(0, pos - 1));
				}
			});
		}
		
		return this.upButton;
	}

	private JButton getDeleteButton() {
		if (this.deleteButton == null) {
			this.deleteButton = new JButton("Delete");
			this.deleteButton.setIcon(createIcon("bin.png"));
			
			this.deleteButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					todoListModel.removeAt(getTaskList().getSelectedIndex());
				}
			});
		}
		
		return this.deleteButton;
	}

	private JButton getDownButton() {
		if (this.downButton == null) {
			this.downButton = new JButton("Down");
			this.downButton.setIcon(createIcon("down.png"));
			
			this.downButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					int pos = getTaskList().getSelectedIndex();
					todoListModel.moveDown(pos);
				
					getTaskList().setSelectedIndex(
						min(getTaskList().getModel().getSize() - 1, pos + 1));
				}
			});
		}
		
		return this.downButton;
	}

	private JButton getAddTaskButton() {
		if (this.addTaskButton == null) {
			this.addTaskButton = new JButton("Add");
			this.addTaskButton.setIcon(createIcon("diary.png"));
			
			this.addTaskButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					if (getNewTaskField().getText().length() > 0) {
						todoListModel.add(getNewTaskField().getText().trim());
						
						getNewTaskField().setText("");
						
						getTaskList().setSelectedIndex(getTaskList().getModel().getSize()-1);
					}
				}
			});
		}
		
		return this.addTaskButton;
	}
	
	private JLabel getStatusBar() {
		if (this.statusBar == null) {
			this.statusBar = new JLabel("Number of tasks: 0");
			this.todoListModel.addListDataListener(new ListDataListener() {
				@Override
				public void contentsChanged(ListDataEvent e) {
					updateLabel(e);
				}
				
				private void updateLabel(ListDataEvent e) {
					getStatusBar().setText("Number of tasks: "+((TodoListModel)e.getSource()).getSize());
				}
				
				@Override
				public void intervalRemoved(ListDataEvent e) {}
				@Override
				public void intervalAdded(ListDataEvent e) {}
			});
		}
		
		return this.statusBar;
	}
	
	private Icon createIcon(String iconfilename) {
		return new ImageIcon(
				getClass().
				getResource("/"+iconfilename));
	}
}
