/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagecompressionga;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Max
 */
public class Evolution extends Thread {

    private final BufferedImage assignedBlock;
    public String rationalizedBlock;
    public String[] population = new String[80];

    public Evolution(BufferedImage assignedBlock) {
        this.assignedBlock = assignedBlock;
        this.rationalizedBlock = "";
    }

    @Override
    public void run() {
        this.rationalize();
        this.generatePopulation();
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
        // for fitness
        // long rationalNumber = new BigInteger(this.evolvedBlocks[0].rationalizedBlock, 2).longValue();
        //System.out.println(rationalNumber);
    }

}
