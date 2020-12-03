import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.shape.*;

public class LineObs extends Obstacle{

    private Line _l1;
    private Line _l2;
    private Line _l3;
    private Line _l4;
    private Line _l5;
    private double strokeValue=15;

    private double centerX , centerY;

    public LineObs(double x,double y)
    {
        super(x,y);
        _l1=new Line(x,y,x-200,y);
        _l2=new Line(x-200,y,x-400,y);
        _l3=new Line(x,y,x+200,y);
        _l4=new Line(x+200,y,x+400,y);
        _l5=new Line(x-400,y,x-600,y);

        centerX=x;
        centerY=y;
        formation();
        this.setObstacleType("LineObs");
        setIntialBounds();
    }
    @Override
    public void formation() {

        _l1.setStroke(blue);
        _l2.setStroke(purple);
        _l3.setStroke(pink);
        _l4.setStroke(yellow);
        _l5.setStroke(blue);

        _l1.setStrokeWidth(strokeValue);
        _l2.setStrokeWidth(strokeValue);
        _l3.setStrokeWidth(strokeValue);
        _l4.setStrokeWidth(strokeValue);
        _l5.setStrokeWidth(strokeValue);

        g=new Group();
        g.getChildren().addAll(_l4 , _l3 , _l1 , _l2 ,_l5);
        setTransition();
    }

    public void setTransition()
    {
        AnimationTimer t=new AnimationTimer() {
            private int i=0;
            private long counter=1;
            @Override
            public void handle(long l) {
                g.setLayoutX(g.getLayoutX()+5);
                if(g.getBoundsInParent().getMaxX() > 1010)
                {
                    Line obj= (Line) g.getChildren().get(i);
                    obj.setStartX(centerX-400-200*counter);
                    obj.setStartY(centerY);
                    obj.setEndX(obj.getStartX()-200);
                    obj.setEndY(centerY);

                    i++;
                    if(i==5)
                        i=0;
                    counter++;
                }
            }
        };
        t.start();
    }

    @Override
    public Group getGroup() {
        return g;
    }
}
