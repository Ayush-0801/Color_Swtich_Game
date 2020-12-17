import java.io.*;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.util.Duration;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.animation.*;
import java.time.LocalTime;
import java.util.*;

public class StartGame extends Application{

    private Pane holder;
    private Scene scene;
    private Scene beforecollision;
    private Scene previous;
    private boolean Up_Key_Pressed;
    private Player currentPlayer;

    private final Color blue=Color.rgb(65,228,243);
    private final Color purple=Color.rgb(147,33,252);
    private final Color yellow=Color.rgb(247,225,29);
    private final Color pink=Color.rgb(255,16,136);
    private final Color bg=Color.rgb(54,54,54);
    private Text score;
    private Text scoreAnimator;
    private Text highestScore;

    private ArrayList<Obstacle> ar;
    private ArrayList<Star> current_stars;
    private ArrayList<ColorSwitcher> current_cs;

    private double sidelength=0;
    private double radius=0;

    private Group scoreDisplay;
    private Group showHand;
    private Group displayTimer;
    private Group scoreAnimation;

    private Stage currentStage;
    private long counter=0;
    private double dist=430;
    private AnimationTimer t;
    private AnimationTimer supersonic;
    private AnimationTimer forceFieldTimer;
    private ArrayList<Group> superSpeed=new ArrayList<Group>();
    private ArrayList<Group> forceFieldObjects=new ArrayList<Group>();

    private double initial;

    public StartGame()
    {
        Up_Key_Pressed=false;
        ar=new ArrayList<Obstacle>();
        current_stars=new ArrayList<Star>();
        current_cs=new ArrayList<ColorSwitcher>();

        currentStage=new Stage();

        holder = new Pane();
        holder.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        scene = new Scene(holder, 800, 800);
        scene.setFill(bg);
    }

    public void comeToThisScene(Stage stage, Player curr) throws FileNotFoundException {
        currentPlayer=curr;
        currentStage=stage;
        currentPlayer.setAliveStatus(true);
        start(stage);
    }

    public void comeHereFromSave(Stage stage, SaveData sd) throws FileNotFoundException
    {
        currentStage=stage;
        currentPlayer=new Player();
        currentPlayer.setShiftInY(sd.getShiftHolderY());
        currentPlayer.setCenterX(sd.centerX());
        currentPlayer.setCenterY(sd.centerY());
        currentPlayer.getBall().setRadius(sd.getRadius());

        System.out.println(sd.colorCode());
        switch (sd.colorCode()) {
            case 1 :
                currentPlayer.getBall().setFill(blue);
                break;
            case 2 :
                currentPlayer.getBall().setFill(purple);
                break;
            case 3 :
                currentPlayer.getBall().setFill(yellow);
                System.out.println("Was here");
                break;
            case 4 :
                currentPlayer.getBall().setFill(pink);
                break;
        }
        currentPlayer.setScore(sd.getScore());
        currentPlayer.setNew_player(false);
        currentPlayer.setName(sd.getName());
        currentPlayer.setHighest_score(sd.getHighestScore());

        for(int i=0 ; i<sd.getTypes().size() ; i++)
        {
            String ob_type=sd.getTypes().get(i);

            if(ob_type.equals("SquareObs"))
            {
                sidelength=150;
                Obstacle sqobj=new SquareObs(sd.getXcoor().get(i),
                        sd.getYcoor().get(i),sidelength);

                setInitialOrientation(sd,i,sqobj);
                ar.add(sqobj);
                holder.getChildren().add(sqobj.getGroup());
            }

            else if(ob_type.equals("CircleObs")) {
                radius = 75;

                Obstacle cr = new CircleObs(sd.getXcoor().get(i),
                        sd.getYcoor().get(i), radius);
                ar.add(cr);
                setInitialOrientation(sd,i,cr);
                holder.getChildren().add(cr.getGroup());
            }

            else if(ob_type.equals("TriangleObs")) {
                sidelength = 200;

                Obstacle tr = new TriangleObs(sd.getXcoor().get(i),
                        sd.getYcoor().get(i),
                        sidelength, currentPlayer.getFill());
                ar.add(tr);
                setInitialOrientation(sd,i,tr);
                holder.getChildren().add(tr.getGroup());
            }

            else if(ob_type.equals("MultiCircle")) {

                radius = 75;
                Obstacle Circle_Left = new CircleObs(sd.getXcoor().get(i),
                        sd.getYcoor().get(i), radius);
                Circle_Left.setObstacleType("MultiCircle");
                Circle_Left.setRotationAC();
                i=i+1;

                Obstacle Circle_Right = new CircleObs(sd.getXcoor().get(i),
                        sd.getYcoor().get(i), radius);

                ((CircleObs) Circle_Right).setColor(purple, blue, pink, yellow);
                Circle_Right.setObstacleType("MultiCircle");
                Circle_Right.setRotationC();

                ar.add(Circle_Left);
                ar.add(Circle_Right);

                holder.getChildren().addAll(Circle_Left.getGroup(), Circle_Right.getGroup());
            }
            else if(ob_type.equals("PlusObs")) {
                sidelength = 100;
                Obstacle pl = new Plus(400,
                        sd.getYcoor().get(i),
                        sidelength);

                ar.add(pl);
                setInitialOrientation(sd,i,pl);
                holder.getChildren().add(pl.getGroup());
            }

            else if(ob_type.equals("LineObs")) {

                Obstacle ln = new LineObs(sd.getXcoor().get(i),
                        sd.getYcoor().get(i));
                ar.add(ln);
                holder.getChildren().add(ln.getGroup());
            }

            else if(ob_type.equals("DottedObs"))
            {
                sidelength=300;
                Obstacle up=new UniquePatterns(sd.getXcoor().get(i),sd.getYcoor().get(i),sidelength,sd.getObstacleUniqueRandValue().get(i));
                ar.add(up);
                holder.getChildren().add(up.getGroup());
            }
        }

        double csMaxY=0,stMaxY=0;
        for(int i=0; i<sd.getX().size() ; i++)
        {
            ColorSwitcher c=new ColorSwitcher(sd.getX().get(i),sd.getY().get(i),15);
            current_cs.add(c);
            holder.getChildren().add(c.getGroup());
            csMaxY=c.getY();
        }

        for(int i=0;i<sd.getSt_X().size();i++)
        {
            Star st= null;
            try {
                st = new Star(sd.getSt_X().get(i),sd.getSt_Y().get(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            current_stars.add(st);
            holder.getChildren().add(st.getGroup());
            stMaxY=st.getCenterY();
        }

        if(stMaxY>csMaxY)
        {
            counter=1;
        }
        else {
            counter=2;
        }

        start(stage);
        stage.setTitle("Current Game");
        stage.setScene(scene);
        stage.show();
    }

    public void setInitialOrientation(SaveData obs_list,int currentIndex, Obstacle currObj)
    {
        if(obs_list.getObstacleRotationVariable().get(currentIndex)!=0)
        {
            if(obs_list.getObstacleRotationVariable().get(currentIndex)==1)
            {
                currObj.setRotationC();
                if(currObj instanceof Plus)
                {
                    ((Plus) currObj).setShifter(2);
                }
            }

            else if(obs_list.getObstacleRotationVariable().get(currentIndex)==-1)
            {
                currObj.setRotationAC();
                if(currObj instanceof Plus)
                {
                    ((Plus) currObj).setShifter(1);
                }
            }
        }
    }

    private static MediaPlayer dead;
    private static MediaPlayer startGame;
    private static MediaPlayer superSonicSong;
    private static MediaPlayer forceFieldSong;

    @Override
    public void start(Stage stage) throws FileNotFoundException{

        Media startGamePlay=new Media(new File("Music/start.wav").toURI().toString());
        startGame=new MediaPlayer(startGamePlay);
        startGame.setVolume(0.04);
        startGame.play();

        score=new Text(50,55,Integer.toString(currentPlayer.getScore()));
        score.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 55));
        score.setFill(Color.WHITE);

        scoreDisplay=new Group();
        scoreDisplay.getChildren().add(score);

        Image hand=new Image(new FileInputStream("src/Icons/handOfTech.png"));
        ImageView handImage=new ImageView();
        handImage.setImage(hand);

        handImage.setX(385);
        handImage.setY(732+ currentPlayer.getShiftInY());
        handImage.setFitWidth(45);
        handImage.setPreserveRatio(true);

        Image img = new Image(new FileInputStream("src/Icons/Title.png"));
        ImageView titleimg= new ImageView();
        titleimg.setImage(img);
        titleimg.setX(270);
        titleimg.setY(550+ currentPlayer.getShiftInY());
        titleimg.setFitWidth(250);
        titleimg.setPreserveRatio(true);

        Obstacle titleCircleLeft=new CircleObs(355, 575 + currentPlayer.getShiftInY(), 23);
        Obstacle titleCircleRight=new CircleObs(445, 575 + currentPlayer.getShiftInY(), 23);
        titleCircleLeft.setRotationAC();
        titleCircleRight.setRotationC();
        ((CircleObs)titleCircleLeft).changeStroke(7);
        ((CircleObs)titleCircleRight).changeStroke(7);

        Group titleMenu=new Group();
        titleMenu.getChildren().addAll(titleimg ,
                titleCircleLeft.getGroup() , titleCircleRight.getGroup());
        showHand=new Group();
        showHand.getChildren().add(handImage);

        scoreAnimator=new Text(-50,-50,"+1");
        scoreAnimator.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 55));
        scoreAnimator.setFill(Color.WHITE);

        scoreAnimation=new Group();
        scoreAnimation.getChildren().add(scoreAnimator);

        holder.getChildren().add(scoreDisplay);
        holder.getChildren().add(scoreAnimation);
        holder.getChildren().add(showHand);
        holder.getChildren().add(titleMenu);
        holder.getChildren().add(currentPlayer.getGroup());

        //Bonus 1
        supersonic=new AnimationTimer() {

            private long timer=0;
            private Color obj;
            private Color sceneOld;
            private Group fallLines;
            private Line newObj;

            @Override
            public void handle(long l) {

                if(timer==0)
                {
                    Main.m1.stop();

                    superSonicSong=new MediaPlayer(new Media(new File("Music/supersonicSong.mp3").toURI().toString()));
                    superSonicSong.setCycleCount(-1);
                    superSonicSong.setVolume(0.15);
                    superSonicSong.play();

                    obj= (Color) currentPlayer.getBall().getFill();
                    sceneOld= (Color) scene.getFill();
                    scene.setFill(Color.BLACK);

                    Image fireTrail= null;
                    try {
                        fireTrail = new Image(new FileInputStream("src/Icons/Burn.gif"));
                    }

                    catch (FileNotFoundException e) {
                        System.out.println("Exception in trail");
                        e.printStackTrace();
                    }

                    ImageView fireTrailView=new ImageView();
                    fireTrailView.setRotate(180);
                    fireTrailView.setImage(fireTrail);
                    fireTrailView.setX(currentPlayer.getCenterX()-38.5);
                    fireTrailView.setY(currentPlayer.getCenterY()-10);
                    fireTrailView.setFitWidth(80);
                    fireTrailView.setPreserveRatio(true);

                    Glow ef=new Glow();
                    ef.setLevel(0.7);
                    fireTrailView.setEffect(ef);

                    ImageView jetPlane=new ImageView();
                    try {
                        jetPlane.setImage(new Image(new FileInputStream("src/Icons/plane.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    jetPlane.setX(currentPlayer.getCenterX()-38.5);
                    jetPlane.setY(currentPlayer.getCenterY()-67);
                    jetPlane.setFitWidth(80);
                    jetPlane.setPreserveRatio(true);
                    jetPlane.setEffect(ef);

                    RotateTransition rt = new RotateTransition();
                    rt.setAxis(Rotate.Y_AXIS);
                    rt.setFromAngle(0);
                    rt.setByAngle(180); //360
                    rt.setCycleCount(-1);
                    rt.setInterpolator(Interpolator.LINEAR);
                    rt.setDuration(Duration.millis(550)); // 650
                    rt.setNode(jetPlane);
                    rt.play();

                    currentPlayer.getGroup().getChildren().add(currentPlayer.getGroup().getChildren().size() , jetPlane);

                    fallLines=new Group();
                    holder.getChildren().add(fallLines);

                    newObj=new Line();
                    fallLines.getChildren().add(newObj);

                    currentPlayer.getGroup().getChildren().add(0,fireTrailView);
                    timer++;

                    currentPlayer.getBall().setOpacity(0);
                }

                newObj.setStartX((new Random()).nextInt(800));
                newObj.setStartY(0);
                newObj.setEndX(newObj.getStartX());
                newObj.setEndY(800);
                newObj.setStrokeWidth(7+(new Random()).nextInt(4));

                if(counter==2)
                    counter=0;

                switch((new Random()).nextInt(4) + 1)
                {
                    case 1:
                        newObj.setStroke(blue);
                        break;

                    case 2:
                        newObj.setStroke(purple);
                        break;

                    case 3:
                        newObj.setStroke(pink);
                        break;

                    case 4:
                        newObj.setStroke(yellow);
                        break;
                }
                currentPlayer.setShiftInY(currentPlayer.getShiftInY()+2.1);

                for(Obstacle el : ar) {
                    for (int i = 0; i < holder.getChildren().size(); i++) {
                        Group g = (Group) holder.getChildren().get(i);
                        if (g != currentPlayer.getGroup() && g != scoreDisplay && g!=fallLines && g!=scoreAnimation) {
                            g.setLayoutY(g.getLayoutY() + 3.1);
                        }
                    }
                }

                if(currentPlayer.getShiftInY()>(initial + 2*dist) && currentPlayer.getCenterY() <=
                        ar.get(1).getGroup().getBoundsInParent().getCenterY() - (dist)/2
                        && ar.get(1).getObstacleType().equals("MultiCircle"))
                {
                    currentPlayer.supersonicspeed=false;
                    currentPlayer.getBall().setFill(obj);
                    currentPlayer.getBall().setOpacity(1);
                    currentPlayer.getBall().setEffect(null);
                    currentPlayer.getGroup().getChildren().remove(0);
                    scene.setFill(sceneOld);
                    holder.getChildren().remove(holder.getChildren().indexOf(fallLines));
                    currentPlayer.getGroup().getChildren().remove(currentPlayer.getGroup().getChildren().size()-1);
                    this.stop();
                    timer=0;
                    superSonicSong.stop();
                    Main.m1.play();
                }
            }
        };
        
        Media die=new Media(new File("Music/dead.wav").toURI().toString());
        t=new AnimationTimer() {
            final double gravity=0.55;
            final double time=0.55;

            @Override
            public void handle(long now)
            {
                beforecollision=stage.getScene();

                if(currentPlayer.supersonicspeed==false) {
                    try {
                        JumpBall(currentPlayer.getcurrSpeed(), gravity, time);
                    } catch (LargeBallRadiusException e) {
                        e.printStackTrace();
                    }
                }

                if(!currentPlayer.getAliveStatus()) {

                    dead=new MediaPlayer(die);
                    dead.setVolume(0.04);
                    dead.play();

                    this.stop();
                    try {
                        if(currentPlayer.getScore()<2)
                        {
                            GameOver(stage);
                        }
                        else
                        {
                            Resurrect();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    insertObjects();
                }

                catch (FileNotFoundException | InsufficientEntityException e) {
                    e.printStackTrace();
                }

                checkSuperSonic();
                checkForceField();

                if(currentPlayer.supersonicspeed==false && currentPlayer.forcefield==false) {
                    CollisionCheck();
                }

                ColorSwitcherCheck();
                try {
                    UpdateScore();
                } catch (InsufficientStarsException e) {
                    e.printStackTrace();
                }
                removeObstaclesOutOfScreen();
            }
        };

        if(currentPlayer.getNew_player())
            t.start();

        EventHandler<KeyEvent> eventKey=new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e)
            {
                if(e.getCode()==KeyCode.W)
                {
                    currentPlayer.setCenterY(currentPlayer.getCenterY()-10);
                    if(currentPlayer.getCenterY()<400)
                    {
                        holder.setLayoutY(holder.getLayoutY()+3);
                    }
                }

                else if(e.getCode()==KeyCode.S)
                    currentPlayer.setCenterY(currentPlayer.getCenterY()+1);

                else if(e.getCode()==KeyCode.UP && currentPlayer.getAliveStatus())
                {
                    Up_Key_Pressed=true;
                    t.start();
                }

                else if(e.getCode()==KeyCode.DOWN)
                    currentPlayer.setCenterY(currentPlayer.getCenterY()+25);

                else if(e.getCode()==KeyCode.A)
                    currentPlayer.setCenterX(currentPlayer.getCenterX()-1);

                else if(e.getCode()==KeyCode.D)
                    currentPlayer.setCenterX(currentPlayer.getCenterX()+1);

                else if(e.getCode()==KeyCode.RIGHT)
                    currentPlayer.setCenterX(currentPlayer.getCenterX()+25);

                else if(e.getCode()==KeyCode.LEFT)
                    currentPlayer.setCenterX(currentPlayer.getCenterX()-25);

                else if(e.getCode()==KeyCode.R)
                {
                    t.stop();
                    try {
                        StartGame newGame=new StartGame();
                        currentPlayer=new Player();
                        newGame.comeToThisScene(stage,currentPlayer);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }

                else if(e.getCode()==KeyCode.P)
                {
                    t.stop();
                    try {
                        Scene current=stage.getScene();
                        PauseMenuFunction(stage);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
            }
        };

        scene.setFill(bg);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, eventKey);
        stage.setTitle("Current Game");
        stage.setScene(scene);
        stage.show();
    }

    public void checkSuperSonic()
    {
        if( superSpeed.isEmpty()==false && currentPlayer.getBall().intersects(superSpeed.get(0).getBoundsInParent()))
        {
            int index=holder.getChildren().indexOf(superSpeed.get(0));
            superSpeed.remove(0);
            holder.getChildren().remove(index);

            if(!currentPlayer.supersonicspeed)
                supersonic.start();

            currentPlayer.supersonicspeed=true;
        }
    }

    public void insertObjects() throws FileNotFoundException,InsufficientEntityException{

        dist=430;

        while(ar.size()<7)
        {
            Random ob=new Random();

            if(ar.isEmpty()) {

                int value;
                value=ob.nextInt(3)+1;

                switch (value) {

                    case 1:

                        sidelength = 150;
                        Obstacle sq = new SquareObs(400,
                                currentPlayer.getCenterY() - dist, sidelength);
                        ((SquareObs) sq).setTransition();
                        ar.add(sq);
                        holder.getChildren().add(sq.getGroup());
                        break;

                    case 2:

                        radius=75;
                        Obstacle cr=new CircleObs(400,
                                currentPlayer.getCenterY() - dist, radius);
                        ar.add(cr);
                        ((CircleObs) cr).setTransition();
                        holder.getChildren().add(cr.getGroup());
                        break;

                    case 3:

                        sidelength=200;
                        Obstacle tr=new TriangleObs ( 400,
                                currentPlayer.getCenterY() - dist,
                                sidelength, currentPlayer.getFill());
                        ((TriangleObs) tr).setTransition();
                        ar.add(tr);
                        holder.getChildren().add(tr.getGroup());
                        break;
                }
            }

            else
            {
                int calc=ar.size()-1;
                int value;

                if(currentPlayer.getScore()<1)
                {
                    value=ob.nextInt(3)+1;

                    if(current_cs.isEmpty()==false)
                    {
                        while(value==3)
                            value=ob.nextInt(3)+1;
                    }
                }

                else if(currentPlayer.getScore()<2)
                {
                    value=ob.nextInt(6)+1;

                    if(current_cs.isEmpty()==false)
                    {
                        while(value==3)
                            value=ob.nextInt(6)+1;
                    }
                }

                else
                {
                    value=ob.nextInt(7)+1;

                    if(current_cs.isEmpty()==false)
                    {
                        while(value==3)
                            value=ob.nextInt(7)+1;
                    }
                }

                switch (value) {
                    case 1:

                        sidelength = 150;
                        Obstacle sq = new SquareObs(400,
                                ar.get(calc).getGroup().getBoundsInParent().getCenterY() - dist,
                                sidelength);
                        ((SquareObs) sq).setTransition();
                        ar.add(sq);
                        holder.getChildren().add(sq.getGroup());
                        break;

                    case 2:

                        radius=75;
                        Group last=ar.get(calc).getGroup();
                        Obstacle cr=new CircleObs(400 ,
                                last.getBoundsInParent().getCenterY() - dist, radius);
                        ar.add(cr);
                        ((CircleObs) cr).setTransition();
                        holder.getChildren().add(cr.getGroup());
                        break;

                    case 3:

                        sidelength=200;
                        last=ar.get(calc).getGroup();
                        Obstacle tr=new TriangleObs ( 400,
                                last.getBoundsInParent().getCenterY() - dist,
                                sidelength, currentPlayer.getFill());
                        ((TriangleObs) tr).setTransition();
                        ar.add(tr);
                        holder.getChildren().add(tr.getGroup());
                        break;

                    case 4:

                        radius=75;
                        last=ar.get(calc).getGroup();
                        Obstacle Circle_Left=new CircleObs(400 - radius - 7.5,
                                last.getBoundsInParent().getCenterY() - dist , radius);

                        Circle_Left.setObstacleType("MultiCircle");
                        Circle_Left.setRotationAC();

                        Obstacle Circle_Right=new CircleObs(400 + radius + 7.5,
                                last.getBoundsInParent().getCenterY() - dist , radius);
                        Circle_Right.setObstacleType("MultiCircle");

                        ((CircleObs) Circle_Right).setColor(purple,blue,pink,yellow);
                        Circle_Right.setRotationC();

                        ar.add(Circle_Left);
                        ar.add(Circle_Right);

                        holder.getChildren().addAll(Circle_Left.getGroup(), Circle_Right.getGroup());
                        break;

                    case 5:

                        sidelength=100;
                        last=ar.get(calc).getGroup();
                        Obstacle pl=new Plus ( 400,
                                last.getBoundsInParent().getCenterY() - dist,
                                sidelength);
                        ((Plus) pl).setTransition();
                        ar.add(pl);
                        holder.getChildren().add(pl.getGroup());
                        break;

                    case 6:

                        last=ar.get(calc).getGroup();
                        Obstacle a=new LineObs( 400 ,
                                last.getBoundsInParent().getCenterY() - dist);
                        ar.add(a);
                        holder.getChildren().add(a.getGroup());
                        break;

                    case 7:

                        sidelength=300;
                        last=ar.get(calc).getGroup();
                        Obstacle dc=new UniquePatterns(400, last.getBoundsInParent().getCenterY() - dist, sidelength,-1);
                        ar.add(dc);
                        holder.getChildren().add(dc.getGroup());
                        break;
                }
            }

            counter+=1;

            if(counter>=10)
            {
                Group last=ar.get(ar.size()-1).getGroup();

                int random=new Random().nextInt(2)+1;

                if(currentPlayer.forcefield)
                    random=2;

                if(currentPlayer.supersonicspeed)
                    random=1;

                if(random==1)
                {
                    Group supersonicObj = new Group();
                    Image flashImage = new Image(new FileInputStream("src/Icons/illustrate.png"));
                    ImageView flash = new ImageView();

                    flash.setImage(flashImage);
                    flash.setX(365);
                    flash.setY((last.getBoundsInParent().getCenterY()) - dist / 2);
                    flash.setFitWidth(62);
                    flash.setPreserveRatio(true);

                    Glow ef = new Glow();
                    ef.setLevel(0.7);
                    flash.setEffect(ef);

                    RotateTransition rt = new RotateTransition();
                    rt.setAxis(Rotate.Y_AXIS);
                    rt.setFromAngle(0);
                    rt.setByAngle(360);
                    rt.setCycleCount(-1);
                    rt.setInterpolator(Interpolator.LINEAR);
                    rt.setDuration(Duration.millis(650));
                    rt.setNode(flash);
                    rt.play();

                    supersonicObj.getChildren().add(flash);
                    superSpeed.add(supersonicObj);
                    holder.getChildren().add(supersonicObj);
                }

                else {

                    Group forceFieldCreate=new Group();
                    Image ForceFieldSymbol=new Image(new FileInputStream("src/Icons/shieldCreate.png"));
                    ImageView ShowShield=new ImageView();
                    ShowShield.setImage(ForceFieldSymbol);
                    ShowShield.setX(365);
                    ShowShield.setY((last.getBoundsInParent().getCenterY()) - dist / 2);
                    ShowShield.setFitWidth(62);
                    ShowShield.setPreserveRatio(true);

                    Glow ef = new Glow();
                    ef.setLevel(0.7);
                    ShowShield.setEffect(ef);

                    RotateTransition rt = new RotateTransition();
                    rt.setAxis(Rotate.Y_AXIS);
                    rt.setFromAngle(0);
                    rt.setByAngle(360);
                    rt.setCycleCount(-1);
                    rt.setInterpolator(Interpolator.LINEAR);
                    rt.setDuration(Duration.millis(650));
                    rt.setNode(ShowShield);
                    rt.play();

                    forceFieldCreate.getChildren().add(ShowShield);
                    forceFieldObjects.add(forceFieldCreate);
                    holder.getChildren().add(forceFieldCreate);
                }
                counter=0;
            }

            else if(counter%2==0)
            {
                Group last=ar.get(ar.size()-1).getGroup();
                Star st= null;

                try {
                    st = new Star(387.5,(last.getBoundsInParent().getCenterY())-dist/2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                current_stars.add(st);
                holder.getChildren().add(st.getGroup());
            }

            else if(counter%2==1)
            {
                Group last=ar.get(ar.size()-1).getGroup();
                ColorSwitcher c=new ColorSwitcher(400,last.getBoundsInParent().getCenterY()-dist/2,15);
                current_cs.add(c);
                holder.getChildren().add(c.getGroup());
            }
        }

        if(ar.size()<3)
        {
            throw new InsufficientEntityException("More entities needed on screen");
        }
    }

    public void CollisionCheck()
    {
        for (Obstacle obs : ar)
        {
            if((obs instanceof CircleObs)==false && (obs instanceof UniquePatterns)==false)
            {
                Group group=obs.getGroup();
                if (currentPlayer.getBall().intersects(group.getBoundsInParent()))
                {
                    for (int j = 0; j < group.getChildren().size(); j++) {
                        Shape s1 = (Shape) group.getChildren().get(j);
                        Shape inter = Shape.intersect(s1, currentPlayer.getBall());
                        if (inter.getBoundsInParent().getWidth() != (double) (-1.0)) {
                            if (!(s1.getStroke().equals(currentPlayer.getFill()))) {
                                currentPlayer.setAliveStatus(false);
                                break;
                            }
                        }
                    }
                }
            }

            else if ((obs instanceof CircleObs))
            {
                Group group=obs.getGroup();
                if (currentPlayer.getBall().intersects(group.getBoundsInParent()))
                {
                    for (int j = 0; j < group.getChildren().size(); j++) {
                        Shape s1 = (Shape) group.getChildren().get(j);
                        Shape inter = Shape.intersect(currentPlayer.getBall(), s1);
                        Shape inter2 = Shape.intersect(currentPlayer.getBall(), ((CircleObs) obs).getBoundaryCircle());
                        if (inter.getBoundsInParent().getWidth() != (double) (-1.0) &&
                                inter2.getBoundsInParent().getWidth() == -1.0) {
                            if (!s1.getStroke().equals(currentPlayer.getFill())) {
                                currentPlayer.setAliveStatus(false);
                                break;
                            }
                        }
                    }
                }
            }

            else if(obs instanceof UniquePatterns)
            {
                Group group=obs.getGroup();
                if (currentPlayer.getBall().intersects(group.getBoundsInParent()))
                {
                    for (int j = 0; j < group.getChildren().size(); j++) {
                        Shape s1 = (Shape) group.getChildren().get(j);
                        Shape inter = Shape.intersect(s1, currentPlayer.getBall());
                        if (inter.getBoundsInParent().getWidth() != (double) (-1.0)) {
                            if (!(s1.getFill().equals(currentPlayer.getFill()))) {
                                currentPlayer.setAliveStatus(false);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeObstaclesOutOfScreen()
    {
        if(ar.isEmpty()==false && ar.get(0).getGroup().getBoundsInParent().getMinY()>800)
        {
            ar.remove(0);
        }
    }

    private static MediaPlayer star;

    public void ShiftScreenDown()
    {
        for(int i=0;i<holder.getChildren().size();i++) {
            Group g=(Group)holder.getChildren().get(i);
            if(g!=currentPlayer.getGroup() && g!=scoreDisplay && g!=scoreAnimation) {
                TranslateTransition tr = new TranslateTransition();
                tr.setNode(g);

                tr.setDuration(Duration.millis(225));
                tr.setByY(74.78);//94.78
                tr.play();
                currentPlayer.setShiftInY(currentPlayer.getShiftInY()+74.78);
            }
        }
    }


    private static MediaPlayer clrswtch;
    
    
    private MediaPlayer clickbutton;

    public void deserializer(){

        try {
            FileInputStream fs_sd = new FileInputStream("Saves/FullData.ser");
            ObjectInputStream ob_sd = new ObjectInputStream(fs_sd);

            while(true) {

                try {
                    SaveOrLoadGame.saveData=(ArrayList<SaveData>)ob_sd.readObject();
                }
                catch(IOException e)
                {
                    break;
                }
                catch (ClassNotFoundException e) {
                    break;
                }

            }
            ob_sd.close();
            fs_sd.close();

        }

        catch (FileNotFoundException e) {
        }

        catch (IOException e) {
            ;
        }
    }

    public void GameOver(Stage stage) throws Exception {

        deserializer();
        boolean flag=false;

        for (int i = 0; i < SaveOrLoadGame.saveData.size(); i++) {

            if (SaveOrLoadGame.saveData.get(i).getName().equals(currentPlayer.getName())) {
                flag = true;
            }
        }

        String filenameP="Saves/Players_List.ser";
        String filenameO="Saves/Obstacles_List.ser";
        String filenameCS="Saves/ColorSwitchers.ser";
        String filenameSt="Saves/Stars.ser";
        String filenameSd="Saves/FullData.ser";

        try
        {
            FileOutputStream file_sd=new FileOutputStream(filenameSd);
            ObjectOutputStream out_sd=new ObjectOutputStream(file_sd);
            out_sd.writeObject(SaveOrLoadGame.saveData);
            out_sd.close();
            file_sd.close();
        }

        catch (IOException e) {
            System.out.println("Game Save: Unsuccessfull");
        }


        Pane holder = new Pane();
        holder.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        Scene scene = new Scene(holder, 800, 800);
        scene.setFill(bg);

        Image img = new Image(new FileInputStream("src/Icons/Title.png"));
        ImageView titleimg= new ImageView(img);
        titleimg.setImage(img);
        titleimg.setX(270);
        titleimg.setY(30);
        titleimg.setFitWidth(250);
        titleimg.setPreserveRatio(true);

        Image img1 = new Image(new FileInputStream("src/Icons/Back.png"));
        ImageView backimg = new ImageView(img1);
        backimg.setFitHeight(70);
        backimg.setPreserveRatio(true);

        FileInputStream input2 = new FileInputStream("src/Icons/RestartButton.png");
        Image img2 = new Image(input2);
        ImageView restartimg = new ImageView(img2);
        restartimg.setFitHeight(150);
        restartimg.setPreserveRatio(true);

        int radiustitle=23;
        Obstacle titleCircleLeft=new CircleObs(355,55,radiustitle);
        Obstacle titleCircleRight=new CircleObs(445,55,radiustitle);
        ((CircleObs) titleCircleLeft).changeStroke(7);
        ((CircleObs) titleCircleRight).changeStroke(7);

        Rectangle score = new Rectangle();
        score.setX(0);
        score.setY(200);
        score.setWidth(800);
        score.setHeight(60);
        score.setFill(Color.rgb(65,65,65));

        Rectangle bestscore = new Rectangle();
        bestscore.setX(0);
        bestscore.setY(340);
        bestscore.setWidth(800);
        bestscore.setHeight(60);
        bestscore.setFill(Color.rgb(243,151,52,0.95));

        Text scoretxt = new Text (315, 248, ("SCORE"));
        scoretxt.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 55));
        scoretxt.setFill(Color.WHITE);

        Text scoreVal = new Text (375, 318, (""+currentPlayer.getScore()));
        scoreVal.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 55));
        scoreVal.setFill(Color.WHITE);

        Text bestscoretxt = new Text (264, 388, "BEST SCORE");
        bestscoretxt.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 55));
        bestscoretxt.setFill(Color.WHITE);

        highestScore= new Text (375, 458, (""+currentPlayer.getHighest_score()));
        highestScore.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 55));
        highestScore.setFill(Color.WHITE);

        Group banner=new Group();
        banner.getChildren().addAll(score,bestscore,scoretxt,bestscoretxt);

        Button backbutton=new Button();
        backbutton.setTranslateX(50);
        backbutton.setTranslateY(30);
        backbutton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 70px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-max-width: 70px; " +
                        "-fx-max-height: 70px;" +
                        "-fx-background-color: #555555"
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

        Button restartbutton=new Button();
        restartbutton.setTranslateX(320);
        restartbutton.setTranslateY(550);
        restartbutton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 150px; " +
                        "-fx-max-width: 150px; " +
                        "-fx-max-height: 150px;" +
                        "-fx-background-color: #555555"
        );
        restartbutton.setGraphic(restartimg);
        restartbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {

                    clickbutton=new MediaPlayer(new Media(new File("Music/button.wav").toURI().toString()));
                    clickbutton.setVolume(0.04);
                    clickbutton.play();

                    currentPlayer.setScore(0);
                    currentPlayer.setCenterY(710);
                    currentPlayer.setNew_player(true);
                    currentPlayer.setShiftInY(0);
                    currentPlayer.getBall().setVisible(true);
                    StartGame newGame=new StartGame();
                    newGame.comeToThisScene(stage,currentPlayer);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        RotateTransition rotate1 = new RotateTransition();
        rotate1.setAxis(Rotate.Z_AXIS);
        rotate1.setFromAngle(0);
        rotate1.setByAngle(-360);
        rotate1.setCycleCount(Timeline.INDEFINITE);
        rotate1.setDuration(Duration.millis(5000));
        rotate1.setNode(titleCircleLeft.getGroup());
        rotate1.setInterpolator(Interpolator.LINEAR);

        RotateTransition rotate2 = new RotateTransition();
        rotate2.setAxis(Rotate.Z_AXIS);
        rotate2.setFromAngle(0);
        rotate2.setByAngle(360);
        rotate2.setCycleCount(Timeline.INDEFINITE);
        rotate2.setDuration(Duration.millis(5000));
        rotate2.setNode(titleCircleRight.getGroup());
        rotate2.setInterpolator(Interpolator.LINEAR);

        rotate1.play();
        rotate2.play();

        holder.getChildren().addAll(titleimg,highestScore,titleCircleLeft.getGroup(),scoreVal,titleCircleRight.getGroup(),backbutton,banner,restartbutton);
        stage.setTitle("GameOver Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void Resurrect() throws FileNotFoundException {

        currentPlayer.getBall().setVisible(false);

        AnimationTimer screenflash=new AnimationTimer(){

            Text res_count;
            private int timer=0;
            private LocalTime before;
            private LocalTime after;
            private ArrayList<Integer> tm=new ArrayList<Integer>();
            @Override
            public void handle(long l) {

                if(timer==0)
                {
                    currentStage.getScene().setFill(Color.TRANSPARENT);
                    holder.setVisible(false);
                    before=LocalTime.now();
                    displayTimer=new Group();
                    res_count = new Text(375 , 442, "Hello");
                    res_count.setFont(Font.loadFont("file:resources/fonts/BlissfulThinking.otf", 55));
                    res_count.setFill(Color.WHITE);
                    displayTimer.getChildren().add(res_count);

                    tm.add(0);
                    tm.add(1);
                    tm.add(2);
                    tm.add(3);
                    tm.add(4);
                    tm.add(5);
                    tm.add(6);
                }
                timer++;

                if(timer==10)
                {
                    currentStage.getScene().setFill(bg);
                    holder.setVisible(true);
                    holder.getChildren().add(displayTimer);
                }

                after=LocalTime.now();
                if(after.getSecond() - before.getSecond() >=5)
                {
                    this.stop();
                    try {
                        GameOver(currentStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(tm.contains(after.getSecond()-before.getSecond()))
                {
                    res_count.setText(Integer.toString(5-after.getSecond()+before.getSecond()));
                }
            }
        };
        screenflash.start();

        Circle[] ar_balls =new Circle[600];
        TranslateTransition[] tr=new TranslateTransition[600];
        FadeTransition[] f = new FadeTransition[600];

        Group children=new Group();
        for(int w=0;w<ar_balls.length;w++)
        {
            Random a=new Random();
            ar_balls[w]=new Circle(currentPlayer.getCenterX(),currentPlayer.getCenterY(),2.5);
            int check=a.nextInt(4)+1;
            switch (check) {
                case 1 : ar_balls[w].setFill(blue);
                    break;
                case 2 : ar_balls[w].setFill(purple);
                    break;
                case 3 : ar_balls[w].setFill(pink);
                    break;
                case 4 : ar_balls[w].setFill(yellow);
                    break;
            }

            children.getChildren().add(ar_balls[w]);

            Random ob = new Random();
            tr[w]=new TranslateTransition();
            tr[w].setDuration(Duration.millis(1000));
            tr[w].setNode(ar_balls[w]);
            tr[w].setByX(-1000 + ob.nextInt(2000));
            tr[w].setByY(-1000 + ob.nextInt(2000));
            tr[w].play();

            f[w]=new FadeTransition();
            f[w].setDuration(Duration.millis(2000));
            f[w].setFromValue(100);
            f[w].setToValue(0);
            f[w].setAutoReverse(false);
            f[w].setCycleCount(1);
            f[w].setNode(ar_balls[w]);
            f[w].play();
        }

        Group resurrect=new Group();
        Image img4 = new Image( new FileInputStream("src/Icons/resurrect.png"));
        ImageView titleimg4= new ImageView();
        titleimg4.setImage(img4);
        titleimg4.setX(250);
        titleimg4.setY(200);
        titleimg4.setFitWidth(300);
        titleimg4.setPreserveRatio(true);

        Button replay=new Button();
        replay.setTranslateX(260);
        replay.setTranslateY(330);
        replay.setStyle(
                "-fx-background-radius: 0em; " +
                        "-fx-min-width: 120px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-max-width: 120px; " +
                        "-fx-max-height: 70px;" +
                        "-fx-background-color: transparent"
        );
        replay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                screenflash.stop();

                try {
                    currentPlayer.setScore(0);
                    currentPlayer.setCenterY(710);
                    currentPlayer.setNew_player(true);  // Restarting Game for current Player
                    currentPlayer.setShiftInY(0);
                    currentPlayer.getBall().setVisible(true);
                    StartGame newGame=new StartGame();
                    newGame.comeToThisScene(currentStage,currentPlayer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button givestar=new Button();
        givestar.setTranslateX(400);
        givestar.setTranslateY(330);
        givestar.setStyle(
                "-fx-background-radius: 0em; " +
                        "-fx-min-width: 120px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-max-width: 120px; " +
                        "-fx-max-height: 70px;" +
                        "-fx-background-color: transparent"
        );
        givestar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                screenflash.stop();
                try {
                    if(currentPlayer.getScore()>=1)
                    {
                        currentPlayer.setScore(currentPlayer.getScore()-1);
                        score.setText(Integer.toString(currentPlayer.getScore()));
                        holder.getChildren().remove(holder.getChildren().indexOf(displayTimer));
                        holder.getChildren().remove(holder.getChildren().indexOf(resurrect));
                        currentPlayer.setAliveStatus(true);
                        currentStage.setScene(beforecollision);
                        currentPlayer.getBall().setVisible(true);
                        double setCoor=740;
                        CollisionCheck();
                        while(currentPlayer.getAliveStatus()==false)
                        {
                            currentPlayer.setCenterY(setCoor);
                            setCoor++;
                            currentPlayer.setAliveStatus(true);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        resurrect.getChildren().addAll(titleimg4,replay,givestar);
        holder.getChildren().add(children);
        holder.getChildren().add(resurrect);
    }
}

class InsufficientEntityException extends Exception{

    public InsufficientEntityException(String message)
    {
        super(message);
    }
}

class LargeBallRadiusException extends Exception{

    public LargeBallRadiusException(String message)
    {
        super(message);
    }
}

class InsufficientStarsException extends Exception{

    public InsufficientStarsException(String message)
    {
        super(message);
    }
}
