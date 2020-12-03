
import java.io.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.*;

public class Main extends Application
{
    private Color bg=Color.rgb(54,54,54);
    private NewGame newGame;
    private SaveOrLoadGame saveGames;
    private Pane holder;
    private Scene scene;

    private static MediaPlayer m1;
    @Override
    public void start(Stage  stage) throws Exception
    {
        holder=new Pane();
        scene = new Scene(holder, 800, 800);

        String path="Music/gameplay.mp3";
//        Media m=new Media(new File(path).toURI().toString());
//        m1=new MediaPlayer(m);
//        m1.setAutoPlay(true);

        FileInputStream input1 = new FileInputStream("src/Icons/PlayButton.png");
        Image img1 = new Image(input1);
        ImageView playimg = new ImageView(img1);
        playimg.setFitHeight(150);
        playimg.setPreserveRatio(true);

        FileInputStream input2 = new FileInputStream("src/Icons/SaveGames.png");
        Image img2 = new Image(input2);
        ImageView savegameimg = new ImageView(img2);
        savegameimg.setFitHeight(80);
        savegameimg.setPreserveRatio(true);

        FileInputStream input3 = new FileInputStream("src/Icons/Exit.png");
        Image img3 = new Image(input3);
        ImageView exitimg = new ImageView(img3);
        exitimg.setFitHeight(60);
        exitimg.setPreserveRatio(true);

        Button playbutton=new Button();
        playbutton.setTranslateX(325);
        playbutton.setTranslateY(275);
        playbutton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 150px; " +
                        "-fx-max-width: 150px; " +
                        "-fx-max-height: 150px;" +
                        "-fx-background-color: #555555"
        );
        playbutton.setGraphic(playimg);
        playbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    newGame=new NewGame();
                    newGame.comeToThisScene(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button savegamesbutton = new Button();
        savegamesbutton.setTranslateX(325);
        savegamesbutton.setTranslateY(525);
        savegamesbutton.setStyle(
                "-fx-background-radius: 0em; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 150px; " +
                        "-fx-max-width: 150px; " +
                        "-fx-max-height: 150px;" +
                        "-fx-background-color: transparent"
        );
        savegamesbutton.setGraphic(savegameimg);
        savegamesbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    saveGames=new SaveOrLoadGame();
                    saveGames.comeToThisScene(stage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        Button exitbutton = new Button();
        exitbutton.setTranslateX(325);
        exitbutton.setTranslateY(650);
        exitbutton.setStyle(
                "-fx-background-radius: 0em; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 150px; " +
                        "-fx-max-width: 150px; " +
                        "-fx-max-height: 150px;" +
                        "-fx-background-color: transparent"
        );
        exitbutton.setGraphic(exitimg);
        exitbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });

        displayGameName(holder);

        int radius1=90,radius2=115,radius3=145;
        Obstacle innerCircle=new CircleObs(400,350,radius1);
        Obstacle middleCircle=new CircleObs(400,350,radius2);
        Obstacle outerCircle=new CircleObs(400,350,radius3);

        ((CircleObs) middleCircle).changeStroke(20);
        ((CircleObs) outerCircle).changeStroke(25);

        innerCircle.setRotationAC();
        middleCircle.setRotationC();
        outerCircle.setRotationAC();

        ScaleTransition st = new ScaleTransition(Duration.millis(1000), savegamesbutton);
        st.setByX(0.10);
        st.setByY(0.10);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        st.play();

        holder.getChildren().addAll(innerCircle.getGroup(),
                middleCircle.getGroup(), outerCircle.getGroup()
                ,playbutton,savegamesbutton,exitbutton);

        holder.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        scene.setFill(bg);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void displayGameName(Pane holder) throws FileNotFoundException {

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
        titleCircleLeft.setRotationAC();
        titleCircleRight.setRotationC();
        ((CircleObs) titleCircleLeft).changeStroke(7);
        ((CircleObs) titleCircleRight).changeStroke(7);

        holder.getChildren().addAll(titleimg, titleCircleLeft.getGroup()
        , titleCircleRight.getGroup());
    }

    public void comeBackHome(Stage stage) throws Exception {
        start(stage);
    }
}

//        RotateTransition rotate1 = new RotateTransition();
//        rotate1.setAxis(Rotate.Z_AXIS);
//        rotate1.setFromAngle(0);
//        rotate1.setByAngle(-360);
//        rotate1.setCycleCount(Timeline.INDEFINITE);
//        rotate1.setDuration(Duration.millis(5000));
//        rotate1.setNode(innerCircle.getGroup());
//        rotate1.setInterpolator(Interpolator.LINEAR);
//
//        RotateTransition rotate2 = new RotateTransition();
//        rotate2.setAxis(Rotate.Z_AXIS);
//        rotate2.setFromAngle(0);
//        rotate2.setByAngle(360);
//        rotate2.setCycleCount(Timeline.INDEFINITE);
//        rotate2.setDuration(Duration.millis(5000));
//        rotate2.setNode(middleCircle.getGroup());
//        rotate2.setInterpolator(Interpolator.LINEAR);
//
//        RotateTransition rotate3 = new RotateTransition();
//        rotate3.setAxis(Rotate.Z_AXIS);
//        rotate3.setFromAngle(0);
//        rotate3.setByAngle(-360);
//        rotate3.setCycleCount(Timeline.INDEFINITE);
//        rotate3.setDuration(Duration.millis(5000));
//        rotate3.setNode(outerCircle.getGroup());
//        rotate3.setInterpolator(Interpolator.LINEAR);
//
//        rotate1.play();
//        rotate2.play();
//        rotate3.play();
