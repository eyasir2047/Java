public class UnsortedList extends myList{
    @Override
    public void remove(int x) {
        int idx=getidx(x);
        if(idx!=-1)
        {
            arr[idx]=arr[head-1];
            head--;
        }
    }
}
