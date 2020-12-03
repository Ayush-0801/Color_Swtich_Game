import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Random;

public class Plus extends Obstacle{

    private Line _side1;
    private Line _side2;
    private Line _side3;
    private Line _side4;
    private double strokeValue=15;
    private int shifter=-1;
    private double shiftValue=75;
    private double sidelength=0;

    public Plus(double x,double y, double sl)
    {
        super(x,y);
        sidelength=sl;

        Random value=new Random();
        shifter=value.nextInt(2)+1;

        switch (shifter) {
            case 1:
                x -= shiftValue;
                break;
            case 2:
                x += shiftValue;
                break;
        }
        _side1=new Line(x,y,x+sl,y);
        _side2=new Line(x,y,x,y+sl);
        _side3=new Line(x,y,x-sl,y);
        _side4=new Line(x,y,x,y-sl);

        formation();
        this.setObstacleType("PlusObs");
        setIntialBounds();
    }

    @Override
    public void formation() {
        _side1.setStrokeWidth(strokeValue);
        _side2.setStrokeWidth(strokeValue);
        _side3.setStrokeWidth(strokeValue);
        _side4.setStrokeWidth(strokeValue);

        _side1.setStrokeLineCap(StrokeLineCap.ROUND);
        _side2.setStrokeLineCap(StrokeLineCap.ROUND);
        _side3.setStrokeLineCap(StrokeLineCap.ROUND);
        _side4.setStrokeLineCap(StrokeLineCap.ROUND);

        _side1.setStroke(blue);
        _side2.setStroke(purple);
        _side3.setStroke(yellow);
        _side4.setStroke(pink);

        g=new Group();
        g.getChildren().addAll(_side1,_side2,_side3,_side4);
    }

    public void setTransition()
    {
        if(shifter==1)
        {
            setRotationAC();
        }

        else if(shifter==2)
        {
            setRotationC();
        }
    }

    public void setShifter(int value)
    {
        shifter=value;
        redefine();
    }

    public void redefine()
    {
        double x=centerX;
        double y=centerY;

        switch (shifter) {
            case 1:
                x -= shiftValue;
                break;
            case 2:
                x += shiftValue;
                break;
        }

        _side1.setStartX(x);
        _side1.setStartY(y);
        _side1.setEndX(x+sidelength);
        _side1.setEndY(y);

        _side2.setStartX(x);
        _side2.setStartY(y);
        _side2.setEndX(x);
        _side2.setEndY(y+sidelength);

        _side3.setStartX(x);
        _side3.setStartY(y);
        _side3.setEndX(x-sidelength);
        _side3.setEndY(y);

        _side4.setStartX(x);
        _side4.setStartY(y);
        _side4.setEndX(x);
        _side4.setEndY(y-sidelength);

    }
    @Override
    public Group getGroup() {
        return g;
    }
}
