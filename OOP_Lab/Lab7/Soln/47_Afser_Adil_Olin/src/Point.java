public class Point {
    private int x , y;
    Point()
    {
        this.x = 0;
        this.y = 0;
    }
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point other = (Point) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }
    Point(int x , int y)
    {
        this.x = x;
        this.y = y;
    }

    int getX()
    {
        return x;
    }
    int getY()
    {
        return y;
    }
    void setX(int x)
    {
        this.x = x;
    }
    void setY(int y)
    {
        this.y = y;
    }



}
