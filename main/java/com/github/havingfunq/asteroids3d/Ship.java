package com.github.havingfunq.asteroids3d;


/**
 * Created by VF on 9/16/2016.
 *
 * logic ported from http://www.blitzbasic.com/Community/posts.php?topic=48800
 */
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by VF on 9/15/2016.
 */
public class Ship {
    // these variables are tricky to set, review the code in logic()
    final float acceleration = 0.003f;
    final float friction = 0.000014f;
    final float topSpeed = 100.0f;
    final float turnAcceleration = 1.8f;
    final float turnFriction = 0.06f;
    final float turnMax = 3.0f;

    float turnSpeed;
    float xSpeed;
    float ySpeed;
    float zSpeed;
    float directionXY;

    // center, not edges of spaceship
    float posX;
    float posY;
    float posZ;

    boolean crashed = false;
    boolean explosionDone = true;
    float explosionDelta = 0.0f;
    private float turnSpeedZ = 0.0f;
    private float turnAccelerationZ = 1.8f;
    public float directionZ;

    public Ship(){
        super();
    }

    public float getXSpeed(){
        return xSpeed;
    }

    public float getYSpeed() {
        return ySpeed;
    }

    public float getZSpeed() { return zSpeed; }

    public float getX(){
        return posX;
    }

    public float getY(){
        return posY;
    }

    public float getZ() { return posZ;}

    public void accelerate(){
        xSpeed += (float) Math.sin(Math.toRadians((double)directionZ))*Math.cos(Math.toRadians((double)directionXY)) * acceleration;
        ySpeed += (float) Math.sin(Math.toRadians((double)directionZ))*Math.sin(Math.toRadians((double)directionXY)) * acceleration;
        zSpeed += (float) Math.cos(Math.toRadians((double)directionZ)) * acceleration;
    }

    public void decelerate(){
        xSpeed -= (float) Math.sin(Math.toRadians((double)directionZ))*Math.cos(Math.toRadians((double)directionXY)) * acceleration;
        ySpeed -= (float) Math.sin(Math.toRadians((double)directionZ))*Math.sin(Math.toRadians((double)directionXY)) * acceleration;
        zSpeed -= (float) Math.cos(Math.toRadians((double)directionZ)) * acceleration;
    }

    public void turnAccelerateXY(){
        turnSpeed -= turnAcceleration;
    }

    public void turnDeccelerateXY(){
        turnSpeed += turnAcceleration;
    }

    public void turnAccelerationZ() {
        turnSpeedZ -= turnAccelerationZ;
    }

    public void turnDeccelerateZ() {
        turnSpeedZ += turnAccelerationZ;
    }

    public void logic(){
        float speedVectorLength = (float) Math.sqrt((double) xSpeed*xSpeed + ySpeed * ySpeed + zSpeed * zSpeed );

        if( speedVectorLength > 0 ) {
            // decrease speed with friction of moving
            xSpeed -= (xSpeed/speedVectorLength)*friction;
            ySpeed -= (ySpeed/speedVectorLength)*friction;
            zSpeed -= (zSpeed/speedVectorLength)*friction;
        }

//        if ( speedVectorLength > topSpeed ) {
//            xSpeed += (xSpeed/speedVectorLength)*(topSpeed - speedVectorLength);
//            ySpeed += (ySpeed/speedVectorLength)*(topSpeed - speedVectorLength);
//        }

        posX += xSpeed;
        posY += ySpeed;
        posZ += zSpeed;

        // limit turnSpeed
        if(turnSpeed > turnMax ) { turnSpeed = turnMax; }
        if(turnSpeed < -turnMax ) { turnSpeed = -1.0f * turnMax; }
        if(turnSpeedZ > turnMax ) { turnSpeedZ = turnMax; }
        if(turnSpeedZ < -turnMax ) { turnSpeedZ = -1.0f * turnMax; }

        directionXY += turnSpeed;
        directionZ += turnSpeedZ;

        //reset rotation,
        if(directionXY > 360) { directionXY -= 360f; }
        if(directionXY < 0) { directionXY += 360f; }
        if(directionZ > 360) { directionZ -= 360f; }
        if(directionZ < 0) { directionZ += 360f; }

        //change turnSpeed
        if(turnSpeed > turnFriction) { turnSpeed -= turnFriction; }
        if(turnSpeed < -1.0f*turnFriction) { turnSpeed += turnFriction; }
        if(turnSpeedZ > turnFriction) { turnSpeedZ -= turnFriction; }
        if(turnSpeedZ < -1.0f*turnFriction) { turnSpeedZ += turnFriction; }

        // reset turnSpeed if turnSpeed > turnFriction
        if((turnSpeed < turnFriction) && (turnSpeed > -1.0f*turnFriction )) { turnSpeed = 0.0f; }
        if((turnSpeedZ < turnFriction) && (turnSpeedZ > -1.0f*turnFriction )) { turnSpeedZ = 0.0f; }

        // reset ship coordinates if off screen
        if(posX > 1.0f ) { posX = -1.0f; }
        if(posX < -1.0f ) { posX = 1.0f; }
        if(posY > 1.0f ) { posY = -1.0f; }
        if(posY < -1.0f ) { posY = 1.0f; }

        if(explosionDelta > 1.0f) {
            explosionDone = true;
        } else {    explosionDelta = explosionDelta + 0.01f;
            //directionXY = directionXY + 1.0f;
        }
    }

    public synchronized void explode() {
        crashed = true;

        explosionDone = false;
    }

    public boolean crashed(){
        return crashed;
    }


}