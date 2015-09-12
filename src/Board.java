public class Board {
	private Tile[][] tiles;
	private int mines;
	private int coveredTiles;
	private int correctFlagTiles;
	
	private GameState currentState;
	
	public Board() {
		this(10,10);
	}
	
	public Board(int dimensions, int mines) {
		this(dimensions, dimensions, mines);
	}
	
	public Board(int row, int col, int mines) {
		tiles = new Tile[row][col];
		this.mines = mines;
		coveredTiles = row * col;
		correctFlagTiles = 0;
		
		currentState = GameState.ONGOING;
		
		for(int r=0; r<row; r++) {
			for(int c=0; c<col; c++) {
				tiles[r][c] = new Tile();
			}
		}
		
		for(int m=0; m<mines; m++) {
			int r = (int)(Math.random() * row);
			int c = (int)(Math.random() * col);
			
			if(tiles[r][c].getValue() != Tile.MINE) {
				tiles[r][c].setValue(Tile.MINE);
				
				for(int rOffset = -1; rOffset <= 1; rOffset++) {
					for(int cOffset = -1; cOffset <= 1; cOffset++) {
						if(r+rOffset >= 0 &&
							r+rOffset < row &&
							c+cOffset >= 0 &&
							c+cOffset < col &&
							tiles[r+rOffset][c+cOffset].getValue() != Tile.MINE)
								tiles[r+rOffset][c+cOffset].setValue(tiles[r+rOffset][c+cOffset].getValue() + 1);
					}
				}	
			} else {
				m--;
			}
		}
	}
	
	public void setFlag(int row, int col) {
		if(tiles[row][col].isFlagged())
			return;
		
		tiles[row][col].flag();
		
		if(tiles[row][col].getValue() == Tile.MINE)
			correctFlagTiles++;
		
		if(correctFlagTiles == mines) {
			gameWon();
		}
	}
	
	public void unsetFlag(int row, int col) {
		if(!tiles[row][col].isFlagged())
			return;
		
		tiles[row][col].unflag();
		
		if(tiles[row][col].getValue() == Tile.MINE)
			correctFlagTiles--;
	}
	
	public void probe(int row, int col) {
		Tile t = tiles[row][col];
		if(!t.isCovered())
			return;
		
		if(!t.isFlagged()) {
			t.uncover();
			coveredTiles--;
			
			if(t.getValue() == Tile.MINE) {
				gameLost();
			} else if(coveredTiles == mines) {
				gameWon();
			} else if(t.getValue() == Tile.EMPTY) {
				for(int rOffset = -1; rOffset <= 1; rOffset++) {
					for(int cOffset = -1; cOffset <= 1; cOffset++) {
						if(row+rOffset >= 0 &&
							row+rOffset < tiles.length &&
							col+cOffset >= 0 &&
							col+cOffset < tiles[row+rOffset].length &&
							tiles[row+rOffset][col+cOffset].isCovered() &&
							tiles[row+rOffset][col+cOffset].getValue() != Tile.MINE) {
								probe(row+rOffset, col+cOffset);
						}
					}
				}
			}
		}
	}
	
	private void gameWon() {currentState = GameState.WON;}
	
	private void gameLost() {currentState = GameState.LOST;}
	
	public GameState getGameState() {return currentState;}
	
	public int getNumRows() {return tiles.length;}
	
	public int getNumCols() {return tiles[0].length;}
	
	public Tile getTileAt(int r, int c) {return tiles[r][c];}
}