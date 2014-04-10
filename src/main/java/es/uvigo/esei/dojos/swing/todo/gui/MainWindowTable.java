package es.uvigo.esei.dojos.swing.todo.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import es.uvigo.esei.dojos.swing.todo.core.TodoList;

public class MainWindowTable extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public JPanel mainContentPane;
	private JPanel newTaskControls;
	private JButton addTaskButton;
	private JTextField newTaskField;
	private JScrollPane taskListScrollPane;
	private JPanel taskListControls;
	private JButton upButton;
	private JButton deleteButton;
	private JButton downButton;
	private JTable tasksTable;
	
	private TodoList todoList;
	private TodoTableModel todoTableModel;
	
	private class TodoTableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
		
		private TodoList list;
		
		public TodoTableModel(TodoList list) {
			this.list = list;
		}
		public void moveUp(int i){
			this.list.moveUp(i);
			this.fireTableRowsUpdated(i-1, i);
		}
		
		public void moveDown(int i){
			this.list.moveDown(i);
			this.fireTableRowsUpdated(i, i+1);
		}
		
		public void removeAt(int i) {
			this.list.removeAt(i);
			this.fireTableRowsDeleted(i, i);
		}
		
		public void add(String task) {
			this.list.add(task);
			this.fireTableRowsInserted(this.list.size()-1, this.list.size()-1);
		}
		
		@Override
		public Object getValueAt(int arg0, int arg1) {
			return this.list.elementAt(arg0);
		}
		
		@Override
		public int getColumnCount() {
			return 1;
		}
		@Override
		public int getRowCount() {
			return this.list.size();
		}
		@Override
		public String getColumnName(int column) {
			switch(column) {
			case 0: 
				return "Task name"; 
			}
			
			return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			this.list.editAt(rowIndex, aValue.toString());
		}
		
	}
	public MainWindowTable(){
		
		this.todoList = new TodoList();
		this.todoTableModel = new TodoTableModel(this.todoList);
		
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
			
		}
		return this.mainContentPane;
	}
	
	private Component getNewTaskControls() {
		if (this.newTaskControls == null) {
			this.newTaskControls = new JPanel();
			
			BorderLayout layout = new BorderLayout();
			this.newTaskControls.setLayout(layout);
			layout.setHgap(5);
			this.newTaskControls.setBorder(BorderFactory.createEmptyBorder(10,0,10,10));
			
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
		if (this.taskListScrollPane == null){
			this.taskListScrollPane = new JScrollPane(getTasksTable());			
		}
		return this.taskListScrollPane;
	}

	private JTable getTasksTable() {
		if (this.tasksTable == null) {
			this.tasksTable = new JTable();			
			
			this.tasksTable.setModel(this.todoTableModel);
		}
		return this.tasksTable;
	}

	private Component getTasksListControls() {
		if (this.taskListControls == null){
			this.taskListControls = new JPanel();
			
			BoxLayout layout = new BoxLayout(this.taskListControls, BoxLayout.Y_AXIS);
			this.taskListControls.setLayout(layout);
			this.taskListControls.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			JButton button = getUpButton();
			button.setAlignmentX(CENTER_ALIGNMENT);
			this.taskListControls.add(button);
			this.taskListControls.add(Box.createRigidArea(new Dimension(0, 10)));
			
			button = getDeleteButton();
			button.setAlignmentX(CENTER_ALIGNMENT);
			this.taskListControls.add(button);
			this.taskListControls.add(Box.createRigidArea(new Dimension(0, 10)));
			
			button = getDownButton();
			button.setAlignmentX(CENTER_ALIGNMENT);
			this.taskListControls.add(button);
			
		}
		
		return this.taskListControls;
	}

	private JButton getDownButton() {
		if (this.downButton == null) {
			this.downButton = new JButton("Down");
			this.downButton.setIcon(new ImageIcon(getClass().getResource("/es/uvigo/esei/dojos/swing/todo/gui/down.png")));
			
			this.downButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					int pos = getTasksTable().getSelectedRow();
					todoTableModel.moveDown(pos);
				}
			});
		}
		return this.downButton;
	}

	private JButton getDeleteButton() {
		if (this.deleteButton == null) {
			this.deleteButton = new JButton("Delete");
			this.deleteButton.setIcon(new ImageIcon(getClass().getResource("/es/uvigo/esei/dojos/swing/todo/gui/bin.png")));
			
			this.deleteButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					todoTableModel.removeAt(getTasksTable().getSelectedRow());
				}
			});
		}
		return this.deleteButton;
	}

	private JButton getUpButton() {
		if (this.upButton == null) {
			this.upButton = new JButton("Up");
			this.upButton.setIcon(new ImageIcon(getClass().getResource("/es/uvigo/esei/dojos/swing/todo/gui/up.png")));
			
			this.upButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					int pos = getTasksTable().getSelectedRow();
					todoTableModel.moveUp(pos);
					
					
				}
			});
		}
		return this.upButton;
	}
	
	private JButton getAddTaskButton() {
		if (this.addTaskButton == null) {
			this.addTaskButton = new JButton("Add");
			this.addTaskButton.setIcon(new ImageIcon(getClass().getResource("/es/uvigo/esei/dojos/swing/todo/gui/diary.png")));
			
			this.addTaskButton.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					if (getNewTaskField().getText().length() > 0) {
						todoTableModel.add(getNewTaskField().getText().trim());
						
						getNewTaskField().setText("");
						
						//updateTaskList();
						
					}
				}
			});
		}
		return this.addTaskButton;
	}
}
