package com.github.daltonks;

public class Constants {
    public static final String REMOTE_IP = "127.0.0.1";
    public static final int REMOTE_PORT = 19999;
    public static final boolean WAIT_UNTIL_CONNECTED = true;
    public static final boolean DO_NOT_RECONNECT_ONCE_DISCONNECTED = true;
    public static final int TIME_OUT_IN_MS = 10000;
    public static final int COMM_THREAD_CYCLE_MILLIS = 5;
    public static final int UPDATE_LOOP_MILLIS = 30;

    public static final String LEFT_MOTOR_NAME = "Pioneer_p3dx_leftMotor";
    public static final String RIGHT_MOTOR_NAME = "Pioneer_p3dx_rightMotor";
    public static final String CAMERA_NAME = "cam1";

    public static final float PIONEER_SPEED = 3;
    public static final float PIONEER_MOTOR_VELOCITY_DAMPING = .9f;

    public static final short PATH_MAX_RED = 20;
    public static final short PATH_MAX_GREEN = 20;
    public static final short PATH_MAX_BLUE = 20;

    public static final short BLOCK_MIN_RED = 150;
    public static final short BLOCK_MAX_GREEN = 50;
    public static final short BLOCK_MAX_BLUE = 50;

    public static final int NUM_BLOCK_PIXELS_BEFORE_FOLLOW = 170;

    public static final int NUM_BLOCK_PIXELS_BEFORE_CONSIDERING_COLLISION = 7000;
    public static final int ACCEPT_BLOCK_COLLISION_MILLIS = 400;

    public static final int REVERSE_TIME_MILLIS = 2000;
}
