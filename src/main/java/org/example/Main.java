package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger threeLetterCount = new AtomicInteger();
    static AtomicInteger fourLetterCount = new AtomicInteger();
    static AtomicInteger fiveLetterCount = new AtomicInteger();

    public static void main(String[] args) {

        // TODO: 04.12.2022  Создайте генератор текстов и сгенерируйте набор из 100'000 текстов, используя код из описания задачи.
        // TODO: 04.12.2022  Заведите в статических полях три счётчика - по одному для длин: 3, 4 и 5.
        // TODO: 04.12.2022  Заведите три потока - по одному на каждый критерий "красоты" слова. Каждый поток проверяет все тексты на "красоту" и увеличивает счётчик нужной длины, если текст соответствует критериям.
        // TODO: 04.12.2022  После окончания работы всех потоков выведите результаты на экран.
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        long startTs = System.currentTimeMillis();
        Thread thread1 = new Thread(() -> {
            for (String word : texts) {
                if (isOneLetter(word)) count(word);
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            for (String word : texts) {
                if (isAlphabeticalOrder(word) && !isOneLetter(word)) count(word);
            }
        });
        thread2.start();

        Thread thread3 = new Thread(() -> {
            for (String word : texts) {
                if (isPalindrome(word) && !isOneLetter(word)) count(word);
            }
        });
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Красивых слов с длиной 3: " + threeLetterCount + " шт.");
        System.out.println("Красивых слов с длиной 4: " + fourLetterCount + " шт.");
        System.out.println("Красивых слов с длиной 5: " + fiveLetterCount + " шт.");
        long endTs = System.currentTimeMillis();
        System.out.println("Time: " + (endTs - startTs) + "ms");

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    //проверка на то, что слово состоит из одной и той же буквы

    public static boolean isOneLetter(String word) {
        char temp = word.charAt(0);
        boolean ok = true;
        for (char letter : word.toCharArray()) {
            if (letter != temp) {
                ok = false;
                break;
            }
        }
        return ok;
    }

    //проверка на то, что буквы идут в алфавитном порядке

    public static boolean isAlphabeticalOrder(String word) {
        char temp = word.charAt(0);
        boolean ok = true;
        for (char letter : word.toCharArray()) {
            if (temp != letter && temp > letter) {
                ok = false;
                break;
            } else {
                temp = letter;
            }
        }
        return ok;
    }

    // проверка на палиндром

    static public boolean isPalindrome(String word) {
        char[] charAr = word.toCharArray();
        boolean ok = true;
        for (int i = 0; i < charAr.length / 2; i++) {
            if (charAr[i] != charAr[(charAr.length - 1) - i]) {
                ok = false;
                break;
            }
        }
        return ok;
    }

    // работа со счетчиком

    public static void count(String word) {
        if (word.length() == 3) threeLetterCount.getAndIncrement();
        else if (word.length() == 4) fourLetterCount.getAndIncrement();
        else if (word.length() == 5) fiveLetterCount.getAndIncrement();
    }
}