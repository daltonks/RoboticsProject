package com.github.daltonks.pioneer.imageProcessing;

import com.github.daltonks.Color;
import com.github.daltonks.Constants;
import com.github.daltonks.SensorImage;

public class PioneerProcessedImage {
    private PioneerProcessedPixelType[][] pixels;

    public PioneerProcessedImage(SensorImage image) {
        pixels = new PioneerProcessedPixelType[image.getResolutionX()][image.getResolutionY()];
        for(int y = 0; y < image.getResolutionY(); y++) {
            for(int x = 0; x < image.getResolutionX(); x++) {
                Color color = image.getColor(x, y);

                PioneerProcessedPixelType pixel;
                if(color.red < Constants.PATH_MAX_RED
                    && color.green < Constants.PATH_MAX_GREEN
                    && color.blue < Constants.PATH_MAX_BLUE) { //Black path

                    pixel = PioneerProcessedPixelType.Path;
                } else if(color.red > Constants.BLOCK_MIN_RED
                            && color.green < Constants.BLOCK_MAX_GREEN
                            && color.blue < Constants.BLOCK_MAX_BLUE) { //Red cube
                    pixel = PioneerProcessedPixelType.Block;
                } else {
                    pixel = PioneerProcessedPixelType.Nothing;
                }
                pixels[x][y] = pixel;
            }
        }
    }

    public int getResolutionX() { return pixels.length; }
    public int getResolutionY() { return pixels[0].length; }
    public PioneerProcessedPixelType getPixel(int x, int y) { return pixels[x][y]; }

    public Float getAverageNormalizedScreenX(PioneerProcessedPixelType pixelType) {
        int halfWidth = getResolutionX() / 2;
        int numPathPixels = 0;
        double totalNormalizedPathXes = 0;
        for(int y = 0; y < getResolutionY(); y++) {
            for(int x = 0; x < getResolutionX(); x++) {
                if(getPixel(x, y) == pixelType) {
                    totalNormalizedPathXes += (x - halfWidth) / (double) halfWidth;
                    numPathPixels++;
                }
            }
        }
        if(numPathPixels != 0) {
            return (float) (totalNormalizedPathXes / numPathPixels);
        } else {
            return null;
        }
    }

    public int getNumPixels(PioneerProcessedPixelType pixelType) {
        int result = 0;
        for(int y = 0; y < getResolutionY(); y++) {
            for(int x = 0; x < getResolutionX(); x++) {
                if(getPixel(x, y) == pixelType) {
                    result++;
                }
            }
        }
        return result;
    }
}