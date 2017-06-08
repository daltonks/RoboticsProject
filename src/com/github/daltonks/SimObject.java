package com.github.daltonks;

public class SimObject {
    private Simulation simulation;
    private int handle;

    public SimObject(int handle, Simulation simulation) {
        this.handle = handle;
        this.simulation = simulation;
    }
}
