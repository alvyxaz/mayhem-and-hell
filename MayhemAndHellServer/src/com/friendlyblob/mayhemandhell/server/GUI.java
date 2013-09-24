package com.friendlyblob.mayhemandhell.server;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class GUI extends SwingWorker<String, Object> {

	public static class GuiFrame extends JFrame{
		private StatisticsPanel stats;
		
		public GuiFrame() {
			setTitle("Server");
			setSize(700, 500);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE); 
			this.setVisible(true);

			// Adding java menu
			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
			
			JMenu serverMenu = new JMenu("Server");
			JMenuItem shutdownItem = new JMenuItem("Shutdown");
			shutdownItem.addActionListener(new Shutdown.ShutDownListener());
			serverMenu.add(shutdownItem);
			
			menuBar.add(fileMenu);
			menuBar.add(serverMenu);
			
			
			setJMenuBar(menuBar);
			
			stats = new StatisticsPanel();
			this.add(stats);

			Timer t = new Timer(1000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					repaint();
				}
				
			});
			t.start();
		}
		
//		public static void main(String[] args) {
//			new GuiFrame();
//		}
	}

	public static class StatisticsPanel extends JPanel  {
		
		private final int startAtX = 10;
		private final int startAtY = 20;
		private final int lineHeight = 20;
		private final int labelWidth = 140;
		
		public StatisticsPanel() {
			
		}
		
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			int line = 0;
			
			drawStat(g2d, "Memory usage:", ServerStatistics.getMemoryUsage(),startAtX, startAtY + line*lineHeight);
			line++;
			line++;
			
			drawStat(g2d, "Packets sent:", ServerStatistics.getPacketsSent(),startAtX, startAtY + line*lineHeight);
			line++;
			drawStat(g2d, "Bytes sent:", ServerStatistics.getBytesSent(),startAtX, startAtY + line*lineHeight);
			line++;
			line++;
			
			drawStat(g2d, "Packets received:", ServerStatistics.getPacketsReceived(),startAtX, startAtY + line*lineHeight);
			line++;
			drawStat(g2d, "Bytes received:", ServerStatistics.getBytesReceived(),startAtX, startAtY + line*lineHeight);
			line++;
			line++;
		}
		
		public void drawStat(Graphics2D g2d, String name, String value, int x, int y) {
			g2d.drawString(name, x, y);
			g2d.drawString(value, x+ labelWidth, y);
		}
		
	}
	
	@Override
	protected String doInBackground() throws Exception {
		new GuiFrame();
		return "yup";
	}
	
}
