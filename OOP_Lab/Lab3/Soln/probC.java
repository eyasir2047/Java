class probCM {
    private int m;
    public probCM(int x) {
        this.m = x;
    }
    public void modifyVal(int x) {
        this.m = x;
    }
    public void display() {
        System.out.println(m);
    }
}
public class probC {
    private int n;
    public probC(int x) {
        this.n = x;
    }
    public static void main(String[] args) {
        var a = new probC(10);
        var b = new probCM(20);
        var c = a;
        // System.out.println(a.hashCode() + " " + b.hashCode() + " " + c.hashCode());
        b.modifyVal(200);
        // System.out.println(b.hashCode());
        // b.display();
        c = new probC(0);
        // System.out.println(c.hashCode());
        a = null;
        // System.out.println(a == null);
    }
}


