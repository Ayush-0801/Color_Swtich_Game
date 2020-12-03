import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {

    //For Player
    private String name;
    private double current_x;
    private double current_y;
    private double ballradius;
    private int colorCode;              //1 blue , 2 purple, 3 yellow, 4 pink
    private int score;
    private int highest_score;
    private double shiftHolderY=0;

    //For Obstacles
    private ArrayList<String> obstacleType;
    private ArrayList<Double> obstacleCenterX;
    private ArrayList<Double> obstacleCenterY;
    private ArrayList<Integer> obstacleRotationVariable;

    //For Color Switchers
    private ArrayList<Double> cs_X;
    private ArrayList<Double> cs_Y;

    //For Stars
    private ArrayList<Double> st_X;
    private ArrayList<Double> st_Y;

    public SaveData(Player currentPlayer, ArrayList<Obstacle> arobs,ArrayList<ColorSwitcher> arcs,ArrayList<Star> arst)
    {
        savePlayer(currentPlayer);
        saveObstacles(arobs);
        saveColorSwitcher(arcs);
        saveStars(arst);
    }

    //For Player
    public void savePlayer(Player currentPlayer)
    {
        current_x=currentPlayer.getCenterX();
        current_y=currentPlayer.getCenterY();
        ballradius=currentPlayer.getBall().getRadius();
        score=currentPlayer.getScore();
        highest_score=currentPlayer.getHighest_score();
        saveColor(currentPlayer);
        name=currentPlayer.getName();
        shiftHolderY=currentPlayer.getShiftInY();
    }

    public void saveColor(Player currentPlayer)
    {
        if(currentPlayer.equals(Color.rgb(65,228,243)))
        {
            colorCode=1;
        }

        else if(currentPlayer.equals(Color.rgb(147,33,252)))
        {
            colorCode=2;
        }

        else if(currentPlayer.equals(Color.rgb(247,225,29)))
        {
            colorCode=3;
        }

        else if(currentPlayer.equals(Color.rgb(255,16,136)))
        {
            colorCode=4;
        }
    }

    String getName()
    {
        return this.name;
    }

    double getRadius()
    {
        return this.ballradius;
    }

    double centerX()
    {
        return this.current_x;
    }

    double centerY()
    {
        return this.current_y;
    }

    int colorCode()
    {
        return this.colorCode;
    }

    int getScore()
    {
        return this.score;
    }

    int getHighestScore()
    {
        return this.highest_score;
    }

    double getShiftHolderY()
    {
        return this.shiftHolderY;
    }

    //For Obstacles
    public void saveObstacles(ArrayList<Obstacle> arobs)
    {
        obstacleType=new ArrayList<String>();
        obstacleCenterX=new ArrayList<Double>();
        obstacleCenterY=new ArrayList<Double>();
        obstacleRotationVariable=new ArrayList<Integer>();
        for(Obstacle obs : arobs)
        {
            obstacleType.add(obs.getObstacleType());
            obstacleCenterX.add(obs.getCenterX()+(obs.getGroup().getBoundsInParent().getCenterX()-obs.getInitialX()));
            obstacleCenterY.add(obs.getCenterY()+(obs.getGroup().getBoundsInParent().getCenterY()-obs.getInitialY()));
            obstacleRotationVariable.add(obs.getRotationVariable());

        }
    }

    public ArrayList<String> getTypes()
    {
        return obstacleType;
    }

    public ArrayList<Double> getXcoor()
    {
        return obstacleCenterX;
    }

    public ArrayList<Double> getYcoor()
    {
        return obstacleCenterY;
    }

    public ArrayList<Integer> getObstacleRotationVariable()
    {
        return obstacleRotationVariable;
    }

    //For Color Switchers
    public void saveColorSwitcher(ArrayList<ColorSwitcher> arcs)
    {
        cs_X=new ArrayList<Double>();
        cs_Y=new ArrayList<Double>();

        for(ColorSwitcher obj : arcs)
        {
            cs_X.add(obj.getGroup().getBoundsInParent().getCenterX());
            cs_Y.add(obj.getGroup().getBoundsInParent().getCenterY());
        }

    }

    public ArrayList<Double> getX()
    {
        return cs_X;
    }

    public ArrayList<Double> getY()
    {
        return cs_Y;
    }

    //For Stars
    public void saveStars(ArrayList<Star> arst)
    {
        st_X=new ArrayList<Double>();
        st_Y=new ArrayList<Double>();

        for(Star obj : arst)
        {
            st_X.add(obj.getCenterX()+
                    obj.getGroup().getBoundsInParent().getCenterX()
                    -obj.getInitialBX());

            st_Y.add(obj.getCenterY()+
                    obj.getGroup().getBoundsInParent().getCenterY()
                    -obj.getInitialBY());
        }

    }

    public ArrayList<Double> getSt_X()
    {
        return st_X;
    }

    public ArrayList<Double> getSt_Y()
    {
        return st_Y;
    }



}
