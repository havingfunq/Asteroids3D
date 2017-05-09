package com.github.havingfunq.asteroids3d;


import android.util.Log;

/**
 * Created by VF on 9/18/2016.
 */
public class Shot {
    private float xPos;
    private float yPos;
    private float zPos;
    private float direction;
    private float direction2;
    private float xSpeed;
    private float ySpeed;
    private float zSpeed;
    private float shotSpeed = 0.01f;

    public Shot(float _xPos, float _yPos, float _zPos, float _xSpeed, float _ySpeed, float _zSpeed, float _direction, float _direction2){
        xPos = _xPos;
        yPos = _yPos;
        zPos = _zPos;
        direction = _direction;
        direction2 = _direction2;
        xSpeed = (float) Math.sin( Math.toRadians( (double) direction2) ) * (float) Math.cos(Math.toRadians((double)direction)) * shotSpeed + _xSpeed;
        ySpeed = (float) Math.sin( Math.toRadians( (double) direction2) ) * (float )Math.sin(Math.toRadians((double)direction))* shotSpeed + _ySpeed;
        zSpeed = (float) Math.cos( Math.toRadians( (double) direction2)  ) * shotSpeed + _zSpeed;
    }

    public void logic() {
        xPos += xSpeed;
        yPos += ySpeed;
        zPos += zSpeed;

        Log.i("Shot", "xPos: " + String.valueOf(xPos) + ", yPos: " + String.valueOf(yPos) + ", zPos: " + String.valueOf(zPos));
    }

    public float getX(){
        return xPos;
    }

    public float getY(){
        return yPos;
    }

    public float getZ() { return zPos; }

    public float getDirection() { return direction; }

    public float getDirection2() { return direction2; }
}