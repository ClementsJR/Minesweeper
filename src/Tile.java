public class Tile {
	public final static int MINE = -1;
	public final static int EMPTY = 0;

	private int value;
	private boolean flagged;
	private boolean covered;
	
	public Tile() {
		value = Tile.EMPTY;
		flagged = false;
		covered = true;
	}
	
	public void flag() {
		flagged = true;
	}
	
	public void unflag() {
		flagged = false;
	}
	
	public void uncover() {
		covered = false;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isFlagged() {
		return flagged;
	}
	
	public boolean isCovered() {
		return covered;
	}
}