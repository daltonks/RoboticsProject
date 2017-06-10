package com.github.daltonks.pioneer.imageProcessing;

import com.github.daltonks.Color;
import com.github.daltonks.SensorImage;

public class PioneerProcessedImage {
    private PioneerProcessedImagePixel[][] pixels;

    public PioneerProcessedImage(SensorImage image) {
        pixels = new PioneerProcessedImagePixel[image.getResolutionX()][image.getResolutionY()];
        for(int y = 0; y < image.getResolutionY(); y++) {
            for(int x = 0; x < image.getResolutionX(); x++) {
                Color color = image.getColor(x, y);

                PioneerProcessedImagePixel pixel;
                if(color.red < 20 && color.green < 20 && color.blue < 20) { //Black path
                    pixel = PioneerProcessedImagePixel.Path;
                } else if(color.red > 150 && color.green < 50 && color.blue < 50) { //Red cube
                    pixel = PioneerProcessedImagePixel.Block;
                } else {
                    pixel = PioneerProcessedImagePixel.Nothing;
                }
                pixels[x][y] = pixel;
            }
        }
    }

    public int getResolutionX() { return pixels.length; }
    public int getResolutionY() { return pixels[0].length; }
    public PioneerProcessedImagePixel getPixel(int x, int y) { return pixels[x][y]; }
}