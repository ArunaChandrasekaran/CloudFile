package homework;

import java.util.Scanner;

public class BankAccount {

    private double balance = 0;

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient Balance");
        }
    }

    public double checkBalance() {
        return balance;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankAccount account = new BankAccount();

        while (true) {
            System.out.println("\n1.Deposit");
            System.out.println("2.Withdraw");
            System.out.println("3.Check Balance");
            System.out.println("4.Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter amount to deposit: ");
                    account.deposit(sc.nextDouble());
                    break;

                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    account.withdraw(sc.nextDouble());
                    break;

                case 3:
                    System.out.println("Balance: " + account.checkBalance());
                    break;

                case 4:
                    System.out.println("Program Exited");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}