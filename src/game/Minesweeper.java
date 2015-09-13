package game;

// Minesweeper
// Minesweeper.java
//
// A Java implementation of the classic Windows bored-at-the-office game.
//
// John Clements
// jrclements@ualr.edu
// https://github.com/ClementsJR
//
// 2015-09-12

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

public class Minesweeper{
	protected static JFrame window;
	
	public static void main(String[] args) {
		window = new JFrame("");
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try{
			window.setIconImage(ImageIO.read(new File("img/mine.png")));
			window.setTitle("Minesweeper");
		} catch (IOException x) {
			window.setTitle("Minesweeper");
		}
		
		makeMenuBar();
		makeGameSettings();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	//
	protected static void makeGameSettings() {
		JPanel gameSettings = new JPanel();
		gameSettings.setLayout(new BoxLayout(gameSettings, BoxLayout.PAGE_AXIS));
		gameSettings.setSize(new Dimension(150,160));
		
		JPanel rowPane = makePanel("Rows:");
		JPanel colPane = makePanel("Columns:");
		JPanel minePane = makePanel("Mines:");
		JButton playButton = new JButton("Play");
		playButton.addMouseListener(new PlayGameListener());
		playButton.setMaximumSize(new Dimension(64, 20));
		JLabel errorLabel = makeLabel("");
		errorLabel.setHorizontalAlignment(JLabel.LEFT);
		errorLabel.setMaximumSize(new Dimension(128, 16));
		
		gameSettings.add(rowPane);
		gameSettings.add(colPane);
		gameSettings.add(minePane);
		gameSettings.add(Box.createRigidArea(new Dimension(0,5)));
		gameSettings.add(playButton);
		gameSettings.add(errorLabel);
		
		window.setContentPane(gameSettings);
		int width = (int)(window.getContentPane().getSize().getWidth());
		int height = (int)(window.getContentPane().getSize().getHeight() +
			window.getJMenuBar().getSize().getHeight());
		window.setPreferredSize(new Dimension(width, height));
		window.pack();
	}
	
	private static JPanel makePanel(String labelText) {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
		pane.add(makeLabel(labelText));
		pane.add(Box.createRigidArea(new Dimension(5,0)));
		pane.add(makeTextField());
		return pane;
	}
	
	private static JLabel makeLabel(String text) {
		JLabel label = new JLabel(text, (int)JLabel.RIGHT);
		label.setMaximumSize(new Dimension(64,16));
		return label;
	}
	
	private static JTextField makeTextField() {
		JTextField textField = new JTextField("10", 3);
		textField.setMaximumSize(new Dimension(30,16));
		return textField;
	}
	
	protected static void makeMinePanel(int rows, int cols, int mines) {
		window.setContentPane(new MinePanel(rows, cols, mines));
		
		int width = (int)(window.getContentPane().getSize().getWidth()) + 6;
		int height = (int)(window.getContentPane().getSize().getHeight() +
			window.getJMenuBar().getSize().getHeight()) +
			(1 * MinePanel.TILE_SIZE + 12);
		
		window.setPreferredSize(new Dimension(width, height));
		window.pack();
	}
	
	protected static void makeMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem newGameItem = new JMenuItem("New Game");
		newGameItem.addActionListener(new NewGameListener());
		JMenuItem quitGameItem = new JMenuItem("Quit Game");
		quitGameItem.addActionListener(new QuitListener());
		fileMenu.add(newGameItem);
		fileMenu.add(quitGameItem);
		bar.add(fileMenu);
		
		JMenu helpMenu = new JMenu("Help");
		JMenuItem controlsItem = new JMenuItem("Controls");
		controlsItem.addActionListener(new ControlsListener());
		JMenuItem creditsItem = new JMenuItem("Credits");
		creditsItem.addActionListener(new CreditsListener());
		helpMenu.add(controlsItem);
		helpMenu.add(creditsItem);
		bar.add(helpMenu);
		
		window.setJMenuBar(bar);
	}
	
	protected static abstract class GenericListener implements MouseListener, Action {
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		
		public void actionPerformed(ActionEvent e) {}
		public Object getValue(String key) {return null;}
		public void putValue(String key, Object value) {}
		public void setEnabled(boolean b) {}
		public boolean isEnabled() {return true;}
		public void addPropertyChangeListener(PropertyChangeListener listener) {}
		public void removePropertyChangeListener(PropertyChangeListener listener) {}
	}
	
	protected static class PlayGameListener extends GenericListener {
		public void mouseClicked(MouseEvent e) {
			Component[] panes = window.getContentPane().getComponents();
			Container pane = (Container)panes[0];
			JTextField txt = (JTextField)pane.getComponent(2);
			int rows = Integer.parseInt(txt.getText());
			
			pane = (Container)panes[1];
			txt = (JTextField)pane.getComponent(2);
			int cols = Integer.parseInt(txt.getText());
			
			pane = (Container)panes[2];
			txt = (JTextField)pane.getComponent(2);
			int mines = Integer.parseInt(txt.getText());
			
			JLabel errorLabel = (JLabel)panes[5];
			
			if(rows < 10 || rows > 99 || cols < 10 || cols > 99 || mines < 1 || mines >= rows * cols) {
				errorLabel.setText("  Bad Input");
				return;
			}
			
			makeMinePanel(rows,cols,mines);
		}
	}
	
	protected static class NewGameListener extends GenericListener {
		public void mouseClicked(MouseEvent e) {
			makeGameSettings();
		}
		
		public void actionPerformed(ActionEvent e) {
			mouseClicked(null);
		}
	}
	
	protected static class QuitListener extends GenericListener {
		public void mouseClicked(MouseEvent e) {
			window.dispose();
			System.exit(0);
		}
		
		public void actionPerformed(ActionEvent e) {
			mouseClicked(null);
		}
	}
	
	protected static class ControlsListener extends GenericListener {
		public void mouseClicked(MouseEvent e) {
			JOptionPane.showMessageDialog(window, "Left click to probe a tile for a mine.\nRight click to place or remove a flag.\nUse flags to mark mine locations.");
		}
		
		public void actionPerformed(ActionEvent e) {
			mouseClicked(null);
		}
	}
	
	protected static class CreditsListener extends GenericListener {
		public void mouseClicked(MouseEvent e) {
			JOptionPane.showMessageDialog(window, "Programmed and Designed by\nJohn Clements\n\nMarch 2015");
		}
		
		public void actionPerformed(ActionEvent e) {
			mouseClicked(null);
		}
	}
}