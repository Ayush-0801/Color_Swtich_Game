import java.io.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
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

    public static MediaPlayer m1;
    private static MediaPlayer button;

    private Color blue=Color.rgb(65,228,243);
    private Color purple=Color.rgb(147,33,252);
    private Color yellow=Color.rgb(247,225,29);
    private Color pink=Color.rgb(255,16,136);

    @Override
    public void start(Stage  stage) throws Exception
    {
        holder=new Pane();
        scene = new Scene(holder, 800, 800);

        String path="Music/gameplay.mp3";

        if(m1==null) {
            Media m = new Media(new File(path).toURI().toString());
            m1 = new MediaPlayer(m);
            m1.setVolume(0.04);
            m1.setAutoPlay(true);
            m1.setCycleCount(-1);
        }

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

        FileInputStream input4= new FileInputStream("src/Icons/Instructions.png");
        Image img4 = new Image(input4);
        ImageView instructionimg = new ImageView(img4);
        instructionimg.setFitHeight(60);
        instructionimg.setPreserveRatio(true);


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

                    button=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                    button.setVolume(0.04);
                    button.play();

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

                    button=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                    button.setVolume(0.04);
                    button.play();

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

                button=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                button.setVolume(0.04);
                button.play();

                stage.close();
            }
        });

        Button instructionbutton=new Button();
        instructionbutton.setTranslateX(720);
        instructionbutton.setTranslateY(55);
        instructionbutton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 10px; " +
                        "-fx-min-height: 10px; " +
                        "-fx-max-width: 10px; " +
                        "-fx-max-height: 10px;" +
                        "-fx-background-color: #555555"
        );
        instructionbutton.setGraphic(instructionimg);
        instructionbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {

                    button=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                    button.setVolume(0.04);
                    button.play();

                    instructionsMenu(stage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        displayGameName();

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
                ,playbutton,savegamesbutton,exitbutton,instructionbutton);

        holder.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        scene.setFill(bg);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void displayGameName() throws FileNotFoundException {

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

    public void instructionsMenu(Stage stage) throws FileNotFoundException
    {
        Pane n=new Pane();
        Scene scene = new Scene(n, 800, 800);

        FileInputStream input1 = new FileInputStream("src/Icons/Back.png");
        Image img1 = new Image(input1);
        ImageView backimg = new ImageView(img1);
        backimg.setFitHeight(70);
        backimg.setPreserveRatio(true);

        Button backbutton=new Button();
        backbutton.setTranslateX(25);
        backbutton.setTranslateY(20);
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
                try {
                    button=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                    button.setVolume(0.04);
                    button.play();

                    comeBackHome(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Text instructiontitle = new Text (183, 100, "INSTRUCTIONS");
        instructiontitle.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 75));
        instructiontitle.setFill(Color.rgb(156,226,206));

        Group sgss=new Group();

        Circle ball = new Circle(185, 160, 15);
        ball.setFill(purple);

        ColorSwitcher colorswitcher=new ColorSwitcher(185,345,15);


        FileInputStream input2 = new FileInputStream("src/Icons/star.png");
        Image img2 = new Image(input2);
        ImageView starimg = new ImageView(img2);
        starimg.setFitHeight(27);
        starimg.setPreserveRatio(true);
        starimg.setTranslateX(172);
        starimg.setTranslateY(235);

        FileInputStream input3 = new FileInputStream("src/Icons/illustrate.png");
        Image img3 = new Image(input3);
        ImageView illimg = new ImageView(img3);
        illimg.setFitHeight(62);
        illimg.setPreserveRatio(true);
        illimg.setTranslateX(155);
        illimg.setTranslateY(425);

        FileInputStream input4 = new FileInputStream("src/Icons/shieldCreate.png");
        Image img4 = new Image(input4);
        ImageView shieldimg = new ImageView(img4);
        shieldimg.setFitHeight(62);
        shieldimg.setPreserveRatio(true);
        shieldimg.setTranslateX(155);
        shieldimg.setTranslateY(540);

        Text balltext = new Text (235, 155, "It indicates the ball color of the player \nto pass the obstacles");
        balltext.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 25));
        balltext.setFill(purple);

        Text startext = new Text (235, 245, "Collect these stars and boost your \nhigh score");
        startext.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 25));
        startext.setFill(Color.WHITE);

        Text colorswitchertext = new Text (235, 340, "It switches the color randomly of the \nplayer ball");
        colorswitchertext.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 25));
        colorswitchertext.setFill(pink);

        Text bonus1text = new Text (235, 430, "Bonus 1\nIt boosts the ball with supersonic \nspeed for a certain time");
        bonus1text.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 25));
        bonus1text.setFill(yellow);

        Text bonus2text = new Text (235, 550, "Bonus 2\nIt activates a force field that protects \nthe ball for 10 seconds from oncoming \nobstacles");
        bonus2text.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 25));
        bonus2text.setFill(Color.rgb(76,186,130));

        sgss.getChildren().addAll(ball,starimg,colorswitcher.getGroup(),illimg,shieldimg,balltext,startext,colorswitchertext,bonus1text,bonus2text);

//Effects
        ScaleTransition sc=new ScaleTransition();
        sc.setByX(0.3);
        sc.setByY(0.3);
        sc.setCycleCount(Integer.MAX_VALUE);
        sc.setAutoReverse(true);
        sc.setDuration(Duration.millis(600));
        sc.setNode(starimg);
        sc.play();

        RotateTransition r1 = new RotateTransition();
        r1.setFromAngle(0);
        r1.setByAngle(360);
        r1.setCycleCount(-1);
        r1.setDuration(Duration.millis(3000));
        r1.setNode(colorswitcher.getGroup());
        r1.setInterpolator(Interpolator.LINEAR);
        r1.play();

        Glow ef1 = new Glow();
        ef1.setLevel(0.7);
        illimg.setEffect(ef1);

        RotateTransition rt1 = new RotateTransition();
        rt1.setAxis(Rotate.Y_AXIS);
        rt1.setFromAngle(0);
        rt1.setByAngle(360);
        rt1.setCycleCount(-1);
        rt1.setInterpolator(Interpolator.LINEAR);
        rt1.setDuration(Duration.millis(650));
        rt1.setNode(illimg);
        rt1.play();

        Glow ef2 = new Glow();
        ef2.setLevel(0.7);
        shieldimg.setEffect(ef2);

        RotateTransition rt2 = new RotateTransition();
        rt2.setAxis(Rotate.Y_AXIS);
        rt2.setFromAngle(0);
        rt2.setByAngle(360);
        rt2.setCycleCount(-1);
        rt2.setInterpolator(Interpolator.LINEAR);
        rt2.setDuration(Duration.millis(650));
        rt2.setNode(shieldimg);
        rt2.play();
//

        Text creators = new Text (150, 725, "Created By : Ayush Misra & Vinay Pandey");
        creators.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 30));
        creators.setFill(Color.WHITE);

        Text rollno1 = new Text (335, 755, "(2019301)");
        rollno1.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 25));
        rollno1.setFill(Color.WHITE);

        Text rollno2 = new Text (520, 755, "(2019288)");
        rollno2.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 25));
        rollno2.setFill(Color.WHITE);

        Group createdby=new Group();
        createdby.getChildren().addAll(creators,rollno1,rollno2);

        n.getChildren().addAll(backbutton,instructiontitle,sgss,createdby);

        n.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        scene.setFill(bg);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void comeBackHome(Stage stage) throws Exception {
        start(stage);
    }
}
