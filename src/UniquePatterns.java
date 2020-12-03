import javafx.animation.ParallelTransition;
import javafx.scene.Group;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class UniquePatterns extends Obstacle {

    private Path tripa;
    private double h;

    public UniquePatterns(double x,double y, double sl)
    {
        super(x,y);
        h=y;
        tripa = new Path();
        MoveTo moveTo = new MoveTo(x, y);
        tripa.getElements().add(moveTo);
        LineTo m1 =new LineTo(x+sl,y);
        LineTo m2 =new LineTo(x+sl/2,y-sl);
        LineTo m3=new LineTo(x,y);
        tripa.getElements().addAll(m1,m2,m3);

        formation();
        this.setObstacleType("DottedObs");
        setIntialBounds();

    }

    @Override
    public void formation() {

        ArrayList<Circle> circle=new ArrayList<>();
        for(int i=0;i<24;i++)
        {
            Circle smc=new Circle();
            smc.setCenterX(300);
            smc.setCenterY(h);
            smc.setRadius(10);

            if(i>5 && i<12)
            {
                smc.setFill(blue);
            }
            else if(i>11 && i<18)
            {
                smc.setFill(yellow);
            }
            else if(i>17 && i<24)
            {
                smc.setFill(purple);
            }
            else
            {
                smc.setFill(pink);
            }
            circle.add(smc);
        }
        g=new Group();
        g.getChildren().addAll(circle);
        transition(207,circle);
    }

    public void transition(int t1, ArrayList<Circle> cc)
    {
        int alpha = Integer.MAX_VALUE;
        ParallelTransition seqT=new ParallelTransition();

        for(int i=0;i<g.getChildren().size();i++)
        {
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(5000));
            pathTransition.setNode(cc.get(i));
            pathTransition.setDelay(Duration.millis(t1*(i)));
            pathTransition.setPath(tripa);
            pathTransition.setCycleCount(alpha);
            pathTransition.setAutoReverse(false);
            pathTransition.setInterpolator(Interpolator.LINEAR);
//            pathTransition.play();

            seqT.getChildren().add(pathTransition);

            //playing the transition
        }
        seqT.play();
    }

    @Override
    public Group getGroup() {
        return g;
    }
}
