package com.friendlyblob.mayhemandhell.client.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TilePanel extends JPanel implements MouseListener {

	private BufferedImage tileSheet;

	public TilePanel() {
		setPreferredSize(new Dimension(96*5, 48*8));
		addMouseListener(this);
		
		try {
			tileSheet = ImageIO.read(new File("./textures/tiles/normal.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(tileSheet, 0, 0, null);
		
		g2d.setColor(new Color(1, 1, 1, .5f));
		g2d.fillRect(MapEditor.selectedTileTexture % 5 * 96, MapEditor.selectedTileTexture/5 * 48, 96, 48);
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		MapEditor.selectedTileTexture = me.getX() / 96 + me.getY() / 48 * 5;
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
