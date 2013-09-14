package com.friendlyblob.mayhemandhell.client.mapeditor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ObjectPanel extends JPanel implements ActionListener, MouseListener {
	private JTable objectTable;

    private String[] columnNames = {"id",
            "title",
            "collision",
            "width",
            "height",
            "centerpoint",
            "texture"};
	
    private JButton loadObjects;
    private JButton saveObjects;
    
    private MapEditorWindow parent;
    
	/**
	 * Create the panel.
	 */
	public ObjectPanel(MapEditorWindow parent) {
		this.parent = parent;

//	      XmlReader xmlReader = new XmlReader();
//	      XmlReader.Element root = null;
//	      
//			try {
//				root = xmlReader.parse(Gdx.files.internal("data/objects.xml"));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			
//		  XmlReader.Element objects = root.getChildByName("objects");
//		  
//		  XmlReader.Element tmp = null;
//		  int childCount = objects.getChildCount();
//		  
//		  Object[][] data = new Object[childCount][];
//		  for (int i = 0; i < childCount; i++) {
//			tmp = objects.getChild(i);
//			  
//			data[i] = new Object[] { tmp.getAttribute("id"), tmp.getAttribute("title"), tmp.getAttribute("collision"), tmp.getAttribute("width"), tmp.getAttribute("height"), tmp.getAttribute("centerpoint"), tmp.getAttribute("texture")};
//		  }
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		
//		JToolBar toolBar = new JToolBar();
//		toolBar.setFloatable(false);
//		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
//		add(toolBar);
//		
//		loadObjects = new JButton(new ImageIcon("./textures/gui/MapEditor/load.png"));
//		toolBar.add(loadObjects);
//		loadObjects.addActionListener(this);
//		
//	    saveObjects = new JButton(new ImageIcon("./textures/gui/MapEditor/save.png"));
//		toolBar.add(saveObjects);
//		saveObjects.addActionListener(this);
//		  
//		
//		objectTable = new JTable();
//	    objectTable.setModel(new DefaultTableModel(data, columnNames));
//		objectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		objectTable.addMouseListener(this);
//
//		
//		JScrollPane scrollPane = new JScrollPane(objectTable);
//		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
//		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
//		add(scrollPane);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == loadObjects) {

		} else if (source == saveObjects) {
			int selectedRow = parent.zoneTable.getSelectedRow();
			if (selectedRow == -1) { 
				return;
			}
			
			ArrayList<EnvironmentObject> objects = GameWorld.getObjects();
			
			String zoneId = parent.zoneTable.getModel().getValueAt(selectedRow, 0).toString();
			String zoneTitle = parent.zoneTable.getModel().getValueAt(selectedRow, 1).toString();
			
			StringWriter sw = new StringWriter();
			 XmlWriter xmlWriter = new XmlWriter(sw);
			 try {
				 xmlWriter.element("xml")
				        .attribute("version", "1.0")
				        .attribute("encoding", "UTF-8")
				    .element("zone")
				    	.attribute("id", zoneId)
			    	.pop()
	       			.element("objects");
			
				for (EnvironmentObject eo : objects) {
					xmlWriter.element("object")
						.attribute("id", eo.objectId)
						.attribute("type", eo.type)
						.attribute("x", eo.x)
						.attribute("y", eo.y)
					.pop();
				}
	
				xmlWriter.pop().pop();
				xmlWriter.close();
				
				// Save table data to file
				PrintWriter out;
				out = new PrintWriter("data/objects/" + zoneTitle + ".xml");
				out.write(sw.toString());
				out.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1) {
			int selectedRow = objectTable.getSelectedRow();
			if (selectedRow == -1) {
				return;
			}
			
			MapEditor.selectedObject = Integer.parseInt(objectTable.getModel().getValueAt(selectedRow, 0).toString());
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
