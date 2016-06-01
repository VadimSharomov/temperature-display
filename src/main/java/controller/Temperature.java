package controller;

public class Temperature {
    private final int value;
    private final int time;

    public Temperature(int time, int value) {
        this.time = time;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getTime() {
        return time;
    }
}
