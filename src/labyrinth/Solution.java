package labyrinth;

import java.util.Scanner;

public class Solution {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int rows = in.nextInt(); // number of rows.
		int columns = in.nextInt(); // number of columns.
		int A = in.nextInt(); // number of rounds between the time the alarm countdown is activated and the
								// time the alarm goes off.

		Labyrinth labyrinth = null;
		boolean controlRoomFound = false;
		boolean wayBackFound = false;
		boolean alarmTriggered = false;
		Direction direction = null;

		// game loop
		while (true) {
			int kirkY = in.nextInt(); // row where Kirk is located.
			int kirkX = in.nextInt(); // column where Kirk is located.
			Position kirkPosition = new Position(kirkX, kirkY);
			Position controlRoomPosition = null;

			char[][] labyrinthAsCharArray = new char[rows][columns];
			for (int i = 0; i < rows; i++) {
				labyrinthAsCharArray[i] = in.next().toCharArray();

				for (int j = 0; j < columns; j++) {
					if (labyrinthAsCharArray[i][j] == 'C') {
						controlRoomFound = true;
						controlRoomPosition = new Position(j, i);
					}
				}
			}

			if (labyrinthAsCharArray[kirkPosition.y][kirkPosition.x] == 'C') {
				alarmTriggered = true;
			}

			if (labyrinth == null) {
				labyrinth = new Labyrinth(labyrinthAsCharArray, rows, columns);
			} else {
				labyrinth.discoverNewCells(labyrinthAsCharArray, direction, kirkPosition);
			}

			if (controlRoomFound && !wayBackFound) {
				int turnCount = labyrinth.computeFastestWayTo(controlRoomPosition, CellType.START);
				if (turnCount != -1 && turnCount <= A) {
					wayBackFound = true;
				}
			}

			CellType targetType;
			if (wayBackFound && alarmTriggered) {
				targetType = CellType.START;
			} else if (controlRoomFound && wayBackFound) {
				targetType = CellType.CONTROL_ROOM;
			} else {
				targetType = CellType.UNKNOWN;
			}
			boolean ignoreUnknownCases = (targetType != CellType.UNKNOWN);
			Cell nextCell = labyrinth.findNextCellLeadingTo(kirkPosition, targetType, ignoreUnknownCases);
			direction = kirkPosition.computeDirectionTo(nextCell.position);

			System.out.println(direction);
		}
	}
}