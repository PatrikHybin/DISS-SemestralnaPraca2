package Generators;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class GenEmpirical extends Generator {

    private ArrayList<EmpParams> params;
    private Random genProb;
    private ArrayList<Random> genRand = new ArrayList<>();

    public GenEmpirical() {
        this.params = new ArrayList<>();
        this.genProb = new Random(Seeder.getSeed());
    }

    @Override
    public double sample() {
        double sum = 0;
        int count = 0;

        double prob = this.genProb.nextDouble();

        for (EmpParams empParams : this.params) {
            sum += empParams.getProbability();
            if (prob < sum) {
                return this.genRand.get(count).nextInt((int) (empParams.getMax() + 1 - empParams.getMin())) + empParams.getMin();
            }
            count++;
        }
        try {
            throw new Exception("Couldn't find empirical parameters for " + prob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }

    public void addParams(EmpParams inParams) {
        this.params.add(inParams);
        this.genRand.add(new Random(Seeder.getSeed()));
    }

}
