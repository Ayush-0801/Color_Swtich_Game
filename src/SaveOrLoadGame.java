import java.io.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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

//    public static ArrayList<SavePlayerStats> players=new ArrayList<SavePlayerStats>();
//    public static ArrayList<SaveObstaclesStats> player_obstacle=new ArrayList<SaveObstaclesStats>();
//    public static ArrayList<SaveColorSwitchers> player_colorSwitchers=new ArrayList<SaveColorSwitchers>();
//    public static ArrayList<SaveStars> player_stars=new ArrayList<SaveStars>();

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
//            FileInputStream fs = new FileInputStream("Saves/Players_List.ser");
//            ObjectInputStream ob = new ObjectInputStream(fs);
//
//            FileInputStream fs_obs = new FileInputStream("Saves/Obstacles_List.ser");
//            ObjectInputStream ob_obs = new ObjectInputStream(fs_obs);
//
//            FileInputStream fs_cs = new FileInputStream("Saves/ColorSwitchers.ser");
//            ObjectInputStream ob_cs = new ObjectInputStream(fs_cs);
//
//            FileInputStream fs_st = new FileInputStream("Saves/Stars.ser");
//            ObjectInputStream ob_st = new ObjectInputStream(fs_st);

            FileInputStream fs_sd = new FileInputStream("Saves/FullData.ser");
            ObjectInputStream ob_sd = new ObjectInputStream(fs_sd);

            while (true) {
                try {
//                    SavePlayerStats obj = (SavePlayerStats) ob.readObject();
//                    SaveObstaclesStats obstacles = (SaveObstaclesStats) ob_obs.readObject();
//                    SaveColorSwitchers csObjects = (SaveColorSwitchers) ob_cs.readObject();
//                    SaveStars stObjects = (SaveStars) ob_st.readObject();

//                    players = (ArrayList<SavePlayerStats>) ob.readObject();
//                    player_obstacle = (ArrayList<SaveObstaclesStats>) ob_obs.readObject();
//                    player_colorSwitchers = (ArrayList<SaveColorSwitchers>) ob_cs.readObject();
//                    player_stars = (ArrayList<SaveStars>) ob_st.readObject();

                    saveData=(ArrayList<SaveData>)ob_sd.readObject();
                } catch (IOException e) {
                    break;
                }
//            StartGame b=new StartGame();
//            b.comeHereFromSave(stage, obj , obstacles, csObjects, stObjects);

            }
//            ob.close();
//            fs.close();
//
//            ob_obs.close();
//            fs_obs.close();
//
//            ob_cs.close();
//            fs_cs.close();
//
//            ob_st.close();
//            fs_st.close();

            ob_sd.close();
            fs_sd.close();

        } catch (IOException e) {
            ;
        } catch (ClassNotFoundException e) {
            ;
        }

        Group sgss=new Group();
//        EventHandler<ActionEvent> event=new EventHandler<ActionEvent>() {
//            private int currentIndex;
//
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                StartGame b=new StartGame();
//                try {
//                    b.comeHereFromSave(stage, players.get(this.currentIndex), player_obstacle.get(this.currentIndex),
//                            player_colorSwitchers.get(this.currentIndex), player_stars.get(this.currentIndex));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//            public void getIndex(int i)
//            {
//                currentIndex=i;
//            }
//        };

//        System.out.println(players.size());
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
                    StartGame b=new StartGame();
                    try {
//                        b.comeHereFromSave(stage, players.get(sgss.getChildren().indexOf(actionEvent.getSource())), player_obstacle.get(sgss.getChildren().indexOf(actionEvent.getSource())),
//                                player_colorSwitchers.get(sgss.getChildren().indexOf(actionEvent.getSource())), player_stars.get(sgss.getChildren().indexOf(actionEvent.getSource())));
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
