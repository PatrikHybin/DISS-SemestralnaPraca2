package Generators;

import java.util.Random;

public class Seeder {
    private static Random seeder = new Random();

    public static long getSeed() {
        return seeder.nextLong();
    }
}
