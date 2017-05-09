package com.github.havingfunq.asteroids3d;


/**
 * Created by VF on 9/15/2016.
 */
public class Point {
    float x = 0.0f;
    float y = 0.0f;
    float z = 0.0f;
    float ret[] = {};

    public Point(float _x, float _y, float _z){
        x = _x;
        y = _y;
        z = _z;
    }

    public float x(){
        return x;
    }

    public float y(){
        return y;
    }

    public float z() { return z; }

    public void setX(float _x){
        x = _x;
    }

    public void setY(float _y){
        y = _y;
    }

    public void setZ(float _z) { z = _z; }
}