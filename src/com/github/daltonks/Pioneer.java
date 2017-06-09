package com.github.daltonks;

public class Pioneer {
    private SimObject leftMotor;
    private SimObject rightMotor;
    private SimObject camera;

    public Pioneer(SimObject leftMotor, SimObject rightMotor, SimObject camera) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.camera = camera;
    }

    public void update() {
        SensorImage image = camera.getVisionSensorImage();
        int halfWidth = image.getResolutionX() / 2;
        int numPathPixels = 0;
        double totalNormalizedPathXes = 0;
        for(int y = 0; y < image.getResolutionY(); y++) {
            for(int x = 0; x < image.getResolutionX(); x++) {
                Color color = image.getColor(x, y);
                //Black path
                if(color.red < 20 && color.green < 20 && color.blue < 20) {
                    totalNormalizedPathXes += (x - halfWidth) / (double) halfWidth;
                    numPathPixels++;
                    //Red cube
                } else if(color.red > 150 && color.green < 50 && color.blue < 50) {

                }
            }
        }
        float speed = 3f;
        if(numPathPixels != 0) {
            float averageNormalizedPathX = (float) (totalNormalizedPathXes / numPathPixels);
            float leftMotorVelocity;
            float rightMotorVelocity;
            if(averageNormalizedPathX >= 0) {
                leftMotorVelocity = 1 * speed;
                rightMotorVelocity = (-averageNormalizedPathX + 1) * speed;
            } else {
                leftMotorVelocity = averageNormalizedPathX + 1;
                rightMotorVelocity = 1 * speed;
            }
            leftMotor.setJointTargetVelocity(leftMotorVelocity);
            rightMotor.setJointTargetVelocity(rightMotorVelocity);
            System.out.println(averageNormalizedPathX);
        }
        else {
            leftMotor.setJointTargetVelocity(speed);
            rightMotor.setJointTargetVelocity(0);
        }
    }
}
