//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////////////////////////
//
// Title: Tournament Bracket
// Files: BracketFX.java, Bracket.java, GameNode.java, GameFX.java
// Course: COMP400 S18
// Team: A-Team 16
//
// Author: Mike Hagenow, Zhelong Li, Xiaoyu Sun,Raghu Yadala, Yudai Yaguchi
// Email: mhagenow@wisc.edu, zli765@wisc.edu, xsun256@wisc.edu,
// yadala@wisc.edu, yyaguchi@wisc.edu
// Lecturer's Name: Deb Deppeler
/////////////////////////////// 100 COLUMNS WIDE //////////////////////////////////////////////////

package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * GameFx object holds Java FX components for game
 */
public class GameFX {
    // each element decides the structure of gameNode
    HBox hboxTeam1, hboxTeam2;
    Label labelTeam1, labelTeam2;
    TextField score1, score2;
    VBox box, box1;
    // score submission button
    Button button;
    // game node object contains the information about each game
    GameNode gameNode;

    /**
     * Constructor sets team names and bracketfx
     * 
     * @param team1 - name of first team in match
     * @param team2 - name of second team in match
     * @param bracketfx - link to overarching gui so that button can change scene
     */
    public GameFX(String team1, String team2, Main bracketfx) {

        // Create a pane
        BorderPane borderPane1 = new BorderPane();
        button = new Button("SUBMIT");

        // set the action of button
        button.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Set the action when the submit button is clicked
             * 
             * @see javafx.event.EventHandler#handle(javafx.event.Event)
             */
            @Override
            public void handle(ActionEvent event) {
                // Check if the score can be cast as an integer
                try {
                    gameNode.setScore1(Integer.parseInt(score1.getText()));
                    gameNode.setScore2(Integer.parseInt(score2.getText()));

                    // final game submit button
                    if (gameNode.getNextGame() == null) {
                    	gameNode.gameFX.disableNode();
                    	// switch to show top 3 on GUI
                        bracketfx.showChampionScene();
                    } else {
                        
                        // if the score is negative, throw exception
                        if (gameNode.getScore1() < 0 || gameNode.getScore2() < 0)
                            throw new NumberFormatException();

                        // if tie case, throw exception
                        if (gameNode.getScore1() == gameNode.getScore2())
                            throw new NumberFormatException();

                        // if the score of the team 1 is higher
                        else if (gameNode.getScore1() > gameNode.getScore2()) {
                            // team should be added as team 1 in the next gamenode
                            if (gameNode.getNextGame().getPrevGames()[0].equals(gameNode)) {
                                gameNode.getNextGame().setTeam1(gameNode.team1);
                                // set the previous node to be disabled
                                gameNode.gameFX.disableNode();
                                // check if next game can be enabled
                                gameNode.getNextGame().gameFX.checkDisable();
                            }
                            // team should be added as team 2 in the next gamenode
                            else {
                                gameNode.getNextGame().setTeam2(gameNode.team1);
                                gameNode.gameFX.disableNode();
                                gameNode.getNextGame().gameFX.checkDisable();
                            }
                        }
                        // team 2 score is higher
                        else {
                            if (gameNode.getNextGame().getPrevGames()[0].equals(gameNode)) {
                                gameNode.getNextGame().setTeam1(gameNode.team2);
                                gameNode.gameFX.disableNode();
                                gameNode.getNextGame().gameFX.checkDisable();
                            } else {
                                gameNode.getNextGame().setTeam2(gameNode.team2);
                                gameNode.gameFX.disableNode();
                                gameNode.getNextGame().gameFX.checkDisable();
                            }
                        }
                    }

                } catch (NumberFormatException ex) {
                    // show alert box if number can't be cast as integer
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Invalid input");
                    alert.setHeaderText(null);
                    alert.setContentText("All scores must be positive integers with no ties.");
                    alert.showAndWait();
                }
            }
        });

        // set the pane to center
        borderPane1.setCenter(button);

        // Team1 and Score1 in HBox
        hboxTeam1 = new HBox(10);
        hboxTeam1.setPrefWidth(180);
        labelTeam1 = new Label(team1);
        labelTeam1.setTextFill(Color.web("#ffffff"));
        labelTeam1.setFont(Font.font(null, FontWeight.BOLD, 12));
        labelTeam1.setPrefWidth(100);
        score1 = new TextField("score");
        score1.setPrefWidth(60);

        // add all the elements to hbox
        hboxTeam1.getChildren().addAll(labelTeam1, score1);

        // Team2 and Score2 in HBox
        hboxTeam2 = new HBox(10);
        labelTeam2 = new Label(team2);
        labelTeam2.setTextFill(Color.web("#ffffff"));
        labelTeam2.setFont(Font.font(null, FontWeight.BOLD, 12));
        labelTeam2.setPrefWidth(100);
        score2 = new TextField("score");
        score2.setPrefWidth(60);
        // add all the elements to hbox
        hboxTeam2.getChildren().addAll(labelTeam2, score2);

        // Add both teams to a VBox
        box = new VBox(10);
        box.setPrefHeight(30);
        box.getChildren().addAll(hboxTeam1, borderPane1, hboxTeam2);

        // Disable text field for TBD
        if (team1.equals("TBD") || team2.equals("TBD")) {
            disableNode();
        }
        // set the style
        box.setStyle("-fx-background-color: #C5050C;-fx-border-color:#666666;-fx-border-width: 2;");
    }

    /**
     * Accessor method for VBox
     * 
     * @return VBox - VBox with GUI element
     */
    public VBox getV() {
        return box;
    }

    /**
     * Mutator method for game
     * 
     * @param gameNode
     */
    public void setGame(GameNode gameNode) {
        this.gameNode = gameNode;
    }

    /**
     * Mutator method for Label1
     */
    public void setLabel1() {
        labelTeam1.setText(gameNode.getTeam1());
    }

    /**
     * Mutator method for Label2
     */
    public void setLabel2() {
        labelTeam2.setText(gameNode.getTeam2());
    }

    /**
     * Method checks whether a disabled gamenode should be enabled
     */
    public void checkDisable() {
        // should be enabled if both teams are set (not TBD)
        if (!this.labelTeam1.getText().equals("TBD") && !this.labelTeam2.getText().equals("TBD")) {
            // enable all JavaFX objects
            score1.setDisable(false);
            score2.setDisable(false);
            button.setDisable(false);
            labelTeam1.setDisable(false);
            labelTeam2.setDisable(false);
        }
    }

    /**
     * Method to disable all the fields
     */
    public void disableNode() {
        // disable all JavaFX objects
        score1.setDisable(true);
        score2.setDisable(true);
        button.setDisable(true);
        labelTeam1.setDisable(true);
        labelTeam2.setDisable(true);
    }
}
