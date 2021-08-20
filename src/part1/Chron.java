package part1;

public class Chron {

    private static volatile boolean switcher;

    public static void main(String[] args) {
        Object flag = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (flag) {
                for (int i = 1; i < 12; i++) {
                    System.out.println("Time passed from start programm " + i + " sec");
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i % 5 == 0 && i != 0) {
                        switcher = true;
                        flag.notify();
                        try {
                            flag.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (flag) {
                for (int i=1;true ;i++ ) {
                    if (switcher) {
                        int a = 0;
                        a = 5*i;
                        System.out.println(a + " sec remain");
                        switcher = false;
                        flag.notifyAll();

                        try {
                            flag.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}