package labyrinth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Labyrinth {
	Map<Cell, List<Cell>> adjacentCells;
	private int rows;
	private int columns;

	void addCell(Cell cell) {
		adjacentCells.putIfAbsent(cell, new ArrayList<>());
	}

	void removeCell(Cell cell) {
		adjacentCells.values().stream().forEach(e -> e.remove(cell));
		adjacentCells.remove(cell);
	}

	void addEdge(Cell cell1, Cell cell2) {
		adjacentCells.get(cell1).add(cell2);
		adjacentCells.get(cell2).add(cell1);
	}

	public Labyrinth(char[][] labyrinthAsCharArray, int rows, int columns) {
		this.adjacentCells = new HashMap<>();
		this.rows = rows;
		this.columns = columns;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (labyrinthAsCharArray[i][j] == '#') {
					continue;
				}
				Cell currentCell = new Cell(j, i, labyrinthAsCharArray[i][j]);
				addCell(currentCell);

				if (j < columns - 1 && labyrinthAsCharArray[i][j + 1] != '#') {
					Cell rightCell = new Cell(j + 1, i, labyrinthAsCharArray[i][j + 1]);
					addCell(rightCell);
					addEdge(currentCell, rightCell);
				}
				if (i < rows - 1 && labyrinthAsCharArray[i + 1][j] != '#') {
					Cell bottomCell = new Cell(j, i + 1, labyrinthAsCharArray[i + 1][j]);
					addCell(bottomCell);
					addEdge(currentCell, bottomCell);
				}
			}
		}
	}

	public void discoverNewCells(char[][] labyrinthAsCharArray, Direction direction, Position position) {
		List<Position> positionsToUpdate = new ArrayList<>();

		switch (direction) {
			case DOWN:
				if (position.y + 2 < rows) {
					positionsToUpdate.add(new Position(position.x, position.y + 2));
					if (position.x - 1 >= 0) {
						positionsToUpdate.add(new Position(position.x - 1, position.y + 2));
					}
					if (position.x - 2 >= 0) {
						positionsToUpdate.add(new Position(position.x - 2, position.y + 2));
					}
					if (position.x + 1 < columns) {
						positionsToUpdate.add(new Position(position.x + 1, position.y + 2));
					}
					if (position.x + 2 < columns) {
						positionsToUpdate.add(new Position(position.x + 2, position.y + 2));
					}
				}
				break;
			case LEFT:
				if (position.x - 2 >= 0) {
					positionsToUpdate.add(new Position(position.x - 2, position.y));
					// if possible
					if (position.y - 1 >= 0) {
						positionsToUpdate.add(new Position(position.x - 2, position.y - 1));
					}
					if (position.y - 2 >= 0) {
						positionsToUpdate.add(new Position(position.x - 2, position.y - 2));
					}
					if (position.y + 1 < rows) {
						positionsToUpdate.add(new Position(position.x - 2, position.y + 1));
					}
					if (position.y + 2 < rows) {
						positionsToUpdate.add(new Position(position.x - 2, position.y + 2));
					}
				}
				break;
			case RIGHT:
				if (position.x + 2 < columns) {
					positionsToUpdate.add(new Position(position.x + 2, position.y));
					if (position.y - 1 >= 0) {
						positionsToUpdate.add(new Position(position.x + 2, position.y - 1));
					}
					if (position.y - 2 >= 0) {
						positionsToUpdate.add(new Position(position.x + 2, position.y - 2));
					}
					if (position.y + 1 < rows) {
						positionsToUpdate.add(new Position(position.x + 2, position.y + 1));
					}
					if (position.y + 2 < rows) {
						positionsToUpdate.add(new Position(position.x + 2, position.y + 2));
					}
				}
				break;
			case UP:
				if (position.y - 2 >= 0) {
					positionsToUpdate.add(new Position(position.x, position.y - 2));
					if (position.x - 1 >= 0) {
						positionsToUpdate.add(new Position(position.x - 1, position.y - 2));
					}
					if (position.x - 2 >= 0) {
						positionsToUpdate.add(new Position(position.x - 2, position.y - 2));
					}
					if (position.x + 1 < columns) {
						positionsToUpdate.add(new Position(position.x + 1, position.y - 2));
					}
					if (position.x + 2 < columns) {
						positionsToUpdate.add(new Position(position.x + 2, position.y - 2));
					}
				}
				break;
			default:
				break;
		}

		for (Position positionToUpdate : positionsToUpdate) {
			Cell cellFromMap = getCellAt(positionToUpdate);
			CellType newCellType = CellType.valueOfChar(labyrinthAsCharArray[positionToUpdate.y][positionToUpdate.x]);

			if (newCellType.equals(CellType.WALL)) {
				removeCell(cellFromMap);
			} else {
				cellFromMap.type = newCellType;
				adjacentCells.values().stream().forEach(e -> {
					int index = e.indexOf(cellFromMap);
					if (index != -1) {
						Cell cell = e.get(index);
						cell.type = newCellType;
					}
				});
			}
		}
	}

	Cell getCellAt(Position position) {
		Set<Cell> cells = adjacentCells.keySet();

		for (Cell cell : cells) {
			if (cell.position.equals(position)) {
				return cell;
			}
		}

		return null;
	}

	List<Cell> getAdjacentCells(Cell cell) {
		return adjacentCells.get(cell);
	}

	Cell findNextCellLeadingTo(Position rootPosition, CellType type, boolean ignoreUnknownCases) {
		Cell root = new Cell(rootPosition.x, rootPosition.y, '.');
		Map<Cell, Cell> visited = new LinkedHashMap<>(); // visited cells mapped to their parent
		Queue<Cell> queue = new LinkedList<>();
		queue.add(root);
		visited.put(root, root);
		boolean cellFound = false;
		Cell result = null;

		while (!queue.isEmpty() && !cellFound) {
			Cell cell = queue.poll();
			for (Cell c : getAdjacentCells(cell)) {
				if (ignoreUnknownCases && c.type.equals(CellType.UNKNOWN)) {
					continue;
				}
				if (!visited.containsKey(c)) {
					visited.put(c, cell);
					queue.add(c);

					if (c.type.equals(type)) {
						cellFound = true;
						result = c;
						break;
					}
				}
			}
		}

		Cell parent = visited.get(result);
		while (!parent.equals(root)) {
			result = parent;
			parent = visited.get(result);
		}

		return result;
	}

	public int computeFastestWayTo(Position rootPosition, CellType type) {
		Cell root = new Cell(rootPosition.x, rootPosition.y, '?');
		Map<Cell, Cell> visited = new LinkedHashMap<>(); // visited cells mapped to their parent
		Queue<Cell> queue = new LinkedList<>();
		queue.add(root);
		visited.put(root, root);
		boolean cellFound = false;
		Cell result = null;

		while (!queue.isEmpty() && !cellFound) {
			Cell cell = queue.poll();
			for (Cell c : getAdjacentCells(cell)) {
				if (c.type.equals(CellType.UNKNOWN)) {
					continue;
				}
				if (!visited.containsKey(c)) {
					visited.put(c, cell);
					queue.add(c);

					if (c.type.equals(type)) {
						cellFound = true;
						result = c;
						break;
					}
				}
			}
		}

		if (cellFound) {
			int turnCount = 1;
			Cell parent = visited.get(result);
			while (!parent.equals(root)) {
				result = parent;
				parent = visited.get(result);
				turnCount++;
			}

			return turnCount;
		}

		return -1;
	}
}
