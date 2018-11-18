//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////////////////////////
//
// Title:           Tournament Bracket
// Files:           BracketFX.java, Bracket.java, GameNode.java, GameFX.java
// Course:          COMP400 S18
// Team: 			A-Team 16
//
// Author:          Mike Hagenow, Zhelong Li, Xiaoyu Sun,Raghu Yadala, Yudai Yaguchi
// Email:           mhagenow@wisc.edu, zli765@wisc.edu, xsun256@wisc.edu,
//					yadala@wisc.edu, yyaguchi@wisc.edu
// Lecturer's Name: Deb Deppeler
/////////////////////////////// 100 COLUMNS WIDE //////////////////////////////////////////////////


package application;
import javafx.scene.layout.VBox;


/**
 * Gamenode holds information of a match between two teams
 */
public class GameNode {
	String team1,team2;
	int score1, score2;
	
	//tree structure is doubly-linked
	GameNode nextGame;
	GameNode[] prevGames;
	
	//link to visual elements
	GameFX gameFX;
	
	
	/**
	 * Constructor sets team names and links to bracket
	 * 
	 * @param team1 - name of first team in match
	 * @param team2 - name of second team in match
	 * @param bracketfx - link to overarching gui so that button can change scene
	 */
	GameNode(String team1, String team2,Main bracketfx){
		this.team1 = team1;
		this.team2 = team2;
		
		//create previous games array
		prevGames = new GameNode[2];
		
		//scores default to zero
		this.score1 = 0;
		this.score2 = 0;
		
		//create visual elements
		gameFX= new GameFX(team1, team2, bracketfx);
		
		//link GameFX back to this gamenode
		gameFX.setGame(this);
	}

	/**
	 * Accessor method for team 1 name
	 * 
	 * @return team1 - string for first team name
	 */
	public String getTeam1() {
		return team1;
	}

	/**
	 * Mutator method for team 1 name
	 * 
	 * @param team1 - string to set first team name
	 */
	public void setTeam1(String team1) {
		this.team1 = team1;
		gameFX.setLabel1();
	}

	/**
	 * Accessor method for team 2 name
	 * 
	 * @return team2 - string for first team name
	 */
	public String getTeam2() {
		return team2;
	}

	/**
	 * Mutator method for team 2 name
	 * 
	 * @param team2 - string to set second team name
	 */
	public void setTeam2(String team2) {
		this.team2 = team2;
		gameFX.setLabel2();
	}

	/**
	 * Accessor method for team 1 score
	 * 
	 * @return score1 - int for team 1 score
	 */
	public int getScore1() {
		return score1;
	}

	/**
	 * Mutator method for team 1 score
	 * 
	 * @param score1 - int to set first team score
	 */
	public void setScore1(int score1) {
		this.score1 = score1;
	}

	/**
	 * Accessor method for team 2 score
	 * 
	 * @return score2 - int for team 2 score
	 */
	public int getScore2() {
		return score2;
	}

	/**
	 * Mutator method for team 2 score
	 * 
	 * @param score1 - int to set second team score
	 */
	public void setScore2(int score2) {
		this.score2 = score2;
	}

	/**
	 * Accessor method for next game
	 * 
	 * ex. quarterfinal -> returns semifinal
	 * 
	 * @return GameNode for the next game in tree
	 */
	public GameNode getNextGame() {
		return nextGame;
	}

	/**
	 * Mutator method for next game
	 * 
	 * @param GameNode for the next game in tree
	 */
	public void setNextGame(GameNode nextGame) {
		this.nextGame = nextGame;
	}

	/**
	 * Accessor method for previous games
	 * 
	 * ex. semifinal -> returns 2 quarterfinals
	 * 
	 * @return GameNode array with two previous games
	 */
	public GameNode[] getPrevGames() {
		return prevGames;
	}

	/**
	 * Mutator method for previous games
	 * 
	 * @param GameNode array of two previous games in tree
	 */
	public void setPrevGames(GameNode node1, GameNode node2) {
		this.prevGames[0] = node1;
		this.prevGames[1] = node2;
	}
	
	/**
	 * Accessor method for visual element box
	 * 
	 * @return VBox containing all gameNode visual elements for GUI
	 */
	public VBox getBox() {
		return gameFX.getV();
	}
	
}
