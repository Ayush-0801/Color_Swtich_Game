import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class UniquePatterns extends Obstacle{

    private double radius=15;
    private double diameter=radius*2;
    private Duration rotation=Duration.millis(4500);

    public UniquePatterns(double x,double y, double sl)
    {
        super(x,y);
        double currentx=x, currenty=y;

        double v1_x = x, v1_y = y;
        double v2_x = x + sl/(double)2, v2_y = y+sl*Math.sqrt(3)/(double)2;
        double v3_x = x - sl/(double)2, v3_y = y+sl*Math.sqrt(3)/(double)2;

        g=new Group();

        while(true)
        {
            Circle obj = new Circle(currentx, currenty, radius);

            if(g.getChildren().size()<7) {
                obj.setFill(purple);
            }

            else if(g.getChildren().size()<14)
            {
                obj.setFill(pink);
            }

            else if(g.getChildren().size()<21)
            {
                obj.setFill(blue);
            }

            else
            {
                obj.setFill(yellow);
            }

            g.getChildren().add(obj);

            if(g.getChildren().size()<=sl/diameter) {

                Polygon pth=new Polygon(currentx, currenty, v2_x, v2_y, v3_x, v3_y,  v1_x, v1_y, currentx, currenty);
                PathTransition path = new PathTransition();
                path.setNode(obj);
                path.setPath(pth);
                path.setDuration(rotation);
                path.setCycleCount(-1);
                path.setInterpolator(Interpolator.LINEAR);
                path.play();

                currentx += diameter / (double) 2;
                currenty += diameter * Math.sqrt(3) / (double) 2;
            }

            else if(g.getChildren().size()<=2*sl/diameter) {

                Polygon pth=new Polygon(currentx, currenty, v3_x, v3_y,  v1_x, v1_y, v2_x, v2_y,currentx, currenty);
                PathTransition path = new PathTransition();
                path.setNode(obj);
                path.setPath(pth);
                path.setDuration(rotation);
                path.setCycleCount(-1);
                path.setInterpolator(Interpolator.LINEAR);
                path.play();

                currentx -= diameter;
            }

            else if(g.getChildren().size()<=3*sl/diameter)
            {
                Polygon pth=new Polygon(currentx, currenty, v1_x, v1_y, v2_x, v2_y,  v3_x, v3_y, currentx, currenty);
                PathTransition path = new PathTransition();
                path.setNode(obj);
                path.setPath(pth);
                path.setDuration(rotation);
                path.setCycleCount(-1);
                path.setInterpolator(Interpolator.LINEAR);
                path.play();

                currentx += diameter/2;
                currenty -= diameter * Math.sqrt(3)/(double)2;
            }

            if(g.getChildren().size()>=3*sl/diameter)
            {
                break;
            }
        }

        formation();
        this.setObstacleType("DottedObs");
        setIntialBounds();
    }

    @Override
    public void formation() {

    }

    @Override
    public Group getGroup() {
        return g;
    }
}