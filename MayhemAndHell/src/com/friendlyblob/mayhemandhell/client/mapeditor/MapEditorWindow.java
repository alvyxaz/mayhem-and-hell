package com.friendlyblob.mayhemandhell.client.mapeditor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.Component;

public class MapEditorWindow extends JFrame implements WindowListener, ActionListener, MouseListener {

	
	private Object[] zones = {};
	private JButton loadZones;
	private JButton saveZone;
	private JButton addNewZone;
	private JButton removeZones;
	public JTable zoneTable;
	public JTabbedPane tabbedPane;

	private Object[] columnNames = {"id", "title", "regionsX", "regionsY", "regionWidth", "regionHeight"};

	
	/**
	 * Create the frame.
	 */
	public MapEditorWindow() {
		setResizable(false);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		TilePanel tp = new TilePanel();
		getContentPane().setPreferredSize(tp.getPreferredSize());
		getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane);
		
		JPanel zonesPanel = new JPanel();
		tabbedPane.addTab("Zones", null, zonesPanel, null);
		zonesPanel.setLayout(new BoxLayout(zonesPanel, BoxLayout.Y_AXIS));
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		zonesPanel.add(toolBar_1);
		toolBar_1.setFloatable(false);
		
		loadZones = new JButton(new ImageIcon("textures/gui/MapEditor/load.png"));
		loadZones.setToolTipText("Load zones");
		toolBar_1.add(loadZones);
		
		saveZone = new JButton(new ImageIcon("textures/gui/MapEditor/save.png"));
		saveZone.setToolTipText("Save selected zone");
		toolBar_1.add(saveZone);
		toolBar_1.addSeparator();
		
		saveZone.addActionListener(this);
		
		addNewZone = new JButton(new ImageIcon("textures/gui/MapEditor/add.png"));
		addNewZone.setToolTipText("Add a new zone");
		toolBar_1.add(addNewZone);
		
		removeZones = new JButton(new ImageIcon("textures/gui/MapEditor/remove.png"));
		removeZones.setToolTipText("Remove selected zone(s)");
		toolBar_1.add(removeZones);
		removeZones.addActionListener(this);
		loadZones.addActionListener(this);
		addNewZone.addActionListener(this);
		zoneTable = new JTable();
		zoneTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		zoneTable.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(zoneTable);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		zonesPanel.add(scrollPane);
		
		tabbedPane.addTab("Tiles", null, tp, null);
		
		ObjectPanel op = new ObjectPanel(this);
		tabbedPane.addTab("Objects", null, op, null);
		
		Object[][] data = {};
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(data, columnNames);
		
		this.addWindowListener(this);
		
		pack();
		setVisible(true);

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		MapEditor.toggle();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Button stuff
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == loadZones) {
			// read from "zones" folder
		    File folder = new File("./data/zones");
		    File[] zoneList = folder.listFiles();
		    
		    Object[][] data = new Object[zoneList.length][6];
		    for (int i = 0; i < zoneList.length; i++) {
		    	
			      XmlReader xmlReader = new XmlReader();
			      FileHandle file = Gdx.files.absolute(zoneList[i].getAbsolutePath());
			      XmlReader.Element root = null;
			      
					try {
						root = xmlReader.parse(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				  XmlReader.Element zone = root.getChildByName("zone");
				  int id = Integer.parseInt(zone.getAttribute("id"));
				  int regionsX = Integer.parseInt(zone.getAttribute("regionsX"));
				  int regionsY = Integer.parseInt(zone.getAttribute("regionsY"));
				  int regionWidth = Integer.parseInt(zone.getAttribute("regionWidth"));
				  int regionHeight = Integer.parseInt(zone.getAttribute("regionHeight"));
		    	
				data[i] = new Object[] { id, zoneList[i].getName().replace(".xml", ""), regionsX, regionsY, regionWidth, regionHeight };
			}
		    zoneTable.setModel(new DefaultTableModel(data, columnNames));
		} else if (source == saveZone) {
			if (zoneTable.getSelectedRow() == -1) {
				return;
			}
			
			int[][] tempMap = null;
			tempMap = Map.getTiles();
			
			String id = zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 0).toString();
			String title = zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 1).toString();
			String regionsX = zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 2).toString();
			String regionsY = zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 3).toString();
			String regionWidth = zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 4).toString();
			String regionHeight = zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 5).toString();
			
			StringWriter sw = new StringWriter();
			 XmlWriter xml = new XmlWriter(sw);
			 try {
				xml.element("xml")
				        .attribute("version", "1.0")
				        .attribute("encoding", "UTF-8")
				        .element("zone")
				                .attribute("id", id)
				                .attribute("regionWidth", regionWidth)
				                .attribute("regionHeight", regionHeight)
				                .attribute("regionsX", regionsX)
				                .attribute("regionsY", regionsY)
		                .pop()
		                .element("tileset")
		                		.element("image")
		                			.attribute("source", "textures/normal.png")
		                			.attribute("width", "512")
		                			.attribute("height", "512")
	                			.pop()
            			.pop()
    					.element("data");
				
				for (int i = 0; i < tempMap.length; i++) {
					for (int j = 0; j < tempMap[i].length; j++) {
						xml.element("tile")
							.attribute("gid", tempMap[i][j])
							.pop();
					}
				}
				
				xml.pop().pop();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			 FileOutputStream out = null;
			try {
				out = new FileOutputStream("./data/zones/" + title + ".xml");
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 try {
				out.write(sw.toString().getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
		} else if (source == addNewZone) {
			// --------
			JPanel addNewZonePanel = new JPanel();
			addNewZonePanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			
			JLabel lblNewLabel = new JLabel("ID");
			addNewZonePanel.add(lblNewLabel, "2, 2, right, default");
			
			JTextField id = new JTextField();
			addNewZonePanel.add(id, "4, 2, fill, default");
			id.setColumns(10);
			
			JLabel lblNewLabel_1 = new JLabel("Title");
			addNewZonePanel.add(lblNewLabel_1, "2, 4, right, default");
			
			JTextField title = new JTextField();
			addNewZonePanel.add(title, "4, 4, fill, default");
			title.setColumns(10);

			JLabel lblNewLabel_4 = new JLabel("RegionsX");
			addNewZonePanel.add(lblNewLabel_4, "2, 6, right, default");
			
			JTextField regionsX = new JTextField();
			addNewZonePanel.add(regionsX, "4, 6, fill, default");
			regionsX.setColumns(10);
			
			JLabel lblNewLabel_5 = new JLabel("RegionsY");
			addNewZonePanel.add(lblNewLabel_5, "2, 8, right, default");
			
			JTextField regionsY = new JTextField();
			addNewZonePanel.add(regionsY, "4, 8, fill, default");
			regionsY.setColumns(10);
			
			JLabel lblNewLabel_2 = new JLabel("Region width");
			addNewZonePanel.add(lblNewLabel_2, "2, 10, right, default");
			
			JTextField regionWidth = new JTextField();
			addNewZonePanel.add(regionWidth, "4, 10, fill, default");
			regionWidth.setColumns(10);
			
			JLabel lblNewLabel_3 = new JLabel("Region height");
			addNewZonePanel.add(lblNewLabel_3, "2, 12, right, default");
			
			JTextField regionHeight = new JTextField();
			addNewZonePanel.add(regionHeight, "4, 12, fill, default");
			regionHeight.setColumns(10);
			

			// ----------
	        int result = JOptionPane.showConfirmDialog(this, addNewZonePanel, "Add a new zone",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
				((DefaultTableModel) zoneTable.getModel()).addRow(new Object[] { id.getText(), title.getText(), regionsX.getText(), regionsY.getText(), regionWidth.getText(), regionHeight.getText()});
	        }
		} else if (source == removeZones) {
	        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete these zones?", null,
		            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
				int[] selectedRows = zoneTable.getSelectedRows();
				Arrays.sort(selectedRows);
				if (selectedRows.length > 0) {
					for (int i = selectedRows.length - 1; i >= 0 ; i--) {
						File f = new File("./data/zones/" + zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 1).toString() + ".xml");
						
						f.setWritable(true);
						if (f.delete()) {
							((DefaultTableModel) zoneTable.getModel()).removeRow(selectedRows[i]);	
						}
					}
				}
	        }
		} 
		
		System.out.println("click");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1) {
			int[][] tempMap = null;
		      XmlReader xmlReader = new XmlReader();
		      
		      String title = zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 1).toString();
		      
		      FileHandle file = Gdx.files.internal("./data/zones/" + title + ".xml");
		      
		      if (file.exists()) {
			      XmlReader.Element root = null;
			      
					try {
						root = xmlReader.parse(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				  XmlReader.Element zone = root.getChildByName("zone");
				  int regionWidth = Integer.parseInt(zone.getAttribute("regionWidth"));
				  int regionHeight = Integer.parseInt(zone.getAttribute("regionHeight"));

				  tempMap = new int[regionWidth][regionHeight];
			      XmlReader.Element data = root.getChildByName("data");
			      
			      for (int i = 0; i < regionWidth; i++) {
					for (int j = 0; j < regionHeight; j++) {
						tempMap[i][j] = Integer.parseInt(data.getChild(i*regionHeight+j).getAttribute("gid"));
					}
				  }

		      } else {
				  int regionWidth = Integer.parseInt(zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 4).toString());
				  int regionHeight = Integer.parseInt(zoneTable.getModel().getValueAt(zoneTable.getSelectedRow(), 5).toString());

		    	  tempMap = new int[regionWidth][regionHeight];
		      }
		      
		      Map.load(tempMap); 
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
