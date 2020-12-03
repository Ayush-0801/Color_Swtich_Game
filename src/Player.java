import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.effect.Glow;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int curr_score;
    private int highest_score;
    private long curr_pos;
    private long curr_level;
    private boolean new_player;
    private long ball_radius=10;
    private boolean isAlive;
    private Circle ball;
    private Group g;
    private double curr_speed=0;
    private double shiftInY=0;
    public boolean supersonicspeed=false;
    public boolean forcefield=false;

    private Color purple=Color.rgb(147,33,252);

    public Player()
    {
        ball=new Circle(400,710,ball_radius);
        ball.setFill(purple);
        g=new Group();
        g.getChildren().add(ball);
        curr_score=0;
        highest_score=0;
        isAlive=true;
        new_player=true;
    }

    Group getGroup()
    {
        return g;
    }

    double getCenterX()
    {
        return ball.getCenterX();
    }

    double getCenterY()
    {
        return ball.getCenterY();
    }

    void setCenterY(double yValue)
    {
        ball.setCenterY(yValue);
    }

    void setCenterX(double xValue)
    {
        ball.setCenterX(xValue);
    }

    Color getFill()
    {
        return (Color) ball.getFill();
    }

    void setFill(Color cValue)
    {
        ball.setFill(cValue);
    }

    Circle getBall()
    {
        return ball;
    }

    void setName(String name)
    {
        this.name=name;
    }

    String getName()
    {
        return this.name;
    }

    void setAliveStatus(boolean value)
    {
        isAlive=value;
    }

    boolean getAliveStatus()
    {
        return isAlive;
    }

    void setScore(int curr_score)
    {
        this.curr_score=curr_score;
    }

    void setHighest_score(int highest_score)
    {
        this.highest_score=highest_score;
    }

    int getScore()
    {
        return curr_score;
    }

    int getHighest_score()
    {
        return highest_score;
    }

    void setShiftInY(double y)
    {
        this.shiftInY=y;
    }

    double getShiftInY()
    {
        return this.shiftInY;
    }

    double getcurrSpeed()
    {
        return curr_speed;
    }

    void setcurrSpeed(double curr_speed)
    {
        this.curr_speed=curr_speed;
    }

    void setNew_player(boolean isNew)
    {
        this.new_player=isNew;
    }

    boolean getNew_player()
    {
        return this.new_player;
    }
}
