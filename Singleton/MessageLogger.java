
package Homework;

class MessageLogger {

  
    private static MessageLogger instance;

    private MessageLogger() {
    }

    public static synchronized MessageLogger getInstance() {
        if (instance == null) {
            instance = new MessageLogger();
        }
        return instance;
    }

    public void logMessage(String msg) {
        System.out.println(Thread.currentThread().getName() + " : " + msg +" object: "+this.hashCode());
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            MessageLogger logger = getInstance();
            logger.logMessage("Thread 1");
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            MessageLogger logger = getInstance();
            logger.logMessage("Thread 2");
        }, "Thread-2");

        t1.start();
        t2.start();
        
        
    }
}

