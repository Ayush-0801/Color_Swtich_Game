import java.io.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.*;
import java.util.*;

public class SaveOrLoadGame extends Application {

    private Color bg=Color.rgb(54,54,54);
    private MediaPlayer clickbutton;

    public static ArrayList<SaveData> saveData=new ArrayList<>();

    public void comeToThisScene(Stage stage) throws IOException, ClassNotFoundException {
        start(stage);
    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        VBox n=new VBox();
        n.setAlignment(Pos.BASELINE_CENTER);
        n.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        ScrollPane sp=new ScrollPane();
        Scene scene = new Scene(n, 800, 800);
        sp.setStyle("-fx-background: transparent;" + "-fx-background-color: transparent;" + "-fx-padding:30 210 10 210 ;");

        FileInputStream input1 = new FileInputStream("src/Icons/Back.png");
        Image img1 = new Image(input1);
        ImageView backimg = new ImageView(img1);
        backimg.setFitHeight(70);
        backimg.setPreserveRatio(true);

        Button backbutton=new Button();
        backbutton.setTranslateX(-300);
        backbutton.setTranslateY(0);
        backbutton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 70px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-max-width: 70px; " +
                        "-fx-max-height: 70px;" +
                        "-fx-background-color: #555555;"
        );
        backbutton.setGraphic(backimg);
        backbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main obj=new Main();
                try {
                    clickbutton=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                    clickbutton.setVolume(0.04);
                    clickbutton.play();

                    obj.comeBackHome(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Text savetitle = new Text (200, 75, "SAVE GAMES");
        savetitle.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 75));
        savetitle.setFill(Color.WHITE);

        try {
            FileInputStream fs_sd = new FileInputStream("Saves/FullData.ser");
            ObjectInputStream ob_sd = new ObjectInputStream(fs_sd);

            while (true) {
                try {
                    saveData=(ArrayList<SaveData>)ob_sd.readObject();
                } catch (IOException e) {
                    break;
                }
            }
            ob_sd.close();
            fs_sd.close();

        } catch (IOException e) {
            ;
        } catch (ClassNotFoundException e) {
            ;
        }

        Group sgss=new Group();
        for(int i=0;i<saveData.size();i++)
        {
            Button sg[]=new Button[saveData.size()];
            sg[i]=new Button();
            sg[i].setTranslateX(225);
            sg[i].setTranslateY(150 + (100*i));
            if(i%2!=0)
            {
                sg[i].setStyle(
                        "-fx-background-radius: 20em; " +
                                "-fx-min-width: 350px; " +
                                "-fx-min-height: 70px; " +
                                "-fx-max-width: 350px; " +
                                "-fx-max-height: 70px;" +
                                "-fx-background-color: #F39734;"+
                                "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 );"+
                                "-fx-font-size: 30px;"+
                                "-fx-font-weight: bold;"+
                                "-fx-text-fill: white;"+
                                "-fx-font-family: \"Blissful Thinking\";"
                );
            }
            else {
                sg[i].setStyle(
                        "-fx-background-radius: 20em; " +
                                "-fx-min-width: 350px; " +
                                "-fx-min-height: 70px; " +
                                "-fx-max-width: 350px; " +
                                "-fx-max-height: 70px;" +
                                "-fx-background-color: #5D5D5D;" +
                                "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 );" +
                                "-fx-font-size: 30px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-family: \"Blissful Thinking\";"
                );
            }
            sg[i].setText(saveData.get(i).getName());
            sg[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    clickbutton=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                    clickbutton.setVolume(0.04);
                    clickbutton.play();

                    StartGame b=new StartGame();
                    try {
                        b.comeHereFromSave(stage,saveData.get(sgss.getChildren().indexOf(actionEvent.getSource())));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            sgss.getChildren().add(sg[i]);
        }
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setContent(sgss);

        n.getChildren().addAll(backbutton,savetitle,sp);

        scene.setFill(bg);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();

    }
}
