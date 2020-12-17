import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.Random;

public class CircleObs extends Obstacle{

    private double strokeValue=15;
    private Arc _a1;
    private Arc _a2;
    private Arc _a3;
    private Arc _a4;
    private Circle boundaryCircle;

    public CircleObs(double x, double y, double r)
    {
        super(x,y);

        _a1=new Arc();
        _a1.setCenterX(x);
        _a1.setCenterY(y);
        _a1.setRadiusX(r);
        _a1.setRadiusY(r);


        _a2=new Arc();
        _a2.setCenterX(x);
        _a2.setCenterY(y);
        _a2.setRadiusX(r);
        _a2.setRadiusY(r);


        _a3=new Arc();
        _a3.setCenterX(x);
        _a3.setCenterY(y);
        _a3.setRadiusX(r);
        _a3.setRadiusY(r);

        _a4=new Arc();
        _a4.setCenterX(x);
        _a4.setCenterY(y);
        _a4.setRadiusX(r);
        _a4.setRadiusY(r);

        formation();
        this.setObstacleType("CircleObs");
        setIntialBounds();
        boundaryCircle=new Circle(x,y,r-_a1.getStrokeWidth()/2-20);
    }

    public Circle getBoundaryCircle()
    {
        return boundaryCircle;
    }

    @Override
    public Group getGroup()
    {
        return g;
    }

    @Override
    public void formation() {
        _a1.setStartAngle(0);
        _a1.setLength(90);
        _a1.setFill(blue);
        _a1.setType(ArcType.OPEN);
        _a1.setStrokeType(StrokeType.CENTERED);
        _a1.setStrokeWidth(strokeValue);
        _a1.setFill(Color.TRANSPARENT);

        _a2.setStartAngle(90);
        _a2.setLength(90);
        _a2.setFill(purple);
        _a2.setType(ArcType.OPEN);
        _a2.setStrokeType(StrokeType.CENTERED);
        _a2.setStrokeWidth(strokeValue);
        _a2.setFill(Color.TRANSPARENT);

        _a3.setStartAngle(180);
        _a3.setLength(90);
        _a3.setFill(yellow);
        _a3.setType(ArcType.OPEN);
        _a3.setStrokeType(StrokeType.CENTERED);
        _a3.setStrokeWidth(strokeValue);
        _a3.setFill(Color.TRANSPARENT);

        _a4.setStartAngle(270);
        _a4.setLength(90);
        _a4.setFill(pink);
        _a4.setType(ArcType.OPEN);
        _a4.setStrokeType(StrokeType.CENTERED);
        _a4.setStrokeWidth(strokeValue);
        _a4.setFill(Color.TRANSPARENT);

        _a1.setStroke(blue);
        _a2.setStroke(purple);
        _a3.setStroke(yellow);
        _a4.setStroke(pink);

        g=new Group();
        g.getChildren().addAll(_a1,_a2,_a3,_a4);
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

    public void setColor(Color a,Color b,Color c, Color d)
    {
        _a1.setStroke(a);
        _a2.setStroke(b);
        _a3.setStroke(c);
        _a4.setStroke(d);
    }

    public void changeStroke(double value)
    {
        strokeValue=value;
        _a1.setStrokeWidth(strokeValue);
        _a2.setStrokeWidth(strokeValue);
        _a3.setStrokeWidth(strokeValue);
        _a4.setStrokeWidth(strokeValue);
    }
}
