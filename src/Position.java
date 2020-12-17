public class Position {
    private int x;
    private int y;
    private static final long serialVersionUID=4;

    public void Position()
    {
        this.x=0;
        this.y=0;
    }

    public Position(int x_coor,
                    int y_coor)
    {
        this.x=x_coor;
        this.y=y_coor;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setX(int x_coor)
    {
        this.x=x_coor;
    }

    public void setY(int y_coor)
    {
        this.y=y_coor;
    }

}
