import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.util.Random;

public class UniquePatterns extends Obstacle{

    private double radius=15;
    private double diameter=radius*2;
    private Duration rotation=Duration.millis(4500);
    private double currentx, currenty;
    private double v1_x, v1_y, v2_x, v2_y, v3_x, v3_y, v4_x, v4_y;

    public UniquePatterns(double x,double y, double sl, int randomValue)
    {
        super(x,y);

        g=new Group();
        radius=10;

        int rand;

        if(randomValue==-1) {
            rand = (new Random()).nextInt(4) + 1;
            randValue = rand;
        }

        else
        {
            rand=randomValue;
            randValue=rand;
        }

        switch(rand)
        {
            case 1:

                y -= sl/Math.sqrt(3);

                v1_x = x;
                v1_y = y;

                v2_x = x + sl/(double)2;
                v2_y = y+sl*Math.sqrt(3)/(double)2;

                v3_x = x - sl/(double)2;
                v3_y = y+sl*Math.sqrt(3)/(double)2;

                currentx = v1_x;
                currenty = v1_y;

                while(true) {
                    Circle obj = new Circle(currentx, currenty, radius);

                    if (g.getChildren().size() < 7) {
                        obj.setFill(purple);
                    } else if (g.getChildren().size() < 14) {
                        obj.setFill(pink);
                    } else if (g.getChildren().size() < 21) {
                        obj.setFill(blue);
                    } else {
                        obj.setFill(yellow);
                    }

                    g.getChildren().add(obj);

                    if (g.getChildren().size() <= sl / diameter) {

                        Polygon pth = new Polygon(currentx, currenty, v2_x, v2_y, v3_x, v3_y, v1_x, v1_y, currentx, currenty);
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

                    else if (g.getChildren().size() <= 2 * sl / diameter) {

                        Polygon pth = new Polygon(currentx, currenty, v3_x, v3_y, v1_x, v1_y, v2_x, v2_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx -= diameter;
                    }

                    else if (g.getChildren().size() <= 3 * sl / diameter) {
                        Polygon pth = new Polygon(currentx, currenty, v1_x, v1_y, v2_x, v2_y, v3_x, v3_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx += diameter / 2;
                        currenty -= diameter * Math.sqrt(3) / (double) 2;
                    }

                    if (g.getChildren().size() >= 3 * sl / diameter) {
                        break;
                    }
                }
                break;

            case 2:

                sl-=60;
                v1_x = x - sl/(double)2;
                v1_y = y - sl/(double)2;

                v2_x = x + sl/(double)2;
                v2_y = y - sl/(double)2;

                v3_x = x + sl/(double)2;
                v3_y = y + sl/(double)2;

                v4_x = x - sl/(double)2;
                v4_y = y + sl/(double)2;

                currentx = v1_x;
                currenty = v1_y;

                while(true) {

                    Circle obj = new Circle(currentx, currenty, radius);

                    g.getChildren().add(obj);

                    if (g.getChildren().size() <= sl / diameter) {

                        obj.setFill(purple);

                        Polygon pth = new Polygon(currentx, currenty, v2_x, v2_y, v3_x, v3_y, v4_x, v4_y, v1_x, v1_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx +=diameter;
                    }

                    else if (g.getChildren().size() <= 2 * sl / diameter) {

                        obj.setFill(pink);

                        Polygon pth = new Polygon(currentx, currenty, v3_x, v3_y, v4_x, v4_y, v1_x, v1_y, v2_x, v2_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currenty += diameter;
                    }

                    else if (g.getChildren().size() <= 3 * sl / diameter) {

                        obj.setFill(blue);

                        Polygon pth = new Polygon(currentx, currenty, v4_x, v4_y, v1_x, v1_y, v2_x, v2_y, v3_x, v3_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx -= diameter;
                    }

                    else if (g.getChildren().size() <= 4 * sl / diameter) {

                        obj.setFill(yellow);

                        Polygon pth = new Polygon(currentx, currenty, v1_x, v1_y, v2_x, v2_y, v3_x, v3_y,  v4_x, v4_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currenty-=diameter;
                    }

                    if (g.getChildren().size() >= 4 * sl / diameter) {
                        break;
                    }
                }
                break;
                
             case 3:

                double rotateRadius=70;

                v1_x = x;
                v1_y = y - rotateRadius;

                v2_x = x + rotateRadius;
                v2_y = y;

                v3_x = x;
                v3_y = y + rotateRadius;

                v4_x = x - rotateRadius;
                v4_y = y;

                double theta=0;
                currentx = x + rotateRadius*Math.cos(theta);
                currenty = y - rotateRadius*Math.sin(theta);
                radius=7;

                while(true) {

                    Circle obj = new Circle(currentx, currenty, radius);

                    if (g.getChildren().size() < 6) {
                        obj.setFill(purple);
                    }

                    else if (g.getChildren().size() < 12) {
                        obj.setFill(pink);
                    }

                    else if (g.getChildren().size() < 18) {
                        obj.setFill(blue);
                    }

                    else {
                        obj.setFill(yellow);
                    }

                    g.getChildren().add(obj);

                    if (g.getChildren().size() <= 24) {

                        Path p=new Path();
                        p.getElements().add( new MoveTo( currentx, currenty));

                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(p);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.setAutoReverse(false);
                        path.play();
                    }

                    theta+=Math.PI/((double)2*6);
                    currentx = x + rotateRadius*Math.cos(theta);
                    currenty = y - rotateRadius*Math.sin(theta);

                    if (g.getChildren().size() >= 24) {
                        break;
                    }
                }

                ScaleTransition st=new ScaleTransition();
                st.setByX(0.8);
                st.setByY(0.8);
                st.setCycleCount(-1);
                st.setAutoReverse(true);
                st.setDuration(Duration.millis(1000));
                st.setNode(g);
                st.play();

                RotateTransition rt=new RotateTransition();
                rt.setDuration(Duration.millis(4000));
                rt.setCycleCount(-1);
                rt.setInterpolator(Interpolator.LINEAR);
                rt.setNode(g);
                rt.setFromAngle(0);
                rt.setToAngle(360);
                rt.play();
                break;

            case 4:

                v1_x = x;
                v1_y = y - sl/(double)2;

                v2_x = x + sl/(double)2;
                v2_y = y;

                v3_x = x;
                v3_y = y + sl/(double)2;

                v4_x = x - sl/(double)2;
                v4_y = y;

                currentx = v1_x;
                currenty = v1_y;

                while(true) {

                    Circle obj = new Circle(currentx, currenty, radius);
                    g.getChildren().add(obj);

                    if (g.getChildren().size() <= sl/(Math.sqrt(2)*diameter)) {

                        obj.setFill(purple);

                        Polygon pth = new Polygon(currentx, currenty, v2_x, v2_y, v3_x, v3_y, v4_x, v4_y, v1_x, v1_y);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx += diameter/Math.sqrt(2);
                        currenty += diameter/Math.sqrt(2);
                    }

                    else if (g.getChildren().size() <= 2 * sl / (Math.sqrt(2)*diameter)) {

                        obj.setFill(pink);

                        Polygon pth = new Polygon(currentx, currenty, v3_x, v3_y, v4_x, v4_y, v1_x, v1_y, v2_x, v2_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx -= diameter/Math.sqrt(2);
                        currenty += diameter/Math.sqrt(2);
                    }

                    else if (g.getChildren().size() <= 3 * sl / (Math.sqrt(2)*diameter)) {

                        obj.setFill(blue);

                        Polygon pth = new Polygon(currentx, currenty, v4_x, v4_y, v1_x, v1_y, v2_x, v2_y, v3_x, v3_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx -= diameter/Math.sqrt(2);
                        currenty -= diameter/Math.sqrt(2);
                    }

                    else if (g.getChildren().size() < 4 * sl / (Math.sqrt(2)*diameter)) {

                        obj.setFill(yellow);

                        Polygon pth = new Polygon(currentx, currenty, v1_x, v1_y, v2_x, v2_y, v3_x, v3_y,  v4_x, v4_y, currentx, currenty);
                        PathTransition path = new PathTransition();
                        path.setNode(obj);
                        path.setPath(pth);
                        path.setDuration(rotation);
                        path.setCycleCount(-1);
                        path.setInterpolator(Interpolator.LINEAR);
                        path.play();

                        currentx += diameter/Math.sqrt(2);
                        currenty -= diameter/Math.sqrt(2);
                    }

                    if(g.getChildren().size() >= 28){
                        break;
                    }
                }
                break;
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
