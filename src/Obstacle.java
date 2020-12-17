import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public abstract class Obstacle {

    private Position obs_pos;
    private String type;
    protected Color blue=Color.rgb(65,228,243);
    protected Color purple=Color.rgb(147,33,252);
    protected Color yellow=Color.rgb(247,225,29);
    protected Color pink=Color.rgb(255,16,136);
    protected Group g;

    protected double centerX;
    protected double centerY;
    protected double initialX;
    protected double initialY;
    protected int rotationVariable=0;
    protected int randValue;

    private static final long serialVersionUID=6;

    public Obstacle(double x, double y) {
        centerX=x;
        centerY=y;
        randValue=-1;
    }

    public double getCenterX()
    {
        return centerX;
    }

    public double getCenterY()
    {
        return centerY;
    }

    public void setPosition(Position value)
    {
        this.obs_pos=value;
    }

    public abstract void formation();
    public abstract Group getGroup();

    public String getObstacleType()
    {
        return this.type;
    }
    public void setObstacleType(String obs_type)
    {
        this.type=obs_type;
    }

    public void setRotationC()
    {
        RotateTransition rt = new RotateTransition();
        rt.setFromAngle(0);
        rt.setByAngle(360);
        rt.setCycleCount(-1);
        rt.setDuration(Duration.millis(3000));
        rt.setNode(g);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
        rotationVariable=1;
    }
    public void setRotationAC()
    {
        RotateTransition rt = new RotateTransition();
        rt.setFromAngle(0);
        rt.setByAngle(-360);
        rt.setCycleCount(-1);
        rt.setDuration(Duration.millis(3000));
        rt.setNode(g);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
        rotationVariable=-1;
    }

    public void setIntialBounds()
    {
        initialX=g.getBoundsInParent().getCenterX();
        initialY=g.getBoundsInParent().getCenterY();
    }

    public double getInitialX()
    {
        return initialX;
    }

    public double getInitialY()
    {
        return initialY;
    }

    public int getRotationVariable()
    {
        return rotationVariable;
    }

    public int getRandValue()
    {
        return randValue;
    }
}
