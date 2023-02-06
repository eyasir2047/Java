package Account;

import java.util.Date;

public class Account {
    private int id;
    private double balance;
    private double annualInterestRate;
    private Date dateCreated;
    //creating the constructor
    public Account(int id, double balance, double annualInterestRate){
        this.id=id;
        this.balance=balance;
        this.annualInterestRate=annualInterestRate;

        dateCreated= new Date();//AS do not create a setDateCreated() method

    }

    public boolean withdraw(double amount){
        if(this.balance<amount){
            System.out.println("invalid request: balance is not sufficient");
            return  false;
        }

        this.balance-=amount;
        return true;
    }


    public void deposit(double amount){
        this.balance+=amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    /* public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }*/ //as do not create a setDateCreated() method


}
