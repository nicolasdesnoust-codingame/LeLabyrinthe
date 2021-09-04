package labyrinth;

import java.util.Scanner;

public class Solution {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int rows = in.nextInt();
		int columns = in.nextInt();
		int alarmCountdown = in.nextInt();

		boolean wayBackFound = false;
		boolean alarmTriggered = false;
		boolean controlRoomFound = false;
		Position controlRoomPosition = null;
		Labyrinth labyrinth = null;
		Direction direction = null;
		
		// game loop
		while (true) {
			int kirkY = in.nextInt(); // row where Kirk is located.
			int kirkX = in.nextInt(); // column where Kirk is located.
			Position kirkPosition = new Position(kirkX, kirkY);

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

			if (labyrinth == null) {
				labyrinth = new Labyrinth(labyrinthAsCharArray, rows, columns);
			} else {
				labyrinth.discoverNewCells(labyrinthAsCharArray, direction, kirkPosition);
			}

			if (labyrinth.getCellAt(kirkPosition).type == CellType.CONTROL_ROOM) {
				alarmTriggered = true;
			}
			if (controlRoomFound && !wayBackFound) {
				int turnCount = labyrinth.computeFastestWayTo(controlRoomPosition, CellType.START);
				if(turnCount != -1 && turnCount <= alarmCountdown) {
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

			Cell nextCell = labyrinth.findNextCellLeadingTo(kirkPosition, targetType);
			direction = kirkPosition.computeDirectionTo(nextCell.position);

			System.out.println(direction);
			break;
		}
	}
}