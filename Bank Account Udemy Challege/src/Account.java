import java.awt.image.WritableRenderedImage;

public class Account {

    private String number;
    private double balance;
    private String customerName;
    private String customerEmail;
    private String customerPhone;

        //constructor form--> constructor has the same name as class, and it doesn't have
    //any return type //if constructor is not called, there is default constructor
    public Account(String number,double balance, String customerName,String email, String phone){
        System.out.println("Account constructor with parameters called");
        this.number=number;
        this.balance=balance;
        this.customerName=customerName;
        customerEmail=email;
        customerPhone=phone;
    }
    public void depositFunds(double depositAmount){
        balance +=depositAmount;
        System.out.println("Deposit of $"+ depositAmount+". New Balance is $"+balance);
    }
    public void withdrawFunds(double withdrawalAmount){
        if(balance- withdrawalAmount<0){
            System.out.println("Insufficient funds! You have only have $"+balance+" in your account");
        }
        else{
           balance-=withdrawalAmount;
            System.out.println("Withdrawal of $"+ withdrawalAmount+". Remaining Balance= $"+ balance);
        }
    }
    public String getNumber() {
        return number;
    }//getter

    public void setNumber(String number) { //setter ( don't return anything)
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
