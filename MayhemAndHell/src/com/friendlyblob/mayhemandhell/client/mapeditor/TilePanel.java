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

	// TODO tileSize should be set in one place only
	private static final int tileSize = 16;
	
	private final int tilesInX;
	private final int tilesInY;
	
	public TilePanel() {
		addMouseListener(this);
		this.setBackground(Color.DARK_GRAY);
		try {
			tileSheet = ImageIO.read(new File("./textures/tiles/tiles.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tilesInX = tileSheet.getWidth()/tileSize;
		tilesInY = tileSheet.getHeight()/tileSize;
		
		setPreferredSize(new Dimension(tilesInX*tileSize, tilesInY*tileSize));
	}
	
	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(tileSheet, 0, 0, null);
		
		g2d.setColor(new Color(1, 1, 1, .5f));
		
		g2d.fillRect(
				(MapEditor.selectedTileTexture % tilesInX) * tileSize, 
				MapEditor.selectedTileTexture/tilesInX * tileSize, 
				tileSize, 
				tileSize);
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		int tileSelected = me.getX() / tileSize + (me.getY() / tileSize)*tilesInX ;
		MapEditor.selectedTileTexture = tileSelected;
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
