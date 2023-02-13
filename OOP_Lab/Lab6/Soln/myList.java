public class myList implements interfaceClass{

    int head;
    int arr[] = new int[100];
    @Override
    public void add(int x) {
        arr[head]=x;
        head++;
    }
    public int getidx(int x)
    {
        for(int i=0;i<head;i++)
        {
            if(arr[i]==x)
            {
                return i;
            }
        }
        return  -1;
    }
    public void remove(int x)
    {
        int idx=getidx(x);
        if(idx!=-1)
        {
            for(int i=idx+1;i<head;i++)
            {
                int tmp = arr[i];
                arr[i]=arr[i-1];
                arr[i-1]=tmp;
            }
            head--;
        }
    }

    @Override
    public boolean find(int x) {
        for(int i=0;i<head;i++)
        {
            if(arr[i]==x)
            {
                System.out.println(x + " is in the list");
                return  true;
            }
        }
        System.out.println(x + " is not in the list");
        return  false;
    }

    @Override
    public void display() {
        for(int i=0;i<head;i++)
        {
            System.out.print(arr[i]+ " ");
        }
        System.out.println("");
    }
}
