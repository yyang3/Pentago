import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * class to build the heuristic value of a board
 * @author Yicong Yang
 *
 */
public class HeuristicAndValueMap {
	/**
	 * heuristic map of the value on each row, col and diag
	 */
	private Map<String, int[]> myHeuristic;
	/**
	 * heuristic value for 2 in a row
	 */
	private static final int TWOINAROW = 1;
	/**
	 * heuristic value for 3 in a row
	 */
	private static final int THREEINAROW = 10;
	/**
	 * heuristic value for 4 in a row
	 */
	private static final int FOURINAROW = 100;
	/**
	 * heuristic value for 5 in a row
	 */
	public static final int FIVEINAROW = 100000000;
	/**
	 * helper string to form the heuristic key
	 */
	private static final String ROWHEAD = "row ";
	/**
	 * helper string to form the heuristic key
	 */
	private static final String COLHEAD = "col ";
	/**
	 * helper string to form the heuristic key
	 */
	private static final String DIAGHEAD = "diag ";
	/**
	 * the int representation for b win
	 */
	public static final int BWIN = 1;
	/**
	 * the int representation for w win
	 */
	public static final int WWIN = -1;
	/**
	 * the int representation for tie
	 */
	public static final int TIE = 0;
	/**
	 * the int representation for continue
	 */
	public static final int CONT = 2;
	public HeuristicAndValueMap(char [][] Input) {
		myHeuristic = new HashMap<>();
		updateH(Input);
	}
	public Map<String, int[]> getHeuristics () {
		return myHeuristic;
	} 
	/**
	 * helper method to update the 
	 * @param Input
	 */
	private final void updateH (char [][] Input) {
		//row
		for (int i = 0; i < 6; i ++) {
			myHeuristic.put(ROWHEAD + Integer.toString(i),new int[]{0,0});
			checkRow(i, Input);
		}
		//col
		for (int i = 0; i < 6; i ++) {
			myHeuristic.put(COLHEAD + Integer.toString(i),new int[]{0,0});
			checkCol(i, Input);
		}
		myHeuristic.put(DIAGHEAD + Integer.toString(1),new int[]{0,0});
		myHeuristic.put(DIAGHEAD + Integer.toString(2),new int[]{0,0});
		checkDiag(0, 0,Input);
		checkDiag(0, 5,Input);
	}
	/**
	 * method to check if a 2d state is winning
	 * @return 
	 */
	public int checkWin() {
		boolean bwin = false;
		boolean wwin = false;
		for (String a : myHeuristic.keySet()) {
			if (myHeuristic.get(a)[0] > 10000){
				bwin = true;
			} else if (myHeuristic.get(a)[1] > 10000) {
				wwin = true;
			} 
		}
		if (bwin && wwin) {
			return TIE;
		} else if (bwin) {
			return BWIN;
		} else if (wwin) {
			return WWIN;
		} else {
			return CONT;
		}
		
	}
	/**
	 * helper method to count the number of neighboring stones on one row
	 * @param i the index of row
	 * @param state the board state
	 * @return the heuristic value of this row
	 */
	private int checkRow(int i, char[][] state) {
//		System.out.println("Row starts!!!!!!!! Row " + i);
		int countb = 0;
		int countw = 0;
		Queue<Integer> tempb = new PriorityQueue<>(Collections.reverseOrder());
		Queue<Integer> tempw = new PriorityQueue<>(Collections.reverseOrder());
		for (int j = 0; j < 6; j ++) {
			if (state[i][j] == Console.BPLAYER) {
				countb ++;
				tempw.add(countw);
				countw =0;
			} else if (state[i][j] == Console.WPLAYER){
				countw ++;
				tempb.add(countb);
				countb = 0;
			} else {
				tempw.add(countw);
				countw =0;
				tempb.add(countb);
				countb =0;
			}
		}
		tempw.add(countw);
		countw =0;
		tempb.add(countb);
		countb =0;
		sumUp(ROWHEAD, i, 0, tempb.peek());
		sumUp(ROWHEAD, i, 1, tempw.peek());	
		return myHeuristic.get(ROWHEAD + Integer.toString(i))[0] - myHeuristic.get(ROWHEAD + Integer.toString(i))[1];
		
	}
	/**
	 * helper method to update the heuristic value of a column
	 * @param j col index
	 * @param state the 2d array of current status
	 * @return the difference between b and w on this col.
	 */
	private int checkCol(int j, char[][] state) {
		int countb = 0;
		int countw = 0;
		Queue<Integer> tempb = new PriorityQueue<>(Collections.reverseOrder());
		Queue<Integer> tempw = new PriorityQueue<>(Collections.reverseOrder());
		for (int i = 0; i < 6; i ++) {
			if (state[i][j] == Console.BPLAYER) {
				countb ++;
				tempw.add(countw);
				countw =0;
			} else if (state[i][j] == Console.WPLAYER){
				countw ++;
				tempb.add(countb);
				countb = 0;
			} else {
				tempw.add(countw);
				countw =0;
				tempb.add(countb);
				countb = 0;
			}
		}
		tempw.add(countw);
		countw =0;
		tempb.add(countb);
		countb =0;
		sumUp(COLHEAD, j, 0, tempb.peek());
		sumUp(COLHEAD, j, 1, tempw.peek());	
		return myHeuristic.get(COLHEAD + Integer.toString(j))[0] - myHeuristic.get(COLHEAD + Integer.toString(j))[1];
	}
	/**
	 * helper method to update the heuristic value of a diagnal line
	 * @param i row index
	 * @param j col index
	 * @param state the 2d array of current status
	 * @return the difference between b and w on this diagonal line.
	 */
	private int checkDiag(int i, int j, char[][] state) {
//		System.out.println("Diagnose starts!!!!!!!! I " + i + " j " + j);
		int countb = 0;
		int countw = 0;
		int diagChoice = -1;
		if (i == j) {
			diagChoice = 1;
		} else if (i + j == 5) {
			diagChoice = 2;
		} else {
			return 0;
		}
		Queue<Integer> tempb = new PriorityQueue<>(Collections.reverseOrder());
		Queue<Integer> tempw = new PriorityQueue<>(Collections.reverseOrder());
		for (int k = 0; k < 6; k ++) {
			int l = k;
			if (diagChoice == 2) {
				l = 5 - k;
			}
			if (state[k][l] == Console.BPLAYER) {
				countb ++;
				tempw.add(countw);
				countw =0;
			} else if (state[k][l] == Console.WPLAYER){
				countw ++;
				tempb.add(countb);
				countb = 0;
			} else {
				tempw.add(countw);
				countw =0;
				tempb.add(countb);
				countb = 0;
			}
		}
		tempw.add(countw);
		countw =0;
		tempb.add(countb);
		countb =0;
		sumUp(COLHEAD, j, 0, tempb.peek());
		sumUp(COLHEAD, j, 1, tempw.peek());	
		return myHeuristic.get(DIAGHEAD + Integer.toString(diagChoice))[0] - 
				myHeuristic.get(DIAGHEAD + Integer.toString(diagChoice))[1];
	}
	/**
	 * method to print out the maps
	 */
	public void print() {
		System.out.println("Heursitics!!!!!!!!!"); 
		for (String a : myHeuristic.keySet()) {
			System.out.println(a + ": b = " + myHeuristic.get(a)[0] + " w = " + myHeuristic.get(a)[1]);
		}
	}
	/**
	 * helper method to sum up the total heuristic value for a specific row, 
	 * col, or diagnose and update it into heuristic map
	 * @param rcd row col or diagnose
	 * @param place row/col/diagnose index
	 * @param player whose turn
	 * @param count the numebrs in a row
	 */
	private void sumUp(String rcd, int place, int player, int count) {
		if (count == 2) {
			myHeuristic.get(rcd + Integer.toString(place))[player] = 
					myHeuristic.get(rcd + 
							Integer.toString(place))[player] + TWOINAROW;
		} else if (count == 3) {
			myHeuristic.get(rcd + Integer.toString(place))[player] = 
					myHeuristic.get(rcd + 
							Integer.toString(place))[player] + THREEINAROW;
		} else if (count == 4) {
			myHeuristic.get(rcd + Integer.toString(place))[player] = 
					myHeuristic.get(rcd + 
							Integer.toString(place))[player] + FOURINAROW;
		} else if (count >= 5) {
			myHeuristic.get(rcd + Integer.toString(place))[player] = 
					myHeuristic.get(rcd + 
							Integer.toString(place))[player] + FIVEINAROW;
		}
	}

}
