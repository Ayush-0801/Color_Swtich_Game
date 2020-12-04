import java.io.*;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
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
    private boolean Up_Key_Pressed=false;
    private Player currentPlayer;

    private Color blue=Color.rgb(65,228,243);
    private Color purple=Color.rgb(147,33,252);
    private Color yellow=Color.rgb(247,225,29);
    private Color pink=Color.rgb(255,16,136);
    private Color bg=Color.rgb(54,54,54);
    private Text score;
    private Text highestScore;

    private ArrayList<Obstacle> ar;
    private ArrayList<Star> current_stars;
    private ArrayList<ColorSwitcher> current_cs;

    private double sidelength=0;
    private double radius=0;
    private Group scoreDisplay;
    private Group showHand;
    private Stage currentStage;
    private long counter=0;
    private double dist=430;
    private AnimationTimer t;
    private AnimationTimer supersonic;
    private AnimationTimer forceFieldTimer;
    private ArrayList<Group> superSpeed=new ArrayList<Group>();
    private ArrayList<Group> forceFieldObjects=new ArrayList<Group>();

    private Group displayTimer;
    private double initial; // forsupersonic

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

        switch (sd.colorCode()) {
            case 1 :
                currentPlayer.getBall().setFill(blue);
                break;
            case 2 :
                currentPlayer.getBall().setFill(purple);
                break;
            case 3 :
                currentPlayer.getBall().setFill(yellow);
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
                sidelength = 150;

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
                Obstacle up=new UniquePatterns(sd.getXcoor().get(i),sd.getYcoor().get(i),sidelength);
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

    @Override
    public void start(Stage stage) throws FileNotFoundException{

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

        holder.getChildren().add(scoreDisplay);
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

                    fallLines=new Group();
                    holder.getChildren().add(fallLines);
                    newObj=new Line();
                    fallLines.getChildren().add(newObj);
                    currentPlayer.getGroup().getChildren().add(0,fireTrailView);
                    timer++;
                }

                currentPlayer.setFill(Color.DARKORANGE);
                newObj.setStartX((new Random()).nextInt(800));
                newObj.setStartY(0);
                newObj.setEndX(newObj.getStartX());
                newObj.setEndY(800);
                newObj.setStrokeWidth(7+(new Random()).nextInt(4));

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
//                currentPlayer.setFill(Color.rgb((new Random()).nextInt(255),(new Random()).nextInt(255),(new Random()).nextInt(255)));
                currentPlayer.setShiftInY(currentPlayer.getShiftInY()+2.1);

                for(Obstacle el : ar) {
                    for (int i = 0; i < holder.getChildren().size(); i++) {
                        Group g = (Group) holder.getChildren().get(i);
                        if (g != currentPlayer.getGroup() && g != scoreDisplay && g!=fallLines) {
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
                    currentPlayer.getBall().setEffect(null);
                    currentPlayer.getGroup().getChildren().remove(0);
                    scene.setFill(sceneOld);
                    holder.getChildren().remove(holder.getChildren().indexOf(fallLines));
                    this.stop();
                    timer=0;
                }
            }
        };

        //Bonus 2
        forceFieldTimer=new AnimationTimer() {

            private long timer=0;
            private Color oldPlayerColor;
            private Color sceneOld;
            private Group radialLines;
            private Circle newObj;
            private FadeTransition fadeBall;
            private LocalTime beforeTime;
            private ImageView FieldAround;
            private ImageView Twinkle;

            @Override
            public void handle(long l) {

                if(timer==0) {
                    oldPlayerColor = (Color) currentPlayer.getBall().getFill();
                    sceneOld = (Color) scene.getFill();
                    scene.setFill(Color.BLACK);
                    currentPlayer.setFill(Color.GHOSTWHITE);
                    beforeTime=LocalTime.now();

                    Image FieldImage= null;
                    try {
                        FieldImage = new Image(new FileInputStream("src/Icons/shield.gif"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    FieldAround=new ImageView();
                    FieldAround.setRotate(180);
                    FieldAround.setImage(FieldImage);
                    FieldAround.setFitWidth(80);
                    FieldAround.setPreserveRatio(true);

                    Image Twinkling= null;
                    try {
                        Twinkling = new Image(new FileInputStream("src/Icons/twinkle.gif"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Twinkle=new ImageView();
                    Twinkle.setRotate(0);
                    Twinkle.setImage(Twinkling);
                    Twinkle.setFitWidth(400);
                    Twinkle.setX(200);
                    Twinkle.setY(130);
                    Twinkle.setPreserveRatio(true);


                    Glow ef=new Glow();
                    ef.setLevel(0.7);
                    FieldAround.setEffect(ef);
                    Twinkle.setEffect(ef);

                    currentPlayer.getGroup().getChildren().add(0,FieldAround);
                    currentPlayer.getGroup().getChildren().add(0,Twinkle);
                    timer++;
                }
                FieldAround.setX(currentPlayer.getCenterX()-38.5);
                FieldAround.setY(currentPlayer.getCenterY()-37.5);

                if(LocalTime.now().getSecond() - beforeTime.getSecond() >=10)
                {
                    currentPlayer.getBall().setFill(oldPlayerColor);
                    scene.setFill(sceneOld);
                    currentPlayer.forcefield=false;
                    currentPlayer.getBall().setEffect(null);
                    currentPlayer.getGroup().getChildren().remove(0);
                    currentPlayer.getGroup().getChildren().remove(0);
                    timer=0;
                    this.stop();
                }
            }
        };

        t=new AnimationTimer() {
            final double gravity=0.55;
            final double time=0.55;

            @Override
            public void handle(long now)
            {
                beforecollision=stage.getScene();

                if(currentPlayer.supersonicspeed==false) {
                    JumpBall(currentPlayer.getcurrSpeed(), gravity, time);
                }

                if(!currentPlayer.getAliveStatus()) {
                    this.stop();
                    try {
                        if(currentPlayer.getScore()<1)
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

                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                checkSuperSonic();
                checkForceField();

                if(currentPlayer.supersonicspeed==false && currentPlayer.forcefield==false) {
                    CollisionCheck();
                }

                ColorSwitcherCheck();
                UpdateScore();
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
            if(currentPlayer.forcefield!=true)
            {
                currentPlayer.supersonicspeed=true;
                supersonic.start();
            }

        }
    }

    public void checkForceField()
    {
        if( forceFieldObjects.isEmpty()==false && currentPlayer.getBall().intersects(forceFieldObjects.get(0).getBoundsInParent()))
        {
            int index=holder.getChildren().indexOf(forceFieldObjects.get(0));
            forceFieldObjects.remove(0);
            holder.getChildren().remove(index);
            if(currentPlayer.supersonicspeed!=true)
            {
                currentPlayer.forcefield=true;
                forceFieldTimer.start();
            }
        }
    }
    public void JumpBall(double speeddown, double gravity, double time)
    {
        speeddown+=gravity*time;
        if(Up_Key_Pressed && (currentPlayer.getCenterY()+speeddown)>400)
        {
            speeddown=-16.7*gravity*time;
            Up_Key_Pressed=false;
        }

        else if(Up_Key_Pressed)
        {
            ShiftScreenDown();
            speeddown=-5*gravity*time;
            Up_Key_Pressed=false;
        }

        if(currentPlayer.getCenterY()+speeddown<= showHand.getBoundsInParent().getMinY()-22)
        {
            currentPlayer.setCenterY(currentPlayer.getCenterY() + speeddown);
        }

        if(currentPlayer.getCenterY() >800)
        {
            currentPlayer.setCenterY(currentPlayer.getCenterY() + speeddown);
            currentPlayer.setAliveStatus(false);
        }
        initial=currentPlayer.getShiftInY();

        //checking supersonic speed
        //
        currentPlayer.setcurrSpeed(speeddown);
    }

    public void insertObjects() throws FileNotFoundException {
        dist=430;
        while(ar.size()<7)
        {
            Random ob=new Random();
            int value=ob.nextInt(5)+1;

            if(ar.isEmpty()) {
                switch (value) {

                    case 1:

                        sidelength = 150;
                        Obstacle sq = new SquareObs(400 - sidelength / 2,
                                currentPlayer.getCenterY() - dist-sidelength/2 + 75, sidelength);
                        ((SquareObs) sq).setTransition();
                        ar.add(sq);
                        holder.getChildren().add(sq.getGroup());
                        break;

                    case 2:

                        radius=75;
                        Obstacle cr=new CircleObs(400,
                                currentPlayer.getCenterY() - dist + 75, radius);
                        ar.add(cr);
                        ((CircleObs) cr).setTransition();
                        holder.getChildren().add(cr.getGroup());
                        break;

                    case 3:

                        sidelength=200;
                        Obstacle tr=new TriangleObs ( 400,
                                currentPlayer.getCenterY() - dist - (sidelength/(Math.sqrt(3))),
                                sidelength, currentPlayer.getFill());
                        ((TriangleObs) tr).setTransition();
                        ar.add(tr);
                        holder.getChildren().add(tr.getGroup());
                        break;

                    case 4:

                        sidelength=100;
                        Obstacle pl=new Plus ( 400 ,
                                currentPlayer.getCenterY() - dist,
                                sidelength);
                        ((Plus) pl).setTransition();
                        ar.add(pl);
                        holder.getChildren().add(pl.getGroup());
                        break;

                    case 5:

                        Obstacle unique=new LineObs( 400 ,
                                currentPlayer.getCenterY() - dist);
                        ar.add(unique);
                        holder.getChildren().add(unique.getGroup());
                        break;

                }
            }

            else
            {
                int calc=ar.size()-1;
                value=ob.nextInt(7)+1;
                if(current_cs.isEmpty()==false)
                {
                    while(value==3)
                        value=ob.nextInt(5)+1;
                }
//                value=7;
                switch (value) {
                    case 1:

                        sidelength = 150;
                        Obstacle sq = new SquareObs(400 - sidelength/2
                                , ar.get(calc).getGroup().getBoundsInParent().getCenterY() - dist
                                - sidelength/2 , sidelength);
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
                                last.getBoundsInParent().getCenterY() - dist - (sidelength/(Math.sqrt(3))),
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
                        Obstacle dc=new UniquePatterns(400,last.getBoundsInParent().getCenterY() - dist - (sidelength/(Math.sqrt(3))),sidelength);
                        ar.add(dc);
                        holder.getChildren().add(dc.getGroup());
                        break;
                }
            }

            counter+=1;

            if(counter>=10)
            {
                Group last=ar.get(ar.size()-1).getGroup();
                Group supersonicObj=new Group();
                Image flashImage=new Image(new FileInputStream("src/Icons/illustrate.png"));
                ImageView flash=new ImageView();

                flash.setImage(flashImage);
                flash.setX(365);
                flash.setY((last.getBoundsInParent().getCenterY())-dist/2);
                flash.setFitWidth(62);
                flash.setPreserveRatio(true);

                Glow ef=new Glow();
                ef.setLevel(0.7);
                flash.setEffect(ef);

                RotateTransition rt=new RotateTransition();
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
                counter=0;
            }
            else if(counter==5)
            {
                Group last=ar.get(ar.size()-1).getGroup();
                Group forcefieldObj=new Group();
                Image shieldImage=new Image(new FileInputStream("src/Icons/shield.png"));
                ImageView shield=new ImageView();

                shield.setImage(shieldImage);
                shield.setX(365);
                shield.setY((last.getBoundsInParent().getCenterY())-dist/2);
                shield.setFitWidth(62);
                shield.setPreserveRatio(true);

                Glow ef=new Glow();
                ef.setLevel(0.7);
                shield.setEffect(ef);

                RotateTransition rt=new RotateTransition();
                rt.setAxis(Rotate.Y_AXIS);
                rt.setFromAngle(0);
                rt.setByAngle(360);
                rt.setCycleCount(-1);
                rt.setInterpolator(Interpolator.LINEAR);
                rt.setDuration(Duration.millis(800));
                rt.setNode(shield);
                rt.play();

                forcefieldObj.getChildren().add(shield);
                forceFieldObjects.add(forcefieldObj);
                holder.getChildren().add(forcefieldObj);


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

    public void UpdateScore()
    {
        boolean hit=false;
        while(current_stars.isEmpty()==false && currentPlayer.getBall().intersects(current_stars.get(0).getGroup().getBoundsInParent()))
        {
            hit=true;
            holder.getChildren().remove(holder.getChildren().indexOf(current_stars.get(0).getGroup()));
            current_stars.remove(0);
        }
        if(hit)
        {
            currentPlayer.setScore(currentPlayer.getScore()+1);
            score.setText(Integer.toString(currentPlayer.getScore()));
        }
        else
        {
            if(currentPlayer.getScore()>currentPlayer.getHighest_score())
            {
                currentPlayer.setHighest_score(currentPlayer.getScore());
            }

            highestScore=new Text();
            highestScore.setText(Integer.toString(currentPlayer.getHighest_score()));
            score.setText(Integer.toString(currentPlayer.getScore()));

        }
    }

    public void ShiftScreenDown()
    {
        for(int i=0;i<holder.getChildren().size();i++) {
            Group g=(Group)holder.getChildren().get(i);
            if(g!=currentPlayer.getGroup() && g!=scoreDisplay) {
                TranslateTransition tr = new TranslateTransition();
                tr.setNode(g);

                tr.setDuration(Duration.millis(225));
                tr.setByY(74.78);//94.78
                tr.play();
                currentPlayer.setShiftInY(currentPlayer.getShiftInY()+74.78);
            }
        }
    }

    public void ColorSwitcherCheck()
    {
        boolean hit=false;

        while(current_cs.isEmpty()==false && currentPlayer.getBall().intersects(current_cs.get(0).getBoundsInParent()))
        {
            hit=true;
            holder.getChildren().remove(current_cs.get(0).getGroup());
            current_cs.remove(0);
        }

        if(hit)
        {
            Random ob=new Random();
            int value=ob.nextInt(4)+1;

            if(currentPlayer.getFill().equals(blue))
            {
                while(value==1)
                    value=ob.nextInt(4)+1;
            }
            else if(currentPlayer.getFill().equals(purple))
            {
                while(value==2)
                    value=ob.nextInt(4)+1;
            }
            else if(currentPlayer.getFill().equals(yellow))
            {
                while(value==3)
                    value=ob.nextInt(4)+1;
            }
            else if(currentPlayer.getFill().equals(pink))
            {
                while(value==4)
                    value=ob.nextInt(4)+1;
            }

            switch (value) {
                case 1 :
                    currentPlayer.setFill(blue);
                    break;
                case 2 :
                    currentPlayer.setFill(purple);
                    break;
                case 3 :
                    currentPlayer.setFill(yellow);
                    break;
                case 4 :
                    currentPlayer.setFill(pink);
                    break;
            }
        }
    }
    public void PauseMenuFunction(Stage stage) throws FileNotFoundException
    {
        previous=stage.getScene();
        Pane holder = new Pane();
        Scene scene = new Scene(holder, 800, 800);

        FileInputStream input1 = new FileInputStream("src/Icons/HomeButton.png");
        Image img1= new Image(input1);
        ImageView homeimg = new ImageView(img1);
        homeimg.setFitHeight(70);
        homeimg.setPreserveRatio(true);

        FileInputStream input = new FileInputStream("src/Icons/PlayButton.png");
        Image img = new Image(input);
        ImageView resumeimg = new ImageView(img);
        resumeimg.setFitHeight(150);
        resumeimg.setPreserveRatio(true);

        Button homebutton=new Button();
        homebutton.setTranslateX(50);
        homebutton.setTranslateY(30);
        homebutton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 70px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-max-width: 70px; " +
                        "-fx-max-height: 70px;" +
                        "-fx-background-color: #555555"
        );
        homebutton.setGraphic(homeimg);
        homebutton.setOnAction(new EventHandler<ActionEvent>() {
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

        Button resumebutton=new Button();
        resumebutton.setTranslateX(315);
        resumebutton.setTranslateY(250);
        resumebutton.setStyle(
                "-fx-background-radius: 100em; " +
                        "-fx-min-width: 150px; " +
                        "-fx-min-height: 150px; " +
                        "-fx-max-width: 150px; " +
                        "-fx-max-height: 150px;" +
                        "-fx-background-color: #555555"
        );
        resumebutton.setGraphic(resumeimg);
        resumebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(previous);
            }
        });

        Button savebutton=new Button();
        savebutton.setTranslateX(225);
        savebutton.setTranslateY(450);
        savebutton.setStyle(
                "-fx-background-radius: 20em; " +
                        "-fx-min-width: 350px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-max-width: 350px; " +
                        "-fx-max-height: 70px;" +
                        "-fx-background-color: #5D5D5D;"+
                        "-fx-font-size: 30px;"+
                        "-fx-font-weight: bold;"+
                        "-fx-text-fill: white;"+
                        "-fx-font-family: \"Blissful Thinking\";"
        );
        savebutton.setText("SAVE PROGRESS");
        savebutton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {

                deserializer();
                boolean flag=false;

                for (int i = 0; i < SaveOrLoadGame.saveData.size(); i++) {

                    if (SaveOrLoadGame.saveData.get(i).getName().equals(currentPlayer.getName())) {
                        flag = true;
//                        SaveOrLoadGame.players.set(i, new SavePlayerStats(currentPlayer));
//                        SaveOrLoadGame.player_obstacle.set(i, new SaveObstaclesStats(ar));
//                        SaveOrLoadGame.player_colorSwitchers.set(i, new SaveColorSwitchers(current_cs));
//                        SaveOrLoadGame.player_stars.set(i, new SaveStars(current_stars));

                        SaveOrLoadGame.saveData.set(i,new SaveData(currentPlayer,ar,current_cs,current_stars));
                    }
                }

                if(flag==false)
                {
//                    SaveOrLoadGame.players.add(new SavePlayerStats(currentPlayer));
//                    SaveOrLoadGame.player_obstacle.add(new SaveObstaclesStats(ar));
//                    SaveOrLoadGame.player_colorSwitchers.add(new SaveColorSwitchers(current_cs));
//                    SaveOrLoadGame.player_stars.add(new SaveStars(current_stars));
                    SaveOrLoadGame.saveData.add(new SaveData(currentPlayer,ar,current_cs,current_stars));
                }

                String filenameP="Saves/Players_List.ser";
                String filenameO="Saves/Obstacles_List.ser";
                String filenameCS="Saves/ColorSwitchers.ser";
                String filenameSt="Saves/Stars.ser";
                String filenameSd="Saves/FullData.ser";

                try
                {
//                    FileOutputStream file_p=new FileOutputStream(filenameP);
//                    ObjectOutputStream out_p=new ObjectOutputStream(file_p);
//                    out_p.writeObject(SaveOrLoadGame.players);
//                    out_p.close();
//                    file_p.close();
//
//                    FileOutputStream file_o=new FileOutputStream(filenameO);
//                    ObjectOutputStream out_o=new ObjectOutputStream(file_o);
//                    out_o.writeObject(SaveOrLoadGame.player_obstacle);
//                    out_o.close();
//                    file_o.close();
//
//                    FileOutputStream file_cs=new FileOutputStream(filenameCS);
//                    ObjectOutputStream out_cs=new ObjectOutputStream(file_cs);
//                    out_cs.writeObject(SaveOrLoadGame.player_colorSwitchers);
//                    out_cs.close();
//                    file_cs.close();
//
//                    FileOutputStream file_st=new FileOutputStream(filenameSt);
//                    ObjectOutputStream out_st=new ObjectOutputStream(file_st);
//                    out_st.writeObject(SaveOrLoadGame.player_stars);
//                    out_st.close();
//                    file_st.close();

                    FileOutputStream file_sd=new FileOutputStream(filenameSd);
                    ObjectOutputStream out_sd=new ObjectOutputStream(file_sd);
                    out_sd.writeObject(SaveOrLoadGame.saveData);
                    out_sd.close();
                    file_sd.close();
                }
                catch (IOException e) {
                    System.out.println("Game Save: Unsuccessfull");
                }
            }

            public void deserializer(){

                try {
//                    FileInputStream fs = new FileInputStream("Saves/Players_List.ser");
//                    ObjectInputStream ob = new ObjectInputStream(fs);
//
//                    FileInputStream fs_obs = new FileInputStream("Saves/Obstacles_List.ser");
//                    ObjectInputStream ob_obs = new ObjectInputStream(fs_obs);
//
//                    FileInputStream fs_cs = new FileInputStream("Saves/ColorSwitchers.ser");
//                    ObjectInputStream ob_cs = new ObjectInputStream(fs_cs);
//
//                    FileInputStream fs_st = new FileInputStream("Saves/Stars.ser");
//                    ObjectInputStream ob_st = new ObjectInputStream(fs_st);

                    FileInputStream fs_sd = new FileInputStream("Saves/FullData.ser");
                    ObjectInputStream ob_sd = new ObjectInputStream(fs_sd);

                    while(true) {

                        try {
//                            SaveOrLoadGame.players=(ArrayList<SavePlayerStats>) ob.readObject();
//                            SaveOrLoadGame.player_obstacle=(ArrayList<SaveObstaclesStats>)ob_obs.readObject();
//                            SaveOrLoadGame.player_colorSwitchers=(ArrayList<SaveColorSwitchers>)ob_cs.readObject();
//                            SaveOrLoadGame.player_stars=(ArrayList<SaveStars>)ob_st.readObject();

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
//                    ob.close();
//                    fs.close();
//
//                    ob_obs.close();
//                    fs_obs.close();
//
//                    ob_cs.close();
//                    fs_cs.close();
//
//                    ob_st.close();
//                    fs_st.close();

                    ob_sd.close();
                    fs_sd.close();

                }

                catch (FileNotFoundException e) {
                    System.out.println("HERE");
                }

                catch (IOException e) {
                    ;
                }

            }
        });

        holder.getChildren().addAll(homebutton,resumebutton,savebutton);

        Text pausetxt = new Text (300, 200, "PAUSE");
        pausetxt.setFont(Font.loadFont ("file:resources/fonts/BlissfulThinking.otf", 75));
        pausetxt.setFill(Color.WHITE);

        holder.getChildren().addAll(pausetxt);
        holder.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));

        scene.setFill(bg);
        stage.setTitle("Pause Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void deserializer(){

        try {
//                    FileInputStream fs = new FileInputStream("Saves/Players_List.ser");
//                    ObjectInputStream ob = new ObjectInputStream(fs);
//
//                    FileInputStream fs_obs = new FileInputStream("Saves/Obstacles_List.ser");
//                    ObjectInputStream ob_obs = new ObjectInputStream(fs_obs);
//
//                    FileInputStream fs_cs = new FileInputStream("Saves/ColorSwitchers.ser");
//                    ObjectInputStream ob_cs = new ObjectInputStream(fs_cs);
//
//                    FileInputStream fs_st = new FileInputStream("Saves/Stars.ser");
//                    ObjectInputStream ob_st = new ObjectInputStream(fs_st);

            FileInputStream fs_sd = new FileInputStream("Saves/FullData.ser");
            ObjectInputStream ob_sd = new ObjectInputStream(fs_sd);

            while(true) {

                try {
//                            SaveOrLoadGame.players=(ArrayList<SavePlayerStats>) ob.readObject();
//                            SaveOrLoadGame.player_obstacle=(ArrayList<SaveObstaclesStats>)ob_obs.readObject();
//                            SaveOrLoadGame.player_colorSwitchers=(ArrayList<SaveColorSwitchers>)ob_cs.readObject();
//                            SaveOrLoadGame.player_stars=(ArrayList<SaveStars>)ob_st.readObject();

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
//                    ob.close();
//                    fs.close();
//
//                    ob_obs.close();
//                    fs_obs.close();
//
//                    ob_cs.close();
//                    fs_cs.close();
//
//                    ob_st.close();
//                    fs_st.close();

            ob_sd.close();
            fs_sd.close();

        }

        catch (FileNotFoundException e) {
            System.out.println("HERE");
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
//                        SaveOrLoadGame.players.set(i, new SavePlayerStats(currentPlayer));
//                        SaveOrLoadGame.player_obstacle.set(i, new SaveObstaclesStats(ar));
//                        SaveOrLoadGame.player_colorSwitchers.set(i, new SaveColorSwitchers(current_cs));
//                        SaveOrLoadGame.player_stars.set(i, new SaveStars(current_stars));

//                System.out.println(SaveOrLoadGame.saveData.get(i).getHighestScore());
            }
        }

//        if(flag==false)
//        {
////                    SaveOrLoadGame.players.add(new SavePlayerStats(currentPlayer));
////                    SaveOrLoadGame.player_obstacle.add(new SaveObstaclesStats(ar));
////                    SaveOrLoadGame.player_colorSwitchers.add(new SaveColorSwitchers(current_cs));
////                    SaveOrLoadGame.player_stars.add(new SaveStars(current_stars));
//            SaveOrLoadGame.saveData.add(new SaveData(currentPlayer,ar,current_cs,current_stars));
//        }

        String filenameP="Saves/Players_List.ser";
        String filenameO="Saves/Obstacles_List.ser";
        String filenameCS="Saves/ColorSwitchers.ser";
        String filenameSt="Saves/Stars.ser";
        String filenameSd="Saves/FullData.ser";

        try
        {
//                    FileOutputStream file_p=new FileOutputStream(filenameP);
//                    ObjectOutputStream out_p=new ObjectOutputStream(file_p);
//                    out_p.writeObject(SaveOrLoadGame.players);
//                    out_p.close();
//                    file_p.close();
//
//                    FileOutputStream file_o=new FileOutputStream(filenameO);
//                    ObjectOutputStream out_o=new ObjectOutputStream(file_o);
//                    out_o.writeObject(SaveOrLoadGame.player_obstacle);
//                    out_o.close();
//                    file_o.close();
//
//                    FileOutputStream file_cs=new FileOutputStream(filenameCS);
//                    ObjectOutputStream out_cs=new ObjectOutputStream(file_cs);
//                    out_cs.writeObject(SaveOrLoadGame.player_colorSwitchers);
//                    out_cs.close();
//                    file_cs.close();
//
//                    FileOutputStream file_st=new FileOutputStream(filenameSt);
//                    ObjectOutputStream out_st=new ObjectOutputStream(file_st);
//                    out_st.writeObject(SaveOrLoadGame.player_stars);
//                    out_st.close();
//                    file_st.close();

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

//        holder.getChildren().remove(holder.getChildren().indexOf(currentPlayer.getGroup()));
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
