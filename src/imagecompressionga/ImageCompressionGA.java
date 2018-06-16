/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagecompressionga;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Maximiliano Casale
 */
public class ImageCompressionGA {

    //Img attributes
    ///////////////////////////////////////// Image properties.
    private final int width = 600;
    private final int height = 400;
    private final String imgAddress = "flying.bmp";
    private final int cols = 150;
    private final int rows = 100;
    //////////////////////////////////////////
    private File f = null;
    private BufferedImage image = null;
    private final int blocks = cols * rows;
    private BufferedImage[] blocksArray = new BufferedImage[this.blocks];
    private Evolution[] evolvedBlocks = new Evolution[this.blocks];

    /**
     * Start Main method for running the image compression.
     */
    public void start() {
        int iteraciones = 100;
        int[] compressedSize = new int[iteraciones];
        for (int i = 0; i < iteraciones; i++){
        //Load Image
        this.readImage();
        //Step 1
        this.createSubImg();
        //Step 2
        this.evolveBlocks();
        //Step 3
       compressedSize[i] = this.calculateCompressedSize();
       System.out.println((i+1) + ".- Compression Complete. Size: "+ compressedSize[i] +" Bytes");
       //this.writeImage();
        }
        int sum = 0;
        for(int i = 0; i < iteraciones; i++){
            sum = compressedSize[i] + sum;
        }
        System.out.println("------------------------------------------------------------------------");
        int avg = sum/iteraciones;
        System.out.println("Tamano promedio sobre: " + iteraciones+ " iteraciones: " + avg );
        
    }
    
    public int calculateCompressedSize(){
        int size = 0;
        for(int i = 0; i < this.blocks;  i++){
           String dividendoBits =  Integer.toBinaryString(this.evolvedBlocks[i].compressedValues[0][0].intValue());
           String divisorBits =  Integer.toBinaryString(this.evolvedBlocks[i].compressedValues[0][1].intValue());
           size = size + dividendoBits.length()/8 + divisorBits.length()/8;
        }
        
        return size;
    }
    
    /**
     * Rationalize Blocks Method for concatenating the values of every Pixel in
     * RGB as one string.
     */
    public void evolveBlocks() {

        for (int i = 0; i < this.blocks; i++) {
            this.evolvedBlocks[i] = new Evolution(this.blocksArray[i]);
        }
        System.out.println("Evolving...");
        for (int i = 0; i < this.blocks; i++) {
            this.evolvedBlocks[i].start();
        }
        for (int i = 0; i < this.blocks; i++) {
            try {
                this.evolvedBlocks[i].join();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        System.out.println("Evolution Done.");
    }

    /**
     * Create SubImg Separates the main image into multiple subImages.
     */
    public void createSubImg() {
        int blockWidth = this.image.getWidth() / this.cols;
        int blockHeight = this.image.getHeight() / this.rows;
        int blockCounter = 0;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //init new block
                this.blocksArray[blockCounter] = new BufferedImage(blockWidth, blockHeight, image.getType());
                // draws the image block on the array  given position
                Graphics2D gr = this.blocksArray[blockCounter++].createGraphics();
                gr.drawImage(image, 0, 0, blockWidth, blockHeight, blockWidth * y, blockHeight * x, blockWidth * y + blockWidth, blockHeight * x + blockHeight, null);
                gr.dispose();
            }
        }
        System.out.println("Splitting done.");
    }

    /**
     * readingImage Method for saving the image as an object using an input
     * file.
     */
    public void readImage() {
        try {
            this.f = new File(this.imgAddress);
            this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            this.image = ImageIO.read(f);
            System.out.println("Image loaded. Size: " + this.image.getWidth() * this.image.getHeight() * 3 + " Bytes");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }//reading img ends here

    /**
     * Read Image Method for writing the compressed image into the root folder.
     */
    public void writeImage() {
        try {
            f = new File("CompressedImg.jpg"); //Output address
            ImageIO.write(image, "bmp", f);
            System.out.println("Decompression Complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }//end of write img

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ImageCompressionGA main = new ImageCompressionGA();
        main.start();
    }
} //end of class
