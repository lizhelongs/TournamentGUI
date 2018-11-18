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
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is used to set margins by using empty vboxes
 *
 */
class BlankBracket {
    VBox vBox1;
    Label label1, label2;

    public BlankBracket() {
        this.label1 = new Label("");
        this.label2 = new Label("");
        this.vBox1 = new VBox(10.0);
        vBox1.getChildren().addAll(label1, label2);
        vBox1.setPrefWidth(400);
    }

    
    /**
     * @return an empty VBox
     */
    public VBox getVBox() {
        return vBox1;
    }

}


/**
 * Main holds a bracket object used to show the tournament 
 */
public class Main extends Application {

	
	Bracket bracket;
	Stage primaryStage;
	Scene scene;
	GridPane gPane;
	
	//used to put champions underneath finals game in gpane
	int centerCol;
	int championRow;
	
	/**
	 * Start method calls the method to create and display gui
	 * 
	 * @param primaryStage to display gui
	 */
    public void start(Stage primaryStage) {

		try {
			initializeBracketGUI(primaryStage);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("The file path specified is illegal");
		}

		//program only allows 0,1,2,4,8,16,32 teams per screen real estate
		catch (IllegalArgumentException e) {
			System.out.println("The file path does not contain a number of challengers"
					+ " that is to the power 2");
		}
    }

    /**
	 * Main method for java application
	 * 
	 * @param args - command line arguments
	 */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
	 * Shows the top finishers on the scene
	 */
    public void showChampionScene()
    {
    	
    	//store all results in gpane
    	GridPane gPane = new GridPane();

    	//create places to put all finishers
    	String[] topThree=bracket.getTop3Teams();
    	Label label1 = new Label("Champion: ");
    	Label label2 = new Label(topThree[0]); 	
    	Label label3 = new Label("2nd Place: ");
    	Label label4 = new Label(topThree[1]);
    	Label label5 = new Label("3rd Place:  ");
    	Label label6 = new Label(topThree[2]);
    	
    	label1.setMaxWidth(150);
    	label2.setMaxWidth(150);
    	label3.setMaxWidth(150);
    	label4.setMaxWidth(150);
    	label5.setMaxWidth(150);
    	label6.setMaxWidth(150);
    	
    	label1.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;-fx-background-color: red; -fx-text-fill: white;-fx-padding: 5;");
    	label2.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;-fx-background-color: red; -fx-text-fill: white;-fx-padding: 5;");
    	label3.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;-fx-background-color: tomato; -fx-text-fill: white;-fx-padding: 5;");
    	label4.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;-fx-background-color: tomato; -fx-text-fill: white;-fx-padding: 5;");
    	label5.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;-fx-background-color: orange; -fx-text-fill: white;-fx-padding: 5;");
    	label6.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;-fx-background-color: orange; -fx-text-fill: white;-fx-padding: 5;");

    	HBox box1=new HBox();
    	HBox box2=new HBox();
    	HBox box3=new HBox();
    	
    	box1.setMaxHeight(4);
    	box3.setMaxHeight(4);
    	box2.setMaxHeight(4);
    	
    	box1.getChildren().addAll(label1,label2);
    	
    	//add 2nd place if it exists
    	if(topThree[1]!=null) {
    	box2.getChildren().addAll(label3,label4);
    	}
    	
    	//add third place if it exists
    	if(topThree[2]!=null) {   	
    	box3.getChildren().addAll(label5,label6);
    	}
    	
    	VBox h = new VBox();
    	h.getChildren().addAll(box1,box2,box3);
    	h.setMaxHeight(20);
    	h.setMaxWidth(300);
    	
    	gPane.add(h, 1, 0);
    	
    	this.gPane.add(gPane,centerCol,championRow);
    	primaryStage.setScene(this.scene);
    }

    // Draws bracket on the stage
    public void initializeBracketGUI(Stage primaryStage) throws IOException {
        {
      
        	//Get command line arguments
        	List<String> args = getParameters().getRaw();
        	String fileName;
        	
        	//check if there is an argument (arg 0 would be filename)
        	if(args.size()>0) {
        		fileName=args.get(0);
        	}
        	
        	else { //otherwise default to included file
        		fileName = "teamNames_1.txt";
        	}
        	
        	this.primaryStage=primaryStage;
        
        	//Create bracket from file and get list of starting gamenodes
        	//passes main gui so that buttons can later call champion scene
            this.bracket = new Bracket(fileName,this);
            
            
            
            
            ArrayList<GameNode> games = bracket.getInitialGames();

            this.championRow=0;
            this.centerCol=0;
            
            this.gPane = new GridPane();
            this.scene = new Scene(gPane, 1500, 800);
            gPane.setStyle("-fx-background-color: #dddddd");    
            
            if(games.size()==0)
            {
            	showChampionScene();
            	return;
            }
               
            int teams = games.size()*2; //2 teams in each of the initial games

            //Rows and Columns Algorithm
            int numOfCol = (int) (Math.log(teams) / Math.log(2)) * 2 - 1;
            int numOfRow = teams / 2 - 1;
          
            //Two lists to be used to create TBD games in the bracket
            ArrayList<GameNode> recursionListLeft = new ArrayList<GameNode>();
            ArrayList<GameNode> recursionListRight = new ArrayList<GameNode>();          

            //Add first (leftmost) column
            for (int i = 0; i < numOfRow; i++) {
                  if (i % 2 == 0) {
                	  //Every other gets gamenode
                      gPane.add(games.get(i / 2).getBox(), 0, i);
                      recursionListLeft.add(games.get(i / 2));
                  } else {
                	  
                	  //Every other gets blank
                      BlankBracket blank = new BlankBracket();
                      gPane.add(blank.getVBox(), 0, i);
                  }
            }           

			//Add Last (rightmost) column
			for (int i = 0; i < numOfRow; i++) {
				if (i % 2 == 0) {
					//Every other gets gamenode
					gPane.add(games.get(i / 2 + teams / 4).getBox(), numOfCol - 1, i);
					recursionListRight.add(games.get(i / 2 + teams / 4));
				} else {
					
					//Every other gets blank
					BlankBracket blank = new BlankBracket();
					gPane.add(blank.getVBox(), numOfCol - 1, i);
				}
			}		
			
			//Determine how many columns between edges and center
			int recurseColumns = (int)numOfCol / 2 - 1;						
			
			//Add columns from the left and the right towards the center
			for(int j=0;j<recurseColumns;j++) {
				
				//Get nodes in previous column
				ArrayList<GameNode> tempLeft = new ArrayList<GameNode>();
				ArrayList<GameNode> tempRight = new ArrayList<GameNode>();
				
				//Trim down to every other gamenode since there are two that point to nexr node
				for(int z=0;z<recursionListLeft.size();z+=2) //increment by two
				{
					tempLeft.add(recursionListLeft.get(z).nextGame);
					tempRight.add(recursionListRight.get(z).nextGame);
				}
				
				recursionListLeft = tempLeft; //new list for left column
				recursionListRight = tempRight; //new list for right column
				
				
				//calculate index where first node goes in column
				int boxIndex = (int)Math.pow(2,j+1)-1;
				
				//keep track of how many nodes have been added
				int recursionListIndex=0;
				
				
				for(int i=0;i<numOfRow;i++) {
					if(i==boxIndex) //We have reached the row to add the next gamenode
					{
						//add gamenode in current row in left and right column
						gPane.add(recursionListLeft.get(recursionListIndex).getBox(),j+1,i);
						gPane.add(recursionListRight.get(recursionListIndex).getBox(),numOfCol-2-j,i);
						
						//increment and find next row to add a gamenode
						recursionListIndex++;
						boxIndex +=Math.pow(2,j+2);
					}
					
					else { //if not index of gamenode, add blank to left and right column
						BlankBracket blankLeft = new BlankBracket();
                        gPane.add(blankLeft.getVBox(), j+1, i);
                        
                        BlankBracket blankRight = new BlankBracket();
                        gPane.add(blankRight.getVBox(),numOfCol-2-j, i);
					}
					
				}
			}
			
			
			//Add the final game in the center column
			GameNode temp = bracket.getFinalGame(); //find from most recent column gamenode list
			
			//special condition for 2 teams
			if(numOfRow==0) {
				gPane.add(temp.getBox(),0,0);
			}
			
			
			//put the champion box in the center column underneath the finals game
			this.centerCol = (int)numOfCol/2;
			this.championRow=(int)numOfRow/2+1;			
			
			for(int i=0;i<numOfRow;i++) {
				if(i==(int)numOfRow/2) {
					//if center row, add final game
					gPane.add(temp.getBox(),(int)numOfCol/2,i);
				}
				
				else {
					
					//otherwise add a blank space
					BlankBracket blank = new BlankBracket();
                    gPane.add(blank.getVBox(),(int)numOfCol/2, i);
				}
			}						

			primaryStage.setScene(scene); //show the scene
			primaryStage.setTitle("TOURNAMENT BRACKET"); //add title to screen
		}
        	

        }
    
}
