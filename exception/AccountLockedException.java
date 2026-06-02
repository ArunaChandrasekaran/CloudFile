package exception;

public class AccountLockedException extends Exception

{
    AccountLockedException(String msg)
    {
        super(msg);
    }
}