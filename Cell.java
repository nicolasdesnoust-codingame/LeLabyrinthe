package labyrinth;

public class Cell {
	Position position;
	CellType type;
	
	public Cell(int x, int y, char type) {
		this.position = new Position(x, y);
		this.type = CellType.valueOfChar(type);
	}
	
	public Cell(Position position, char type) {
		this.position = new Position(position.x, position.y);
		this.type = CellType.valueOfChar(type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
}
