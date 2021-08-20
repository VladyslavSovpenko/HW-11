package part2;
/*
Напишите программу, которая выводит в консоль строку, состоящую из чисел от  1 до n, но с заменой некоторых значений:
если число делится на 3 - вывести "fizz"
если число делится на 5 - вывести "buzz"
если число делится на 3 и на 5 - вывести "fizzbuzz"
Например, для n = 15, ожидаемый результат: 1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz.
Программа должна быть многопоточной, работать с 4 потоками:
Поток A вызывает fizz() чтобы проверить делимость на 3 и вывести fizz.
Поток B вызывает buzz() чтобы проверить делимость на 5 и вывести buzz.
Поток C вызывает fizzbuzz() чтобы проверить делимость на 3 и 5 и вывести fizzbuzz.
Поток D вызывает number() чтобы вывести число.
 */

import java.util.function.IntConsumer;

class Streams {

    private final int n;
    private int currentNumber = 1;

    public Streams(int n) {
        this.n = n;
    }

    public synchronized void fizz(Runnable fizz) {
        while (currentNumber <= n) {
            if (currentNumber % 3 == 0 && currentNumber != 0) {
                fizz.run();
                currentNumber++;
                notifyAll();
            } else {
                hold();
            }
        }
    }

    public synchronized void buzz(Runnable buzz) {
        while (currentNumber <= n) {
            if (currentNumber % 5 == 0 && currentNumber != 0) {
                buzz.run();
                currentNumber++;
                notifyAll();
            } else {
                hold();
            }
        }
    }

    public synchronized void fizzBuzz(Runnable fizzBuzz) {
        while (currentNumber <= n) {
            if (currentNumber % 5 == 0 && currentNumber % 3 == 0 && currentNumber != 0) {
                fizzBuzz.run();
                currentNumber++;
                notifyAll();
            } else {
                hold();
            }
        }
    }

    public synchronized void number(IntConsumer number) {

        while (currentNumber <= n) {
            if (currentNumber % 3 != 0 && currentNumber % 5 != 0) {
                number.accept(currentNumber);
                currentNumber++;
                notify();
            } else {
                hold();
            }
        }
    }

    public void hold() {
        try {
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static class Main {
        public static void main(String[] args) {
            Streams streams = new Streams(16);
            Runnable printFizz = () -> System.out.print("fizz ");
            Runnable printBuzz = () -> System.out.print("buzz ");
            Runnable printFizzBuzz = () -> System.out.print("fizzbuzz ");
            IntConsumer printNumber = i -> System.out.print(i + " ");

            new Thread(() -> streams.fizz(printFizz)).start();
            new Thread(() -> streams.buzz(printBuzz)).start();
            new Thread(() -> streams.fizzBuzz(printFizzBuzz)).start();
            new Thread(() -> streams.number(printNumber)).start();
        }
    }
}
