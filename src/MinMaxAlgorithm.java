

/**
 * the class to do minmax
 * @author Yicong Yang
 *
 */
public class MinMaxAlgorithm {
	/**
	 * the starting state of the board
	 */
	private basics myBasics;
	/**
	 * the character represent the player
	 */
	private char myPlayer;
	/**
	 * the largest depth that this algorithm goes (have put and rotate separate here)
	 */
	private static final int DEPTHLIMIT = 3;
	/**
	 * constructor
	 * @param theBasics the board
	 * @param thePlayer the player
	 */
	public MinMaxAlgorithm (basics theBasics, char thePlayer) {
		myBasics = theBasics.clone();
		myPlayer = thePlayer;
	}
	
	/**
	 * tree size 2 levels, with one put and one rotate sublevel, so tree depth level 4, 
	 * maximum node = 36 * 35 * 8 * 8  = 80640
	 * @return
	 */
	
	public basics solution() {
		Node Start = new Node(myBasics, myPlayer);
		buildPut(Start, myPlayer, 0);
		uploadH(Start, 0);
		int result = Start.getH();
		for (Node temp : Start.getChildren()) {
			if (temp.getH() == result) {
				if (new HeuristicAndValueMap(temp.getBasics().getState()).checkWin() 
								!= HeuristicAndValueMap.CONT || temp.getBasics().getStepCount() >= 36) {
					System.out.println("NOt -2" + new HeuristicAndValueMap(temp.getBasics().getState()).checkWin() 
							+ "game finished: " + temp.getBasics().getStepCount());
					return temp.getBasics();
				} else {
					System.out.println("-2" + new HeuristicAndValueMap(temp.getBasics().getState()).checkWin() 
							+ "game not finished: " + temp.getBasics().getStepCount());
					for (Node temp2 : temp.getChildren()) {
						if (temp2.getH() == result) {
							return temp2.getBasics();
						}
					}
				}
				
			}
		}
		return null;
	}
	/**
	 * helper method to build put movement
	 * @param in the current board
	 * @param p the player
	 * @param depth current depth
	 */
	private void buildPut (Node in, char p, int depth){
		int [] platenum= {0,1,2,3};
		int [] placenum= {0,1,2,3,4,5,6,7,8,};
		if (depth != DEPTHLIMIT) {
			for (int i : platenum) {
				for (int j : placenum) {
					basics temp = in.getBasics().clone(); 
					if (temp.place(j, i, p)) {
						Node tempNode = new Node(temp, p);
						in.addC(tempNode);
//						temp.print();
						if (depth < DEPTHLIMIT && new HeuristicAndValueMap(tempNode.getBasics().getState()).checkWin() 
								== HeuristicAndValueMap.CONT) {
							bulidRotate (tempNode, p, depth + 1);
						}
					}
				}
			}
		}
	}
	/**
	 * helper method to build rotate movement
	 * @param in the current board
	 * @param p the player
	 * @param depth current depth
	 */
	private void bulidRotate (Node in, char p, int depth) {
//		System.out.println("In buildRot");
		int [] platenum= {0,1,2,3};
		char [] rotdir = {'l','r'};
		char [] player ={Console.BPLAYER, Console.WPLAYER};
		char pl = player[0];
		if (p == pl) {
			pl = player[1];
		}
		for (int i : platenum){
			for (char j : rotdir) {
				basics temp = in.getBasics().clone();
				if (temp.rotate(i, j)) {
					Node tempNode = new Node(temp, p);
					in.addC(tempNode);
//					temp.print();
					if (depth < DEPTHLIMIT && new HeuristicAndValueMap(tempNode.getBasics().getState()).checkWin() 
							== HeuristicAndValueMap.CONT) {
						buildPut (tempNode, pl, depth + 1);
					}
				}
			}
		}
	}
	/**
	 * helper method to update heuristic values for all nodes
	 * @param in the current board
	 * @param depth current depth
	 */
	private void uploadH (Node in, int depth) {
		char pl = 'b';
		if ((depth/ 2) % 2 == 0) {
			pl = myPlayer;
		} else {
			if (myPlayer == 'b') {
				pl = 'w';
			} else {
				pl = 'b';
			}
		}
		if (in.getH() == 0) {
			if (depth == DEPTHLIMIT || in.getChildren().isEmpty()) {
				in.setH();
			} else {
				for (Node temp : in.getChildren()) {
					uploadH(temp, depth + 1);
				}
				if (pl == Console.BPLAYER){
					in.getMax();
				} else {
					in.getMin();
				}
			} 
		}
		
	}
}
