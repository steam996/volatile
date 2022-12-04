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
        AtomicInteger count = new AtomicInteger();
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        new Thread(() -> {
            for (String word: texts) {
                isPalindrome(word);
            }
            count.getAndIncrement();
        }).start();
        new Thread(() -> {
            for (String word: texts) {
                isOneLetter(word);
            }
            count.getAndIncrement();
        }).start();
        new Thread(() -> {
            for (String word: texts) {
                isAlphabeticalOrder(word);
            }
            count.getAndIncrement();
        }).start();
        while (count.get() != 3);
            System.out.println("Красивых слов с длиной 3: " + threeLetterCount + " шт.");
            System.out.println("Красивых слов с длиной 4: " + fourLetterCount + " шт.");
            System.out.println("Красивых слов с длиной 5: " + fiveLetterCount + " шт.");


    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // проверка на палиндром
    static public void isPalindrome (String word) {
        char[] charAr = word.toCharArray();
        boolean ok = true;
        for (int i = 0; i < charAr.length / 2; i++) {
            if (charAr[i] != charAr[(charAr.length - 1) - i]) {
                ok = false;
                break;
            }
        }
        if (ok && word.length() == 3) threeLetterCount.getAndIncrement();
        else if (ok && word.length() == 4) fourLetterCount.getAndIncrement();
        else if (ok && word.length() == 5) fiveLetterCount.getAndIncrement();
    }

    //проверка на то, что слово состоит из одной и той же буквы

    public static void isOneLetter(String word) {
        char[] charAr = word.toCharArray();
        char temp = charAr[0];
        boolean ok = true;
        for (char letter : charAr) {
            if (letter != temp) {
                ok = false;
                break;
            }
        }
        if (ok && word.length() == 3) threeLetterCount.getAndIncrement();
        else if (ok && word.length() == 4) fourLetterCount.getAndIncrement();
        else if (ok && word.length() == 5) fiveLetterCount.getAndIncrement();
    }

    public static void isAlphabeticalOrder(String word) {
        char[] charAr = word.toCharArray();
        char temp = charAr[0];
        boolean ok = true;
        if ((word.contains("a") && word.contains("b"))
                || (word.contains("a") && word.contains("c"))
                || (word.contains("c") && word.contains("b"))) {
            for (char letter : charAr) {
                if (temp != letter && temp > letter) {
                    ok = false;
                    break;
                } else {
                    temp = letter;
                }
            }
        } else ok = false;
        if (ok && word.length() == 3) threeLetterCount.getAndIncrement();
        else if (ok && word.length() == 4) fourLetterCount.getAndIncrement();
        else if (ok && word.length() == 5) fiveLetterCount.getAndIncrement();
    }
}