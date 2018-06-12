/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagecompressionga;

import java.awt.image.BufferedImage;

/**
 *
 * @author Max
 */
public class Rationalize extends Thread{
    private BufferedImage assignedBlock;
    public String rationalizedBlock;

    public Rationalize(BufferedImage assignedBlock) {
        this.assignedBlock = assignedBlock;
        this.rationalizedBlock = "";
    }

    @Override
    public void run() {
        for (int i = 0; i < this.assignedBlock.getWidth(); i++){
            for (int j = 0; j < this.assignedBlock.getHeight(); j++){
                    int pixelRGBA = this.assignedBlock.getRGB(i, j);
                    String RGB = Integer.toBinaryString(pixelRGBA).substring(8);
                    this.rationalizedBlock = this.rationalizedBlock.concat(RGB);
            }
        }
    }
}

