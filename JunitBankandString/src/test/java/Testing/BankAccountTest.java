

package Testing;
import homework.BankAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

    @Test
    void testDeposit() {
        BankAccount account = new BankAccount();

        account.deposit(1000);

        assertEquals(1000, account.checkBalance());
    }

    @Test
    void testWithdraw() {
        BankAccount account = new BankAccount();

        account.deposit(1000);
        account.withdraw(400);

        assertEquals(600, account.checkBalance());
    }

    @Test
    void testCheckBalance() {
        BankAccount account = new BankAccount();

        account.deposit(500);

        assertEquals(500, account.checkBalance());
    }

    @Test
    void testWithdrawInsufficientBalance() {
        BankAccount account = new BankAccount();

        account.deposit(500);
        account.withdraw(1000);

        assertEquals(500, account.checkBalance());
    }
}