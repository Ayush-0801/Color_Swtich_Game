import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Random;

public class TriangleObs extends Obstacle{

    private Line _side1;
    private Line _side2;
    private Line _side3;
    private Color _ballcolor;
    private double strokeValue=15;

    public TriangleObs(double x,double y, double sl, Color ballcolor)
    {
        super(x,y);
        _side1=new Line(x, y, x+sl/(double)2, y+(sl*Math.sqrt(3))/(double)2);
        _side2=new Line(x+sl/(double)2, y+(sl*Math.sqrt(3))/(double)2, x-sl/(double)2, y+(sl*Math.sqrt(3))/(double)2);
        _side3=new Line(x-sl/(double)2, y+sl*Math.sqrt(3)/(double)2, x, y);

        _ballcolor=ballcolor;
        formation();
        this.setObstacleType("TriangleObs");
        setIntialBounds();
    }

    @Override
    public void formation() {
        _side1.setStrokeWidth(strokeValue);
        _side2.setStrokeWidth(strokeValue);
        _side3.setStrokeWidth(strokeValue);

        _side1.setStrokeLineCap(StrokeLineCap.ROUND);
        _side2.setStrokeLineCap(StrokeLineCap.ROUND);
        _side3.setStrokeLineCap(StrokeLineCap.ROUND);

        if(_ballcolor.equals(blue))
        {
            _side1.setStroke(_ballcolor);
            _side2.setStroke(purple);
            _side3.setStroke(yellow);
        }

        else if(_ballcolor.equals(purple))
        {
            _side1.setStroke(_ballcolor);
            _side2.setStroke(yellow);
            _side3.setStroke(pink);
        }

        else if(_ballcolor.equals(yellow))
        {
            _side1.setStroke(_ballcolor);
            _side2.setStroke(pink);
            _side3.setStroke(blue);
        }

        else if(_ballcolor.equals(pink))
        {
            _side1.setStroke(_ballcolor);
            _side2.setStroke(blue);
            _side3.setStroke(purple);
        }

        else
        {
            _side1.setStroke(blue);
            _side2.setStroke(purple);
            _side3.setStroke(pink);
        }

        g=new Group();
        g.getChildren().addAll(_side1,_side2,_side3);
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
    public Group getGroup()
    {
        return g;
    }
}
