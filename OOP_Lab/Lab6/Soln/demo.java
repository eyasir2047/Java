import java.util.*;
public class demo {
    public static void main(String[] args) {
        //For Unsorted list
        myList obj = new UnsortedList();
        Scanner input = new Scanner(System.in);
        int q;
        q = input.nextInt();
        while (q>0)
        {
            int t ;
            t = input.nextInt();
            int x;
            x= input.nextInt();
            if(t==1)
            {
                obj.add(x);
            }
            else if(t==2)
            {
                obj.find(x);
            }
            else
            {
                obj.remove(x);
            }
            obj.display();
            q--;
        }
        //for sorted list
        obj = new SortedList();
        q = input.nextInt();
        while (q>0)
        {
            int t ;
            t = input.nextInt();
            int x;
            x= input.nextInt();
            if(t==1)
            {
                obj.add(x);
            }
            else if(t==2)
            {
                obj.find(x);
            }
            else
            {
                obj.remove(x);
            }
            obj.display();
            q--;
        }
    }
}
