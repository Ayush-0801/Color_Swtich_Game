import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

import java.util.Random;

public class SquareObs extends Obstacle{

    private Line _side1;
    private Line _side2;
    private Line _side3;
    private Line _side4;

    private double strokeValue=15;

    public SquareObs(double x,double y,double sl)
    {
        super(x,y);
        _side1=new Line(x,y,x,y+sl);
        _side2=new Line(x,y+sl,x+sl,y+sl);
        _side3=new Line(x+sl,y+sl,x+sl,y);
        _side4=new Line(x+sl,y,x,y);

        formation();
        this.setObstacleType("SquareObs");
        setIntialBounds();
    }

    @Override
    public void formation() {

        _side1.setStrokeLineCap(StrokeLineCap.ROUND);
        _side2.setStrokeLineCap(StrokeLineCap.ROUND);
        _side3.setStrokeLineCap(StrokeLineCap.ROUND);
        _side4.setStrokeLineCap(StrokeLineCap.ROUND);

        _side1.setStrokeWidth(strokeValue);
        _side2.setStrokeWidth(strokeValue);
        _side3.setStrokeWidth(strokeValue);
        _side4.setStrokeWidth(strokeValue);

        _side1.setStroke(blue);
        _side2.setStroke(purple);
        _side3.setStroke(yellow);
        _side4.setStroke(pink);

        g=new Group();
        g.getChildren().addAll(_side1,_side2,_side3,_side4);
    }

    public void setTransition()
    {
        Random value=new Random();
        int ob=value.nextInt(2)+1;

        switch(ob) {
            case 1:
                setRotationC();
                break;
            case 2:
                setRotationAC();
                break;
        }
    }
    @Override
    public Group getGroup() {
        return g;
    }
}
