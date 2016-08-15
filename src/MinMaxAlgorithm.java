import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


public class MinMaxAlgorithm {
	private basics myBasics;
	private char myPlayer;
	private static final int DEPTHLIMIT = 4;
	
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
		System.out.println("In solution!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Node Start = new Node(myBasics, myPlayer);
		buildPut(Start, myPlayer, 0);
		uploadH(Start, 1, myPlayer);
		int result = Start.getH();
		for (Node temp : Start.getChildren()) {
			if (temp.getH() == result) {
				return temp.getBasics();
			}
		}
		return null;
	}

	private void buildPut (Node in, char p, int depth){
		int [] platenum= {0,1,2,3};
		int [] placenum= {0,1,2,3,4,5,6,7,8,};
		if (depth == DEPTHLIMIT) {
			in.setH();
		} else {
			for (int i : platenum) {
				for (int j : placenum) {
					basics temp = in.getBasics().clone(); 
					if (temp.place(j, i, p)) {
						Node tempNode = new Node(temp, p);
						in.addC(tempNode);
//						temp.print();
						if (depth < DEPTHLIMIT) {
							bulidRotate (tempNode, p, depth + 1);
						}
					}
				}
			}
		}
	}
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
					if (depth < DEPTHLIMIT) {
						buildPut (tempNode, pl, depth + 1);
					}
				}
			}
		}
	}
	private void uploadH (Node in, int depth, char p) {
		char pl = 'b';
		if (depth%2 ==0) {
			pl = p;
		} else {
			if (p == 'b') {
				pl = 'w';
			} else {
				pl = 'b';
			}
		}
		
		if (depth == DEPTHLIMIT) {
			in.setH();
		} else {
			for (Node temp : in.getChildren()) {
				uploadH(temp, depth + 1, pl);
			}
			if (p == Console.BPLAYER){
				if (((depth - 1)/ 2) % 2 ==0) {
					in.getMax();
				} else {
					in.getMin(); 
				}
			} else {
				if (((depth - 1)/ 2) % 2 ==0) {
					in.getMin();
				} else {
					in.getMax();
				}
			}
		} 
	}
	
	
	
	
	

}
class Node {
	private basics myBasics;
	private int HeuristicValue;
	private List<Node> myChildren;
	private char player;
	
	public Node(basics Input, char p) {
		myBasics = Input;
		player = p;
		HeuristicAndValueMap temp = new HeuristicAndValueMap(Input.getState());
		if (temp.checkWin() == HeuristicAndValueMap.BWIN) {
			HeuristicValue = HeuristicAndValueMap.FIVEINAROW;
		} else if (temp.checkWin() == HeuristicAndValueMap.WWIN) {
			HeuristicValue = -HeuristicAndValueMap.FIVEINAROW;
		} else if (temp.checkWin() == HeuristicAndValueMap.TIE) {
			if (player == Console.BPLAYER) {
				HeuristicValue = HeuristicAndValueMap.FIVEINAROW - 1000;
			} else {
				HeuristicValue = - HeuristicAndValueMap.FIVEINAROW + 1000;
			}
		} else {
			HeuristicValue = 0;
		}	
		myChildren = new ArrayList<>();
	}
	public basics getBasics() {
		return myBasics;
	}
	
	public void setH() {
		HeuristicAndValueMap temp = new HeuristicAndValueMap(myBasics.getState());
		Map<String, int[]> theMap = temp.getHeuristics();
		for (String e : theMap.keySet()) {
			HeuristicValue = HeuristicValue + theMap.get(e)[0] - theMap.get(e)[1];
		}
		
		
	}
	
	public int getH(){
		return HeuristicValue;
	}
	
	public void addC(Node i) {
		myChildren.add(i);
	}
	
	public Node getMin () {
		Queue<Node> sort = new PriorityQueue <>(compMin());
		if(myChildren.isEmpty()) {
			return this;
		} else {
			for (Node temp : myChildren) {
				sort.add(temp);
			}
		}
		HeuristicValue = sort.peek().getH();
		return sort.peek();
	}
	
	public List<Node> getChildren () {
		return myChildren;
	}
	
	public Node getMax () {
		Queue<Node> sort = new PriorityQueue <>(compMax());
		if(myChildren.isEmpty()) {
			return this;
		} else {
			for (Node temp : myChildren) {
				sort.add(temp);
			}
		}
		HeuristicValue = sort.peek().getH();
		return sort.peek();
	}
	
	private Comparator<Node> compMin() {
		Comparator<Node> h1 =  new Comparator<Node>(){  
            public int compare(Node o1, Node o2) {  
                // TODO Auto-generated method stub  
                int numbera = o1.getH();  
                int numberb = o2.getH();  
                if(numberb > numbera){  
                    return -1;  
                }  
                else if(numberb<numbera){  
                    return 1;  
                }  
                else {  
                    return 0;  
                }  
              
            }  
		};
		return h1;
	}
	private Comparator<Node> compMax() {
		
		Comparator<Node> h1 =  new Comparator<Node>(){  
            public int compare(Node o1, Node o2) {  
                // TODO Auto-generated method stub  
                int numbera = o1.getH();  
                int numberb = o2.getH(); 
                if(numberb > numbera){  
                    return 1;  
                }  
                else if(numberb<numbera){  
                    return -1;  
                }  
                else {  
                    return 0;  
                }  
              
            }  
		};
		return h1;
	}
}

