package problem2;

 class student{
    private String name;
    private int rollNumber;

    student(String name,int rollNumber){
        this.name=name;
        this.rollNumber=rollNumber;
    }

    student(String name){
        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public  void display(){
        System.out.println("Name:  "+this.name);
        System.out.println("Roll Number :"+this.rollNumber);
    }
}

public class Main {
    public static void main(String[] args) {

            student s=new student("Abrar");//problem-1
            student s2=createStudent("Jone");//problem-2
            s.setName("Galib");
            s.setRollNumber(12);

            s.display();

    }

   public  static student createStudent(String name){
        return  new student(name);

    }
}
