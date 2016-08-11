import java.util.ArrayList;
import java.util.List;


public class basics {
	/**
	 * the array contains 4 plates for the pentago
	 */
	private List<char[][]> myPlates;

	/**
	 * constructor
	 */
	public basics () {
		myPlates = new ArrayList<>();
		for (int i = 0; i < 4; i ++) {
			myPlates.add(new char[3][3]);
		}
		initialize();
	}
	/**
	 * method to make one move and check the situation
	 * @param place place to put
	 * @param plate plate to put
	 * @param player player char
	 * @param plater plate to rotate
	 * @param direction direction to rotate
	 * @return 1 if b win
	 * 		   -1 if w win
	 * 		   -10 if put is wrong
	 * 		   -20 if rot is wrong
	 * 		   TIE if the game is tie
	 */
	public int step(int place, int plate, char player, int plater, char direction) {
		place = place - 1;
		plate = plate - 1;
		plater = plater - 1;
		System.out.println("place = " + 
				place + " plate = " + plate + "player = " + player + "plater = " + plater + "direction = " + direction);
		boolean pass = place(place, plate, player);
		if (!pass) {
			return -10;
		}
		print();
		HeuristicAndValueMap a = new HeuristicAndValueMap(getState());
		a.print();
		pass = pass && rotate (plater, direction);
		if (!pass) {
			return -20;
		}
		print();
		a = new HeuristicAndValueMap(getState());
		a.print();
		return -10;
	}

	/**
	 * method to put
	 * @param place place to put
	 * @param plate plate to put
	 * @param player player char
	 * @return if the move is right
	 */
	private boolean place(int place, int plate, char player) {
		int i = (place) / 3;
		int j = (place) % 3;
		if ((myPlates.get(plate))[i][j] == '-') {
			(myPlates.get(plate))[i][j] = player;
			return true;
		} else {
			System.out.println("Sth wrong: place = " + 
		place + " plate = " + plate + "player = " + player);
			return false;
		}
	}
	
	/**
	 * Method to rotate
	 * @param plater plate to rotate
	 * @param direction direction to rotate
	 * @return if the move is right
	 */
	private boolean rotate (int plater, char direction) {
		if (plater > -1 && plater < 4) {
			char[][] rotated = new char[3][3];
			char[][] current = myPlates.get(plater);
			//		right turn
			//		123     741    11 => 13, 12 => 23, 13 => 33
			//		456  => 852    21 => 12, 22 => 22, 23 => 32
			//		789     963    31 => 11, 32 => 21, 33 => 31
			if (direction == 'R' || direction == 'r') {
				rotated [0][2] = current [0][0];
				rotated [1][2] = current [0][1];
				rotated [2][2] = current [0][2];
				rotated [0][1] = current [1][0];
				rotated [2][1] = current [1][2];
				rotated [0][0] = current [2][0];
				rotated [1][0] = current [2][1];
				rotated [2][0] = current [2][2];
				rotated [1][1] = current [1][1];
			} else if (direction == 'L' || direction == 'l'){
				//			left turn
				//		123     369    11 => 31, 12 => 21, 13 => 11
				//		456  => 258    21 => 32, 22 => 22, 23 => 12
				//		789     147    31 => 33, 32 => 23, 33 => 13     
				rotated [2][0] = current [0][0];
				rotated [1][0] = current [0][1];
				rotated [0][0] = current [0][2];
				rotated [2][1] = current [1][0];
				rotated [0][1] = current [1][2];
				rotated [2][2] = current [2][0];
				rotated [1][2] = current [2][1];
				rotated [0][2] = current [2][2];
				rotated [1][1] = current [1][1];
			} else {
				System.out.println("there is no such an action.");
				return false;
			}
			myPlates.set(plater, rotated);
			return true;
		} else {
			System.out.println("there is no such plate to rotate.");
			return false;
		}
		
	}
	
	/**
	 * method to initialize a new game
	 */
	public final void initialize() {
		for (char[][] a : myPlates) {
			for (int i = 0; i < 3; i ++) {
				for (int j = 0; j < 3; j ++) {
					a[i][j] = '-';
				}
			}
		}
	}
	/**
	 * helper method to get the current state of the game
	 * @return 2d array represent the state
	 */
	public char[][] getState() {
		char[][] state = new char[6][6];
		for (int i = 0; i < 6; i ++) {
			for (int j = 0; j < 6; j ++) {
				if (j > -1 && j < 3) {
					if (i < 3) {
						state[i][j] = myPlates.get(0)[i][j];
					} else {
						state[i][j] = myPlates.get(2)[i-3][j];
					}
				} else {
					if (i < 3) {
						state[i][j] = myPlates.get(1)[i][j-3];
					} else {
						state[i][j] = myPlates.get(3)[i-3][j-3];
					}
				}
			}
		}
		return state;
	}
	public basics clone() {
		basics re = new basics();
		for (int k = 0; k < 4; k ++) {
			for (int i = 0; i < 3; i ++) {
				for (int j = 0; j < 3; j ++) {
					re.myPlates.get(k)[i][j] = myPlates.get(k)[i][j];
				}
			}
		}
		return re;
	}

	/**
	 * method to print the state
	 */
	public void print() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 9; i ++) {
			for (int j = 0; j < 9; j ++) {
				if (i == 0 || i == 4 || i == 8) {
					s.append('*');
				} else if (j == 0 || j == 4 || j == 8) {
					s.append('*');
				} else {
					if (j > 0 && j < 4) {
						if (i < 4) {
							s.append(myPlates.get(0)[i-1][j-1]);
						} else {
							s.append(myPlates.get(2)[i-5][j-1]);
						}
					} else {
						if (i < 4) {
							s.append(myPlates.get(1)[i-1][j-5]);
						} else {
							s.append(myPlates.get(3)[i-5][j-5]);
						}
					}
				}
			}
			s.append('\n');
		}
		System.out.println(s.toString());
		
	}

}
