import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.text.*;//Text, Font
import javafx.scene.effect.*; // Reflection, DropShadow
import javafx.scene.paint.Color;
import javafx.scene.layout.*;//HBox BorderPane
import javafx.geometry.*;// Pos, Insets
import javafx.scene.control.*;// TextField, Button, Label
import javax.swing.*;// JOptionPane
import javafx.scene.image.*;//Image , ImageView
import java.io.*;
import java.util.*;// List, OservableList, ArrayList

public class Main extends Application{

	Stage stage;

	
	Scene activeScene;

	HBox topBox;
	Image fceLogo;
	ImageView topLeft;
	TilePane bottomTile;
	Text welcomeText;
	Label matricNumLabel, emailLabel        ;
	TextField matric, email;
	Button accredit;
	Text fepictxt;
	HBox bx;
	GridPane centerNode;
	BorderPane accreditSceneBorder, confirmSceneBorder;
	Scene accreditScene, confirmScene;

	public static void main(String[] args) {
		launch(args);
	}

	

	public void start(Stage primaryStage){
		

		stage = primaryStage;
		Text topText = new Text("FEDERAL COLLEGE OF EDUCATION(TECH)AKOKA STUDENT UNION\n\t\t\tFECOETSU-ELECTION MANAGER");
		topText.setId("topText");
		topText.setFont(Font.font("Georgia", FontWeight.BOLD, 15));


		

		//Create the leftPart wit a VBox
		TilePane bottomTile = new TilePane();
		bottomTile.setId("bottomTile");


		/*
		GridPane of the work area
		Features of the work aree i.e the center of border pane
		Text that give a little information
		*/
		// Text to add to Grid pane
		String wlcmTxt = "Please input your\n matric number to get accredited";
		Text welcomeText = new Text(wlcmTxt);
		welcomeText.setId("welcomeText");
		welcomeText.setFont(Font.font("Lucida Calligraphy", FontWeight.BOLD, 15));

		matricNumLabel = new Label("MATRIC_NUMBER:\n");
		matricNumLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));

		//labels for choice boxes
		
		TextField matric = new TextField();
		matric.setPromptText("Enter Matric number here");
		//matric.setMinWidth(5);
		//matric.setPrefSize(30,10);

		emailLabel = new Label("Email");
		emailLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));

		email = new TextField("e.g danny@gmail.com");


		Button accredit = new Button("Accredit");
		accredit.setOnAction(
			e->{
				String id = matric.getText();
				doAccreditation(id);
			}
		);
		

		//Box to hold Fepic SUG Label in the HBox
		Text fepictxt = new Text("FECOEUTSU / FEPIC ");
		fepictxt.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
		HBox bx = new HBox();
		bx.setId("bx");
		bx.setAlignment(Pos.CENTER);
		bx.getChildren().add(fepictxt);

		// GridPane to hold welcome text, and Label and TextField(Matric number)
		GridPane centerNode = new GridPane();
		centerNode.setVgap(5);
		centerNode.setHgap(5);
		centerNode.setAlignment(Pos.CENTER);
		centerNode.add(welcomeText,0,0,2,1);// start adding nodes pf grid below
		centerNode.add(matricNumLabel,0,3);
		centerNode.add(matric,1,3);
		centerNode.add(emailLabel,0,4);
		centerNode.add(email,1,4);
		centerNode.setPadding(new Insets(25,25,25,25));
		centerNode.add(accredit,1,7);
		
		topBox = new HBox();
		topBox.setId("topBox");
		topBox.setAlignment(Pos.CENTER);
		topBox.getChildren().addAll(topText);
		//Create border and add nodes for Scene1
		BorderPane accreditSceneBorder = new BorderPane();
		accreditSceneBorder.setTop(topBox);
		accreditSceneBorder.setBottom(bottomTile);
		accreditSceneBorder.setCenter(centerNode);

		accreditScene = new Scene(accreditSceneBorder,900,600, Color.BLACK);// scene to resize 600, 900
		activeScene = accreditScene;

		activeScene.getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
		primaryStage.setScene(activeScene);
		primaryStage.getIcons().add(new Image("file:C:\\Users\\DANNY\\Desktop\\others\\progress\\icon.png"));
		primaryStage.setTitle("FECOETSU-VOTE");
		primaryStage.setFullScreen(false);
		primaryStage.show();//#2A5058

	}

private void doAccreditation(String val){
	Accreditor accreditor = new Accreditor(val);
	try{
		accreditor.accredit();
	}catch(Exception error){
		error.printStackTrace();
	}finally{
		matric.requestFocus();
	}
}