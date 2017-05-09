package com.github.havingfunq.asteroids3d;


/**
 * Created by VF on 9/15/2016.
 */

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.WindowManager;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mProjectionMatrix = new float[16];
    private float ratio;
    private boolean gameOver = false;
    private float[] V;
    private float[] VP;
    private float oldTheta, oldPhi;
    private float newTheta, newPhi;

    private float[] oldBasisX = {1f, 0f, 0f};
    private float[] oldBasisY = {0f, 0f, -1f};

    private float[] newBasisX = {0f, 0f, 0f};
    private float[] newBasisY = {0f, 0f, 0f};

    Button up;
    Button down;
    Button left;
    Button right;
    Button fire;
    Button accelerate;
    Button decelerate;

    Ship ship;
    ShotManager sm;
    RockManager rm;
    GameOverScreen gos;
    private float deltaPhi;
    private float deltaTheta;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        ship = new Ship();

        sm = new ShotManager();
        rm = new RockManager();

        gos = new GameOverScreen();

        V = new float[16];
        VP = new float[16];

    }

    // main loop, its a horid state machine, really no way around this
    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if(!gameOver) {
            // Draw background color

            // draw buttons
            up.draw();
            down.draw();
            left.draw();
            right.draw();
            fire.draw();
            accelerate.draw();
            decelerate.draw();

            //draw Distant Stars
            
            drawStars();
            
            ship.logic();

            newTheta = ship.directionXY;
            newPhi = ship.directionZ;

            deltaTheta = newTheta - oldTheta;
            deltaPhi = newPhi - oldPhi;

            newBasisX = rotateThetaPhi(deltaTheta, deltaPhi, oldBasisX);
            newBasisY = rotateThetaPhi(deltaTheta, deltaPhi, oldBasisY);

            float[] up = crossProduct(newBasisX, newBasisY);

            Matrix.setIdentityM(VP, 0);
            Matrix.setIdentityM(V, 0);
            Matrix.setLookAtM(V, 0, ship.getX(), ship.getY(), ship.getZ(),
                    ship.getX() + (float) Math.sin(Math.toRadians((double)ship.directionZ))* (float)Math.cos(Math.toRadians((double)ship.directionXY)),
                    ship.getY() + (float) Math.sin(Math.toRadians((double)ship.directionZ))* (float)Math.sin(Math.toRadians((double)ship.directionXY)),
                    ship.getZ() + (float) Math.cos(Math.toRadians((double)ship.directionZ)),
                    up[0], up[1], up[2]);

            oldBasisX = newBasisX;
            oldBasisY = newBasisY;

            oldTheta = newTheta;
            oldPhi = newPhi;

            Matrix.multiplyMM(VP, 0, mProjectionMatrix, 0, V, 0);

            // manages phasor blasts
            sm.logic(rm);
            sm.drawAll(VP);

            // iterates rocks, checks for collisions
            rm.logic(ship);
            rm.drawAll(VP);

            if (ship.crashed && ship.explosionDone) {
                Lives.decrement();

                ship.removeShaderProgram();
                sm.removeShaderProgram();
                rm.removeShaderProgram();

                ship = new Ship();

                sm = new ShotManager();
                rm = new RockManager();

                int lives = Lives.livesLeft();

                if (lives == 0) {
                    gos = new GameOverScreen();

                    gameOver = true;
                }
            }
        } else {
            //game over

            gos.incr();
            if(gos.stopGOSAnimation == true) {
                Score.reset();
                Lives.reset();
                gameOver = false;
            }
        }
    }

    private void drawStars() {
    }

    private float[] rotateThetaPhi(float directionXY, float directionZ, float[] oldBasisX) {
        float[] rot = new float[16];
        float[] toBeRotated = {oldBasisX[0], oldBasisX[1], oldBasisX[2], 1.0f};

        // a
        Matrix.setIdentityM(rot, 0);

        //convert point to cartesian so it is easy to work with
        float x = (float) Math.sin(Math.toRadians((double)directionZ))*(float)Math.cos(Math.toRadians((double)directionXY));
        float y = (float) Math.sin(Math.toRadians((double)directionZ))* (float)Math.sin(Math.toRadians((double)directionXY));
        float z = (float) Math.cos(Math.toRadians((double)directionZ));

        float thetaZ =  (float) Math.toDegrees(-Math.atan((double)z/(double)x) + Math.PI / 2);
        float thetaX = (float) Math.toDegrees(-Math.atan((double)z/(double)y) + Math.PI / 2);
        float thetaY =  directionXY;

        Matrix.setRotateM(rot, 0, thetaZ, 0.0f, 0.0f, 1.0f);
        Matrix.multiplyMV(toBeRotated, 0, rot, 0, toBeRotated, 0);

        Matrix.setRotateM(rot, 0, thetaX, 1.0f, 0.0f, 0.0f);
        Matrix.multiplyMV(toBeRotated, 0, rot, 0, toBeRotated, 0);

        Matrix.setRotateM(rot, 0, thetaY, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMV(toBeRotated, 0, rot, 0, toBeRotated, 0);

        //XY should be easy

        return toBeRotated;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;

        float top = 1.0f;
        float bottom = -1.0f;
        float left_ = -1.0f;
        float right_ = 1.0f;

        // these are normalized device coordinates, not pixels
        up = new Button(left_ + .3f, bottom + 1.05f, 0.25f, ratio);
        down = new Button(left_ + .3f, bottom + 0.55f, 0.25f, ratio);
        left = new Button(left_ + .01f, bottom + 0.75f, 0.25f, ratio);
        right = new Button(left_ + .583f, bottom + 0.75f, 0.25f, ratio);
        fire = new Button(right_ - 0.3f, bottom + .75f, 0.25f, ratio);
        accelerate = new Button(right_ - 0.3f, bottom + 1f, .125f, ratio);
        decelerate = new Button(right_ - 0.3f, bottom + .25f, .125f, ratio);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        //Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, -1.0f, 1.0f);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45, ratio, .001f, 1000);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public void scanButtons(float x, float y, float screenWidth, float screenHeight) {
        if(!ship.crashed()) {
            if (up.checkTouch(x, y, screenWidth, screenHeight)) {
                ship.turnAccelerationZ();
            }

            if (down.checkTouch(x, y, screenWidth, screenHeight)) {
                ship.turnDeccelerateZ();
            }

            if (left.checkTouch(x, y, screenWidth, screenHeight)) {
                ship.turnDeccelerateXY();
            }

            if (right.checkTouch(x, y, screenWidth, screenHeight)) {
                ship.turnAccelerateXY();
            }

            if (fire.checkTouch(x, y, screenWidth, screenHeight)) {
                sm.fire(ship.getX(), ship.getY(), ship.getZ(), ship.getXSpeed(), ship.getYSpeed(), ship.getZSpeed(), ship.directionXY, ship.directionZ);
            }

            if(decelerate.checkTouch(x, y, screenWidth, screenHeight)){
                ship.decelerate();
            }

            if(accelerate.checkTouch(x, y, screenWidth, screenHeight)){
                ship.accelerate();
            }
        }
    }

    float[] crossProduct(float[] a, float[] b) {
        float[] ret = {
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };

        return ret;
    }
}

