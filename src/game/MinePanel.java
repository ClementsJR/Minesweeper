package game;

// Minesweeper
// MinePanel.java
//
// John Clements
// jrclements@ualr.edu
// https://github.com/ClementsJR
//
// 2015-09-12

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

public class MinePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public final static int TILE_SIZE = 16;
	protected Board board;
	
	private BufferedImage coveredTile,
		emptyTile, flaggedTile,
		wrongFlaggedTile, mineTile,
		detonatedMineTile, oneTile,
		twoTile, threeTile, fourTile,
		fiveTile, sixTile, sevenTile,
		eightTile;
	
	public MinePanel(int rows, int cols, int mines) {
		super(new BorderLayout());
		addMouseListener(new MinesweeperMouseListener(this));
		
		board = new Board(rows,cols,mines);
		loadImages();
		
		setSize(cols*TILE_SIZE, rows*TILE_SIZE);
		setBackground(Color.PINK);
	}
	
	public void paint(Graphics pen) {
		for(int r=0; r<board.getNumRows(); r++) {
			for(int c=0; c<board.getNumCols(); c++) {
				Tile t = board.getTileAt(r,c);
				Image picToDraw = null;
				
				if(t.isFlagged())
					picToDraw = flaggedTile;
				else if(t.isCovered())
					picToDraw = coveredTile;
				else if(t.getValue() == Tile.EMPTY)
					picToDraw = emptyTile;
				else if(t.getValue() == 1)
					picToDraw = oneTile;
				else if(t.getValue() == 2)
					picToDraw = twoTile;
				else if(t.getValue() == 3)
					picToDraw = threeTile;
				else if(t.getValue() == 4)
					picToDraw = fourTile;
				else if(t.getValue() == 5)
					picToDraw = fiveTile;
				else if(t.getValue() == 6)
					picToDraw = sixTile;
				else if(t.getValue() == 7)
					picToDraw = sevenTile;
				else if(t.getValue() == 8)
					picToDraw = eightTile;
				
				pen.drawImage(picToDraw,TILE_SIZE*c,TILE_SIZE*r,null);
			}
		}
			
		if(board.getGameState() == GameState.LOST) {
			for(int r=0; r<board.getNumRows(); r++) {
				for(int c=0; c<board.getNumCols(); c++) {
					Tile t = board.getTileAt(r,c);
					
					if(t.isFlagged() && t.getValue() != Tile.MINE)
						pen.drawImage(wrongFlaggedTile,TILE_SIZE*c,TILE_SIZE*r,null);
					else if(!t.isCovered() && t.getValue() == Tile.MINE)
						pen.drawImage(detonatedMineTile,TILE_SIZE*c,TILE_SIZE*r,null);
					else if(t.getValue() == Tile.MINE && !t.isFlagged())
						pen.drawImage(mineTile,TILE_SIZE*c,TILE_SIZE*r,null);
				}
			}
		}
		
		if(board.getGameState() == GameState.WON) {
			JLabel winLabel = new JLabel("WINNER", (int)JLabel.CENTER_ALIGNMENT);
			winLabel.setOpaque(true);
			winLabel.setBackground(Color.PINK);
			winLabel.setSize(64, 16);
			
			add(winLabel, BorderLayout.CENTER);
			paintComponents(pen);
		}
	}
	
	private void loadImages() {
		try {
			coveredTile = ImageIO.read(new File("img/covered.png"));
			emptyTile = ImageIO.read(new File("img/empty.png"));
			flaggedTile = ImageIO.read(new File("img/flagged.png"));
			wrongFlaggedTile = ImageIO.read(new File("img/wrong_flagged.png"));
			mineTile = ImageIO.read(new File("img/mine.png"));
			detonatedMineTile = ImageIO.read(new File("img/detonated_mine.png"));
			oneTile = ImageIO.read(new File("img/1.png"));
			twoTile = ImageIO.read(new File("img/2.png"));
			threeTile = ImageIO.read(new File("img/3.png"));
			fourTile = ImageIO.read(new File("img/4.png"));
			fiveTile = ImageIO.read(new File("img/5.png"));
			sixTile = ImageIO.read(new File("img/6.png"));
			sevenTile = ImageIO.read(new File("img/7.png"));
			eightTile = ImageIO.read(new File("img/8.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected class MinesweeperMouseListener implements MouseListener {
		private MinePanel p;
		public MinesweeperMouseListener(MinePanel p) {
			this.p = p;
		}
		
		public void mouseClicked(MouseEvent e) {
			int r = (int) e.getY()/TILE_SIZE;
			int c = (int) e.getX()/TILE_SIZE;
			
			if(p.board.getGameState() != GameState.ONGOING || r < 0 || r >= p.board.getNumRows() || c < 0 || c >= p.board.getNumCols())
				return;
			
			if(e.getButton() == MouseEvent.BUTTON1) {
				p.board.probe(r,c);
			} else if(e.getButton() == MouseEvent.BUTTON3){
				if(p.board.getTileAt(r,c).isFlagged())
					p.board.unsetFlag(r,c);
				else
					p.board.setFlag(r,c);
			}
			
			p.repaint();
		}
		
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}