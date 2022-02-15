package org.firstinspires.ftc.teamcode;

public enum Zone {

    //Todo - link xpos

    ONE(10, 180), TWO(8, 380), THREE(12, 800);

    private final double x;
    private final double deg;

    private Zone(final double x, final double deg) {
        this.x = x;
        this.deg = deg;
    }

    public double getX() {return x;}

    public double getDeg() {return deg;}
}
