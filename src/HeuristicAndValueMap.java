import java.util.HashMap;
import java.util.Map;


public class HeuristicAndValueMap {
	/**
	 * heuristic map of the value on each row, col and diag
	 */
	private Map<String, int[]> myHeuristic;
	/**
	 * 2d array for the heuristic value of the positions
	 */
	private int[][] myValueb;
	/**
	 * 2d array for the heuristic value of the positions
	 */
	private int[][] myValuew;
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
	private static final int FIVEINAROW = 100000000;
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
	
	
	public HeuristicAndValueMap(char [][] Input) {
		myHeuristic = new HashMap<>();
		myValueb = new int[6][6];
		myValuew = new int[6][6];
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
			checkRow(i, Input);
		}
		//col
		for (int i = 0; i < 6; i ++) {
			checkCol(i, Input);
		}
		checkDiag(0, 0,Input);
		checkDiag(0, 5,Input);
	}

	private int checkRow(int i, char[][] state) {
//		System.out.println("Row starts!!!!!!!! Row " + i);
		myHeuristic.put(ROWHEAD + Integer.toString(i),new int[]{0,0});
		int countb = 0;
		int countw = 0;
		char prev = '-';
		for (int j = 0; j < 6; j ++) {
			if (state[i][j] == 'b') {
				myValueb[i][j] = -10;
				myValuew[i][j] = -10;
				if (prev == 'w' && countw > 1) {
					sumUp(ROWHEAD, i, 1, countw);
//					System.out.println("Should count w now");
					countw = 0;
				} 
				countb ++;
				if (j == 5) {
					sumUp(ROWHEAD, i, 0, countb);
					if (j - countb> 0  && myValueb[i][j - countb] >= 0) {
						myValueb[i][j - countb] += countb;
					}
//					System.out.println("Should count b now");
					countb = 0;
				}
			} else if (state[i][j] == 'w') {
				myValueb[i][j] = -10;
				myValuew[i][j] = -10;
				if (prev == 'b' && countb > 1) {
					sumUp(ROWHEAD, i, 0, countb);
//					System.out.println("Should count b now");
					countb = 0;
				}
				countw ++;
				if (j == 5) {
					sumUp(ROWHEAD, i, 1, countw);
					if (j - countw> 0  && myValuew[i][j - countw] >= 0) {
						myValuew[i][j - countw] += countw;
					}
//					System.out.println("Should count w now");
					countw = 0;
					
				}
			} else {
				if (prev == 'w') {
					if (countw > 1){
						sumUp(ROWHEAD, i, 1, countw);	
					}
					if (j - countw - 1> 0  && myValuew[i][j - countw - 1] >= 0) {
						myValuew[i][j - countw - 1] += countw;
					}
					myValuew[i][j] = countw;
//					System.out.println("Should count w now");
				} else if (prev == 'b'){
					if (countb > 1){
						sumUp(ROWHEAD, i, 0, countb);
					}
					if (j - countb - 1> 0  && myValueb[i][j - countb - 1] >= 0) {
						myValueb[i][j - countb - 1] += countb;
					}
					myValueb[i][j] = countb;
//					System.out.println("Should count b now");
				} 
				countw = 0;
				countb = 0;
			}
			prev = state[i][j];
//			System.out.print(prev);
		}
//		System.out.println();
		return myHeuristic.get(ROWHEAD + Integer.toString(i))[0] - myHeuristic.get(ROWHEAD + Integer.toString(i))[1];
		
	}
	/**
	 * helper method to update the heuristic value of a column
	 * @param j col index
	 * @param state the 2d array of current status
	 * @return the difference between b and w on this col.
	 */
	private int checkCol(int j, char[][] state) {
//		System.out.println("Cols starts!!!!!!!! Col " + j);
		myHeuristic.put(COLHEAD + Integer.toString(j),new int[]{0,0});
		int countb = 0;
		int countw = 0;
		char prev = '-';
		for (int i = 0; i < 6; i ++) {
			if (state[i][j] == 'b') {
				myValueb[i][j] = -10;
				myValuew[i][j] = -10;
				if (prev == 'w' && countw > 1) {
					sumUp(COLHEAD, j, 1, countw);
//					System.out.println("Should count w now");
					countw = 0;
				} 
				countb ++;
				if (i == 5) {
					sumUp(COLHEAD, j, 0, countb);
//					System.out.println("Should count b now");
					if (i - countb> 0  && myValueb[i - countb][j] >= 0) {
						myValueb[i - countb][j] += countb;
					}
					countb = 0;
				}
			} else if (state[i][j] == 'w') {
				myValueb[i][j] = -10;
				myValuew[i][j] = -10;
				if (prev == 'b' && countb > 1) {
					sumUp(COLHEAD, j, 0, countb);
//					System.out.println("Should count b now");
					countb = 0;
				}
				countw ++;
				if (i == 5) {
					sumUp(COLHEAD, j, 1, countw);
					if (i - countw> 0  && myValuew[i - countw][j] >= 0) {
						myValuew[i - countw][j] += countw;
					}
//					System.out.println("Should count w now");
					countw = 0;
				}
			} else {
				if (prev == 'w') {
					if (countw > 1){
						sumUp(COLHEAD, j, 1, countw);	
					}
					if (i - countw - 1> 0  && myValuew[i - countw - 1][j] >= 0) {
						myValuew[i - countw - 1][j] += countw;
					}
					myValuew[i][j] += countw;
//					System.out.println("Should count w now, countw = " + countw);
				} else if (prev == 'b'){
					if (countb > 1){
						sumUp(COLHEAD, j, 0, countb);
					}
					if (i - countb - 1> 0  && myValueb[i - countb - 1][j] >= 0) {
						myValueb[i - countb - 1][j] += countb;
					}
					myValueb[i][j] += countb;
//					System.out.println("Should count b now, countb = " + countb);
				} 
				countw = 0;
				countb = 0;
			}
			prev = state[i][j];
		}
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
		char prev = '-';
		int diagChoice = -1;
		if (i == j) {
			diagChoice = 1;
		} else if (i + j == 5) {
			diagChoice = 2;
		} else {
			return 0;
		}
		myHeuristic.put(DIAGHEAD + Integer.toString(diagChoice),new int[]{0,0});
		int l = 0;
		for (int k = 0; k < 6; k ++) {
			if(diagChoice == 1) {
				l = k;
			} else {
				l = 5 - k;
			}
			if (state[k][l] == 'b') {
				myValueb[k][l] = -10;
				myValuew[k][l] = -10;
				if (prev == 'w' && countw > 1) {
					sumUp(DIAGHEAD, diagChoice, 1, countw);
//					System.out.println("Should count w now");
					countw = 0;
				} 
				countb ++;
				if (k == 5) {
					sumUp(DIAGHEAD, diagChoice, 0, countb);
//					System.out.println("Should count b now");
					if (diagChoice == 1 && k - countb> 0  && myValueb[k - countb][l - countb] >= 0) {
						myValueb[k - countb][l - countb] += countb;
					} else if (diagChoice == 2 && k - countb > 0 && myValueb[k - countb][l + countb] >= 0) {
						myValueb[k - countb][l + countb] += countb;
					}
					countb = 0;
				}
			} else if (state[k][l] == 'w') {
				myValueb[k][l] = -10;
				myValuew[k][l] = -10;
				if (prev == 'b' && countb > 1) {
					sumUp(DIAGHEAD, diagChoice, 0, countb);

//					System.out.println("Should count b now");
					countb = 0;
				}
				countw ++;
				if (k == 5) {
					sumUp(DIAGHEAD, diagChoice, 1, countw);
//					System.out.println("Should count w now");
					if (diagChoice == 1 && k - countw> 0  && myValuew[k - countw][l - countw] >= 0) {
						System.out.println(myValuew[k - countw][l - countw]);
						myValuew[k - countw][l - countw] += countw;
					} else if (diagChoice == 2 && k - countb> 0 && myValuew[k - countb][l + countb] >= 0) {
						myValuew[k - countw][l + countw] += countw;
					}
					countw = 0;
				}
			} else {
				if (prev == 'w') {
					if (countw > 1){
						sumUp(DIAGHEAD, diagChoice, 1, countw);	
					}
//					System.out.println("k = " + k + " l = " + l + " DC = " + diagChoice + " if k - countw > 0 " + 
//					(k - countw > 0)  +  " k - countw: " + (k - countw) + "l - countw " + (l - countw ) + 
//					" value with kl" + myValuew[k - countw][l - countw ]+ "Countw = " + countw);
//					System.out.println(diagChoice == 1 && k - countw - 1> 0  && myValuew[k - countw - 1][l - countw - 1] >= 0);
					if (diagChoice == 1 && k - countw - 1> 0  && myValuew[k - countw - 1][l - countw - 1] >= 0) {
						System.out.println(myValuew[k - countw - 1][l - countw - 1]);
						myValuew[k - countw - 1][l - countw - 1] += countw;
					} else if (diagChoice == 2 && k - countb - 1> 0 && myValuew[k - countb - 1][l + countb - 1] >= 0) {
						myValuew[k - countw - 1][l + countw + 1] += countw;
					}
					myValuew[k][l] += countw;
//					System.out.println("Should count w now, countw = " + countw);
				} else if (prev == 'b'){
					if (countb > 1) {
						sumUp(DIAGHEAD, diagChoice, 0, countb);
					}
					if (diagChoice == 1 && k - countb - 1> 0  && myValueb[k - countb - 1][l - countb - 1] >= 0) {
						myValueb[k - countb - 1][l - countb - 1] += countb;
					} else if (diagChoice == 2 && k - countb > 0 && myValueb[k - countb - 1][l + countb + 1] >= 0) {
						myValueb[k - countb - 1][l + countb + 1] += countb;
					}
					myValueb[k][l] += countb;
//					System.out.println("Should count b now, countb = " + countb);
				} 
				countw = 0;
				countb = 0;
			}
			prev = state[k][l];
//			System.out.print(prev);
		}
//		System.out.println();
		return myHeuristic.get(DIAGHEAD + Integer.toString(diagChoice))[0] - 
				myHeuristic.get(DIAGHEAD + Integer.toString(diagChoice))[1];
	}
	/**
	 * method to print out the maps
	 */
	public void print() {
//		System.out.println("Heursitics!!!!!!!!!"); 
//		for (String a : myHeuristic.keySet()) {
//			System.out.println(a + ": b = " + myHeuristic.get(a)[0] + " w = " + myHeuristic.get(a)[1]);
//		}
		System.out.println("Values B!!!!!!!!!");
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 6; i ++) {
			for (int j = 0; j < 6; j ++) {
				s.append(String.format("%4d",myValueb[i][j]));
				s.append(' ');
			}
			s.append('\n');
		}
		System.out.println(s.toString());
		System.out.println("Values W!!!!!!!!!");
		s = new StringBuilder();
		for (int i = 0; i < 6; i ++) {
			for (int j = 0; j < 6; j ++) {
				s.append(String.format("%4d", myValuew[i][j]));
				s.append(' ');
			}
			s.append('\n');
		}
		System.out.println(s.toString());
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
//		if (rcd.equals(DIAGHEAD)) 
//			System.out.println("\n\n" + count);
	}
}
