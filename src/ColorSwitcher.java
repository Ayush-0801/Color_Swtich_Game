import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import java.io.Serializable;

public class ColorSwitcher{

    private Arc cs1;
    private Arc cs2;
    private Arc cs3;
    private Arc cs4;
    private Group g;
    private double centerX;
    private double centerY;
    private double initialBX,initialBY;

    private Color blue=Color.rgb(65,228,243);
    private Color purple=Color.rgb(147,33,252);
    private Color yellow=Color.rgb(247,225,29);
    private Color pink=Color.rgb(255,16,136);

    public ColorSwitcher(double x,double y,double r)
    {
        cs1=new Arc();
        cs2=new Arc();
        cs3=new Arc();
        cs4=new Arc();

        cs1.setCenterX(x);
        cs2.setCenterX(x);
        cs3.setCenterX(x);
        cs4.setCenterX(x);

        cs1.setCenterY(y);
        cs2.setCenterY(y);
        cs3.setCenterY(y);
        cs4.setCenterY(y);

        cs1.setRadiusX(r);
        cs2.setRadiusX(r);
        cs3.setRadiusX(r);
        cs4.setRadiusX(r);

        cs1.setRadiusY(r);
        cs2.setRadiusY(r);
        cs3.setRadiusY(r);
        cs4.setRadiusY(r);
        formation();

        this.centerX=x;
        this.centerY=y;
        this.initialBX=g.getBoundsInParent().getCenterX();
        this.initialBY=g.getBoundsInParent().getCenterY();

    }

    public void formation()
    {
        cs1.setStartAngle(0);
        cs1.setLength(90);
        cs2.setStartAngle(90);
        cs2.setLength(90);
        cs3.setStartAngle(180);
        cs3.setLength(90);
        cs4.setStartAngle(270);
        cs4.setLength(90);

        cs1.setType(ArcType.ROUND);
        cs2.setType(ArcType.ROUND);
        cs3.setType(ArcType.ROUND);
        cs4.setType(ArcType.ROUND);

        cs1.setFill(purple);
        cs2.setFill(yellow);
        cs3.setFill(blue);
        cs4.setFill(pink);

        g=new Group();
        g.getChildren().addAll(cs1,cs2,cs3,cs4);
    }

    Group getGroup()
    {
        return g;
    }

    Bounds getBoundsInParent()
    {
        return g.getBoundsInParent();
    }

    double getX()
    {
        return centerX;
    }
    double getY()
    {
        return centerY;
    }

    double getInitialBX(){return initialBX;}
    double getInitialBY(){return initialBY;}
}
