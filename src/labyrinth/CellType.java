package labyrinth;

import java.util.HashMap;
import java.util.Map;

public enum CellType {
	EMPTY('.'), WALL('#'), UNKNOWN('?'), START('T'), CONTROL_ROOM('C');

	private static final Map<Character, CellType> BY_CHARACTER = new HashMap<>();

	static {
		for (CellType e : values()) {
			BY_CHARACTER.put(e.character, e);
		}
	}

	public final char character;

	private CellType(char character) {
		this.character = character;
	}

	public static CellType valueOfChar(char character) {
		return BY_CHARACTER.get(character);
	}
}
