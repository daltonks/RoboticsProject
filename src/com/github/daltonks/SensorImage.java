package com.github.daltonks;

public class SensorImage {
    private Color[][] colors;
    private int resolutionX;
    private int resolutionY;

    public int getResolutionX() {
        return resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public SensorImage(char[] charColors, int resolutionX, int resolutionY) {
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;

        colors = new Color[resolutionX][resolutionY];

        for(int y = 0; y < resolutionY; y++) {
            for(int x = 0; x < resolutionX; x++) {
                int redCharIndex = (y * resolutionX + x) * 3;

                colors[x][y] = new Color(
                    charColors[redCharIndex],
                    charColors[redCharIndex + 1],
                    charColors[redCharIndex + 2]
                );
            }
        }
    }

    public Color getColor(int x, int y) {
        return colors[x][y];
    }
}