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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Math;


/**
 *  Bracket class reads teams form a file and holds every game node
 */
public class Bracket {

	private GameNode finalGame;
	private String filepath;
	private ArrayList<GameNode> leaves;
	private Queue<GameNode> myQueue;
	
	//keep reference to gui so that buttons can modify scene
	private Main bracketfx;
	
	//used if file has 0 or 1 teams
	private String onlyOneTeam;

	/**
	 * This is an anonymous inner class containing queue implementation.
	 */
	class Queue<E> {
		private static final int initialCAPACITY = 150;  		// initial queue capacity
		private int capacityOfqueue;  					// queue capacity
		private int frontPointer;  						// index of the first element in queue
		private int nextPointer;  						// index of the next available array cell in queue
		private E[] Array;  							// array used to implement the queue

		Queue() {
			capacityOfqueue = initialCAPACITY + 1;
			Array = (E[]) new Object[capacityOfqueue];
			frontPointer = nextPointer = 0;
		}

		/**
		 * Enqueue follows the FIFO rule. Add the element to the array.
		 * 
		 * @return void
		 * */
		void enqueue(E element) {
			// check if array is out of space 
			if (size() == capacityOfqueue - 1) {
				int newSize = capacityOfqueue * 2;
				// increase array size
				E[] newArray = (E[]) new Object[newSize];
				for (int i = 0, j = frontPointer; i < size(); i++) {
					newArray[i] = Array[j];
					j = (++j) % capacityOfqueue;
				}
				// assign front and next pointers properly during wrap-around
				frontPointer = 0;
				nextPointer = capacityOfqueue - 1;
				// set old array pointer to new array
				capacityOfqueue = newSize;
				Array = newArray;
			}
			// insert element and increment next pointer
			Array[nextPointer] = element;
			nextPointer = (++nextPointer) % capacityOfqueue;
		}

		/**
		 * dequeue removes the element first in the queue. 
		 * 
		 * @return E element
		 * */
		E dequeue() {
			E element;
			if (isEmpty()) {
				return null;
			} else {
				element = Array[frontPointer]; //Retrieve Element
				Array[frontPointer] = null; //Set Pointer to null
				frontPointer = (++frontPointer) % capacityOfqueue;

				return element;
			}
		}

		/**
		 * Returns the size of the queue.
		 * 
		 * @return int size
		 * */
		int size() {
			return (capacityOfqueue - frontPointer + nextPointer) % capacityOfqueue;
		}

		/**
		 *	Returns a boolean value after checking if the queue is empty.
		 *
		 * @return boolean 
		 */
		boolean isEmpty() {
			return (frontPointer == nextPointer);
		}
	}

	public Bracket(String filepath,Main bracketfx) throws IOException, IllegalArgumentException{
		this.leaves = new ArrayList<>();				//Array List of the all the leaf nodes
		this.filepath = filepath;						//file path containing the file to be read
		this.myQueue = new Queue<>();					//Queue for making the binary tree
		this.finalGame = new GameNode("TBD", "TBD",bracketfx);	//Final Game Node, Root
		this.bracketfx=bracketfx;						//BrtacketFX object
		//When a new bracket object is created, teams are already seeded and the bracket data structure is already initialized 
		seedTeams();							
		initializeBracket();
	}

	/**
	 * This method reads a file and returns an ArrayList with team names in the order of their ranks. This method is
	 * automatically called when a Bracket Object is created. If the file is not found/illegal, then an exception with an 
	 * error message is thrown. 
	 * 
	 * @return Stream of Strings
	 * @throws IOException 
	 */
	private Stream<String> readTeams(String filepath) throws IOException {
		Stream<String> stream;
		stream = Files.lines(Paths.get(filepath));      
		stream = stream.map(String::trim).filter(x -> x != null && !x.equals(""))  //Map, Trim and filter
				.map(String::toUpperCase);
		return stream;
	}

	/**
	 * This method seeds the teams and decides the initial team game match-ups.
	 * If given order is [team1, team2, team3, team4], then game nodes created are [team1, team4], [team2, team3].
	 * This method is automatically called when a new Bracket Object is created.
	 * 
	 * @return void
	 * @throws IOException 
	 */
	private void seedTeams() throws IOException,IllegalArgumentException{

		Stream<String> teamsStream = this.readTeams(this.filepath);
		ArrayList<String> teamsNameList = (ArrayList<String>) teamsStream.collect(Collectors.toList());
		int teamSize = teamsNameList.size();
		
		//Due to screen constraints, limit number of teams to 32. Enforce that team number is a power of two
		if(!(teamSize==0 ||teamSize==1 ||teamSize==2 ||teamSize==4 ||teamSize==8 ||teamSize==16||teamSize==32)) {
			throw new IllegalArgumentException();
		}
		else {
			ArrayList<String> teamsListTemp = new ArrayList<>();

			if(teamsNameList.size()==0)
			{
				onlyOneTeam="No Teams";
				return;
			}

			if(teamsNameList.size()==1)
			{
				onlyOneTeam=teamsNameList.get(0);
				return;
			}

			int[] seed = {1, 2}; // Initialize seed as a two-element array containing 1 and 2
			int size = 2;
			int sum = 3;  //used to seed every teams
			int totalSize = teamsNameList.size();
			int i = 0;
			while (size < totalSize) {
				int[] newSeed = new int[size * 2]; // Create a new array with double size
				sum += sum - 1;
				i = 0;
				while (i < size) {  // copy elements to the new array
					newSeed[2 * i] = seed[i];
					newSeed[2 * i + 1] = sum - seed[i];
					i++;
				}
				seed = newSeed;
				size = size * 2;
			}
			for (int k = 0; k < seed.length; k++) {
				teamsListTemp.add(teamsNameList.get(seed[k] - 1));
			}
			for (int j = 0; j < totalSize / 2; j++) {
				GameNode newNode = new GameNode(teamsListTemp.get(j * 2), teamsListTemp.get(j * 2 + 1),bracketfx); //creates game nodes for leaves
				this.leaves.add(newNode); //Add to leaves
			}
		}

	}

	/**
	 * The Binary tree is constructed from the leaves to the root in reverse order. In order to do this all the leaves 
	 * will be put into a queue first. Next, the first two nodes will be dequeued, a new parent node will be created which 
	 * is essentially the next game. The newly created node is put back into the queue and this process is continued till 
	 * the queue is emptied. The newly created nodes will have teams set as "TBD" nodes. This method is automatically called
	 * when a new Bracket Object is called.
	 * 
	 * @return void
	 */
	private void initializeBracket(){


		for (GameNode node : this.leaves) {
			this.myQueue.enqueue(node);
		}
		if (myQueue.size() == 1) {// Special case: 2 teams (1 game)
			GameNode finalGame = myQueue.dequeue();
			this.finalGame = finalGame;
		}
		else if (myQueue.size() > 1) {		// More than two games
			while (!myQueue.isEmpty()) {
				if (myQueue.size() != 2) {
					//Dequeue the first two nodes 
					GameNode node1 = myQueue.dequeue();    
					GameNode node2 = myQueue.dequeue();
					//Create a new parent node
					GameNode newNode = new GameNode("TBD", "TBD", bracketfx);
					//Set the links
					node1.setNextGame(newNode);
					node2.setNextGame(newNode);
					newNode.setPrevGames(node1, node2);
					//Add the parent to the queue
					myQueue.enqueue(newNode);
				} else {  //If we are at semi-finals and the final game node is to be Final Game
					GameNode node1 = myQueue.dequeue();
					GameNode node2 = myQueue.dequeue();
					node1.setNextGame(this.finalGame);
					node2.setNextGame(this.finalGame);
					this.finalGame.setPrevGames(node1, node2);
				}
			}
		}
	}





	/**
	 * This method checks the the final 3 game nodes to find the top 3 games.
	 * 
	 * @return Array of Strings
	 */
	String[] getTop3Teams() {
		String[] top3 = new String[3];
		String first;
		String second;
		String third;

		if(onlyOneTeam!=null)						//If only 1 team is present in the competition 
		{
			top3[0]=onlyOneTeam;
			return top3;
		}

		if (this.finalGame.getScore1() > this.finalGame.getScore2()) {   //Checking the scores in the final Game
			first = this.finalGame.getTeam1();
			second = this.finalGame.getTeam2();
		} else {
			first = this.finalGame.getTeam2();
			second = this.finalGame.getTeam1();
		}


		top3[0] = first;
		top3[1] = second;
		if(leaves.size()==1) {
			top3[2] = null;
			return top3;
		}

		GameNode left = this.finalGame.getPrevGames()[0];
		GameNode right = this.finalGame.getPrevGames()[1];
		//Checking the next two games, semi-finals and comparing scores
		if (Math.min(left.getScore1(), left.getScore2()) > Math.min(right.getScore1(), right.getScore2())) {
			if (left.getScore1() < left.getScore2()) {
				third = left.getTeam1();
			} else {
				third = left.getTeam2();
			}
		} else {
			if (right.getScore2() < right.getScore1()) {
				third = right.getTeam2();
			} else {
				third = right.getTeam1();
			}
		}
		top3[0] = first;
		top3[1] = second;
		top3[2] = third;
		return top3;
	}

	/**
	 * Given a childNode, this method updates its parent. That is, the qualifying team is entered into the
	 * next game node.
	 * 
	 * @return void 
	 */
	void updateNodeParent(GameNode childNode) {
		String team;
		if (childNode.getScore1() > childNode.getScore2()) {
			team = childNode.getTeam1();
		} else {
			team = childNode.getTeam2();
		}
		childNode.getNextGame().setTeam1(team);
	}

	/**
	 * This method is a getter method for the root which is the final Game.
	 * 
	 * @return GameNode finalGame
	 * */
	GameNode getFinalGame() {
		return this.finalGame;
	}

	/**
	 * This method is a getter method for the initial games which are the leaves of the tree. 
	 * 
	 * @return ArrayList of Leaves
	 * */
	ArrayList<GameNode> getInitialGames(){
		return this.leaves;
	}

	/**
	 * This method returns the total number of games that will take place in the tournament.
	 * 
	 * @return integer value of total no. of games
	 */
	int totalNumberOfGames() {
		return 2 * this.leaves.size() - 1;
	}

	/**
	 * This method returns the height of the Bracket tree.
	 * 
	 * @return integer height
	 */
	int TreeHeight() {
		return (int) (Math.log(totalNumberOfGames() + 1) / Math.log(2));
	}
}
