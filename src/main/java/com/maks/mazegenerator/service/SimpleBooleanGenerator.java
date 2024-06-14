package com.maks.mazegenerator.service;

import java.util.Random;

public class SimpleBooleanGenerator implements BooleanGenerator {
    private final Random random = new Random();

    @Override
    public boolean random() {
        int number = random.nextInt();
        return number % 2 == 0;
    }
}
