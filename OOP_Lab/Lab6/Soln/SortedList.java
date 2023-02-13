public class SortedList extends myList{
    @Override
    public void add(int x) {
        super.add(x);
        for(int i=head-2;i>=0;i--)
        {
            if(arr[i]>arr[i+1])
            {
                int tmp = arr[i];
                arr[i]=arr[i+1];
                arr[i+1]=tmp;
            }
            else
            {
                break;
            }
        }
    }

    @Override
    public boolean find(int x) {
        int l = 0 , h = head-1, mid;
        while (l<=h)
        {
            mid = (l+h)/2;
            if(x==arr[mid])
            {
                System.out.println(x + " is in the list");
                return  true;
            }
            else if(x<arr[mid])
            {
                h=mid-1;
            }
            else
            {
                l = mid+1;
            }
        }
        System.out.println(x + " is not in the list");
        return false;
    }
}
