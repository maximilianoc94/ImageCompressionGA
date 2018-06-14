/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagecompressionga;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Max
 */
public class Evolution extends Thread {

    
    private final int epochs = 1000;
    private final BufferedImage assignedBlock;
    public String rationalizedBlock;
    public String[] population = new String[80];
    public String[] nextGen = new String[80];
    public BigInteger[][] compressedValues = new BigInteger[80][2];
    public int[] fitness = new int[80];
    public BigInteger rational;
    public ArrayList<String> matingPool;
    public String bestEvolution;

    public Evolution(BufferedImage assignedBlock) {
        this.assignedBlock = assignedBlock;
        this.rationalizedBlock = "";
    }

    @Override
    public void run() {
        this.rationalize();
        this.generatePopulation();
        int generacion = 0;
        while (generacion < this.epochs) {
            this.fitnessPopulation();
            this.selectionByWheel();
            //this.selectionByTournament();
            this.nextGenerationByCrossover();
            generacion++;
        }
        this.sortByFitness();
        this.bestEvolution = this.population[0];
    }

    public void sortByFitness() {

        int n = this.population.length;
        for (int i = 1; i < n; ++i) {
            int key = this.fitness[i];
            String value = this.population[i];
            BigInteger dividendo = this.compressedValues[i][0];
            BigInteger divisor = this.compressedValues[i][1];
            int j = i - 1;
            while (j >= 0 && this.fitness[j] > key) {
                this.fitness[j + 1] = this.fitness[j];
                this.population[j + 1] = this.population[j];
                j = j - 1;
            }
            this.fitness[j + 1] = key;
            this.population[j + 1] = value;
            this.compressedValues[j + 1][0] = dividendo;
            this.compressedValues[j + 1][1] = divisor;
        }

    }

    public void nextGenerationByCrossover() {
        int popSize = this.population.length;
        int matingPoolSize = this.matingPool.size();
        for (int i = 0; i < popSize; i++) {
            int a = ThreadLocalRandom.current().nextInt(0, popSize);
            int b = ThreadLocalRandom.current().nextInt(0, popSize);
            String parentA = this.population[a];
            String parentB = this.population[b];
            this.nextGen[i] = this.crossoverChild(parentA, parentB);
        }
        this.population = this.nextGen;
    }

    public String crossoverChild(String parentA, String parentB) {
        int crossoverPoint = ThreadLocalRandom.current().nextInt(0, parentA.length());
        String child;
        child = parentA.substring(0, crossoverPoint) + parentB.substring(crossoverPoint);
        return child;

    }

    public void selectionByWheel() {
        this.matingPool = new ArrayList<>();
        int size = this.population.length;
        for (int i = 0; i < size; i++) {
            int numberRepeat = this.fitness[i];
            for (int j = 0; j < numberRepeat; j++) {
                this.matingPool.add(this.population[i]);
            }
        }
    }

    public void rationalize() {
        for (int i = 0; i < this.assignedBlock.getWidth(); i++) {
            for (int j = 0; j < this.assignedBlock.getHeight(); j++) {
                int pixelRGBA = this.assignedBlock.getRGB(i, j);
                String RGB = Integer.toBinaryString(pixelRGBA).substring(8);
                this.rationalizedBlock = this.rationalizedBlock.concat(RGB);
            }
        }
    }

    public void generatePopulation() {
        int size = this.population.length;
        for (int i = 0; i < size; i++) {
            int cont = 0;
            String parent = "";
            while (cont * 24 < this.assignedBlock.getWidth() * this.assignedBlock.getHeight() - 24) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 23);
                int posicion = cont * 24 + randomNum;
                char chromosome = this.rationalizedBlock.charAt(posicion);
                char sw = (chromosome == '1') ? '0' : '1';
                String mutatedByte = this.rationalizedBlock.substring(cont * 24, posicion) + sw + this.rationalizedBlock.substring(posicion + 1, (cont + 1) * 24);
                parent = parent.concat(mutatedByte);
                cont++;
            }
            cont = 0;
            this.population[i] = parent;
        }

    }

    public void fitnessPopulation() {
        int size = this.population.length;
        BigInteger maxNumber = new BigInteger("9999999");
        for (int i = 0; i < size; i++) {
            this.rational = new BigInteger(this.population[i], 2);
            BigInteger rationalNumber = new BigInteger(this.population[i], 2);
            BigInteger MCD = rationalNumber.gcd(maxNumber);
            this.compressedValues[i][0] = rationalNumber.divide(MCD);
            this.compressedValues[i][1] = maxNumber.divide(MCD);

            if (this.compressedValues[i][0].intValue() <= 1000 || this.compressedValues[i][1].intValue() <= 1000) {
                this.fitness[i] = ((int) (5000 / this.compressedValues[i][0].intValue()));
            } else {
                this.fitness[i] = 1;
            }
        }
    }

}
