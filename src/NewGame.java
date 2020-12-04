
import java.io.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.*;

public class NewGame extends Application {

    private Color bg=Color.rgb(54,54,54);
    private Player _newPlayer;
    private Pane holder;
    private Scene scene;
    private StartGame _beginNewGame;
    private Button submitButton;

    public void comeToThisScene(Stage stage) throws FileNotFoundException {
        start(stage);
    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage  stage) throws FileNotFoundException
    {
        holder=new Pane();
        scene = new Scene(holder, 800, 800);
        displayMenu(holder);

        Text savetitle = new Text (210, 300, "Enter Player Name:");
        savetitle.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 50));
        savetitle.setFill(Color.WHITE);

        TextField playername=new TextField();
        playername.setTranslateX(225);
        playername.setTranslateY(350);
        playername.setStyle( "-fx-background-radius: 20em; " +
                "-fx-min-width: 350px; " +
                "-fx-min-height: 70px; " +
                "-fx-max-width: 350px; " +
                "-fx-max-height: 70px;" +
                "-fx-background-color: #5D5D5D;"+
                "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 );"+
                "-fx-font-size: 30px;"+
                "-fx-font-weight: bold;"+
                "-fx-text-fill: white;"+
                "-fx-font-family: \"Blissful Thinking\";"
        );

        submitButton = new Button();
        submitButton.setTranslateX(320);
        submitButton.setTranslateY(450);
        submitButton.setStyle(
                "-fx-background-radius: 20em; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-max-width: 150px; " +
                        "-fx-max-height: 70px;" +
                        "-fx-background-color: #F39734;"+
                        "-fx-font-size: 30px;"+
                        "-fx-font-weight: bold;"+
                        "-fx-text-fill: white;"+
                        "-fx-font-family: \"Blissful Thinking\";"
        );
        submitButton.setText("SUBMIT");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(playername.getText().equals("")==false)
                {
                    _newPlayer=new Player();
                    _newPlayer.setName(playername.getText());
                    _beginNewGame=new StartGame();
                    try {
                        _beginNewGame.comeToThisScene(stage,_newPlayer);
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        EventHandler<KeyEvent> event=new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()==KeyCode.ENTER)
                {
                    if(!playername.getText().equals(""))
                    {
                        _newPlayer=new Player();
                        _newPlayer.setName(playername.getText());
                        _beginNewGame=new StartGame();

                        try {
                            _beginNewGame.comeToThisScene(stage,_newPlayer);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        holder.getChildren().addAll(playername ,savetitle ,submitButton);
        holder.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event);
        scene.setFill(bg);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();

    }

    public void displayMenu(Pane holder) throws FileNotFoundException {

        InputStream stream = new FileInputStream("src/Icons/Title.png");
        Image img = new Image(stream);
        ImageView titleimg= new ImageView();
        titleimg.setImage(img);
        titleimg.setX(270);
        titleimg.setY(30);
        titleimg.setFitWidth(250);
        titleimg.setPreserveRatio(true);

        int radiustitle=23;
        Obstacle titleCircleLeft=new CircleObs(355,55,radiustitle);
        Obstacle titleCircleRight=new CircleObs(445,55,radiustitle);

        ((CircleObs) titleCircleLeft).changeStroke(7);
        ((CircleObs) titleCircleRight).changeStroke(7);

        titleCircleLeft.setRotationAC();
        titleCircleRight.setRotationC();

        holder.getChildren().addAll(titleimg, titleCircleLeft.getGroup() , titleCircleRight.getGroup());
    }
}