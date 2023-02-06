package Account;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        Account testAccount= new Account(1122,20000,4.5);

        if(testAccount.withdraw(2500)){
            System.out.println("Withdraw successful");
        }
        else{
            System.out.println("Withdraw not successful");
        }

        testAccount.deposit(3000);

        //printing the account information

        System.out.println(testAccount.getId());
        System.out.println(testAccount.getBalance());
        System.out.println(testAccount.getAnnualInterestRate());
        System.out.println(testAccount.getDateCreated().toLocaleString());




    }

}
