import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.image.*;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Star {
    private Image StarImage;
    private ImageView viewStar;
    private Group g;
    private double centerX,centerY;
    private double initialBX,initialBY;

    private static final long serialVersionUID=3;

    public Star(double x,double y) throws FileNotFoundException {
        StarImage=new Image(new FileInputStream("src/Icons/star.png"));
        viewStar=new ImageView(StarImage);
        viewStar.setX(x);
        viewStar.setY(y);

        this.centerX=x;
        this.centerY=y;

        formation();
        g=new Group();
        g.getChildren().add(viewStar);

        this.initialBX=g.getBoundsInParent().getCenterX();
        this.initialBY=g.getBoundsInParent().getCenterY();
    }
    public void formation()
    {
        viewStar.setFitHeight(25);
        viewStar.setFitWidth(25);
        viewStar.setPreserveRatio(true);
        viewStar.setSmooth(true);
        setTransition();
    }

    public void setTransition()
    {
        ScaleTransition sc=new ScaleTransition();
        sc.setByX(0.3);
        sc.setByY(0.3);
        sc.setCycleCount(Integer.MAX_VALUE);
        sc.setAutoReverse(true);
        sc.setDuration(Duration.millis(600));
        sc.setNode(viewStar);
        sc.play();
    }

    public ImageView getStar()
    {
        return viewStar;
    }

    public Group getGroup()
    {
        return g;
    }

    public double getCenterX()
    {
        return centerX;
    }

    public double getCenterY()
    {
        return centerY;
    }

    public double getInitialBX()
    {
        return initialBX;
    }

    public double getInitialBY()
    {
        return initialBY;
    }
}
