
public class MinMaxAlgorithm {
	
	private HeuristicAndValueMap myHANDV;
	private basics myBasics;
	private char myPlayer;
	
	public MinMaxAlgorithm (basics theBasics, char thePlayer) {
		myHANDV = new HeuristicAndValueMap(theBasics.getState());
		myBasics = theBasics.clone();
		myPlayer = thePlayer;
	}
	
	
	
	public String solution() {
		return null;
	}

}
