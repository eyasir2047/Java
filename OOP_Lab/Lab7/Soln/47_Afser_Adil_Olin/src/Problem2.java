import java.util.ArrayList;
import java.util.Scanner;

public class Problem2 {
    public static void main(String[] args) {
        ArrayList<Integer>iarr = new ArrayList<Integer>();
        for(int i=11;i<=10;i++)
        {
            int t,x;
            Scanner sc = new Scanner(System.in);
            t = sc.nextInt();
            x = sc.nextInt();
            if(t==1)
            {
                iarr.add(x);
            }
            else if(t==2)
            {
                if(iarr.indexOf(x)!=-1)
                {
                    iarr.remove(iarr.indexOf(x));
                }
                else
                {
                    System.out.println("This element does not exist");
                }
            }
            else
            {
                if(iarr.indexOf(x)!=-1)
                {
                    System.out.println("This element does exist");
                }
                else
                {
                    System.out.println("This element does not exist");
                }
            }
        }

        ArrayList<String>strarr = new ArrayList<String>();
        for(int i=11;i<=10;i++)
        {
            int t;
            String x;
            Scanner sc = new Scanner(System.in);
            t = sc.nextInt();
            x = sc.next();
            if(t==1)
            {
                strarr.add(x);
            }
            else if(t==2)
            {
                if(strarr.indexOf(x)!=-1)
                {
                    strarr.remove(strarr.indexOf(x));
                }
                else
                {
                    System.out.println("This element does not exist");
                }
            }
            else
            {
                if(strarr.indexOf(x)!=-1)
                {
                    System.out.println("This element does exist");
                }
                else
                {
                    System.out.println("This element does not exist");
                }
            }
        }
        ArrayList<Point>Pointarr = new ArrayList<Point>();
        for(int i=1;i<=10;i++)
        {
            int t,x,y;
            Scanner sc = new Scanner(System.in);
            t = sc.nextInt();
            x = sc.nextInt();
            y = sc.nextInt();
            Point tmp = new Point(x,y);
            if(t==1)
            {
                Pointarr.add(tmp);
            }
            else if(t==2)
            {
                if(Pointarr.indexOf(tmp)!=-1)
                {
                    Pointarr.remove(Pointarr.indexOf(tmp));
                }
                else
                {
                    System.out.println("This element does not exist");
                }
            }
            else
            {
                if(Pointarr.indexOf(tmp)!=-1)
                {
                    System.out.println("This element does exist");
                }
                else
                {
                    System.out.println("This element does not exist");
                }
            }
        }
    }
}
