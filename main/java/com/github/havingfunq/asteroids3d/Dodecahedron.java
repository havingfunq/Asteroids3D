package com.github.havingfunq.asteroids3d;

import java.util.ArrayList;

/**
 * Created by Vince on 4/22/2017.
 */

public class Dodecahedron {
    float[][] dodec = new float[20][3];
    float[] verts = new float[500];
    ArrayList<Float> dodecahedron = new ArrayList<Float>();

    public Dodecahedron(){
        float alpha = (float) Math.sqrt(2.0 / (3.0 + Math.sqrt(5.0)));
        float beta = 1.0f + (float) Math.sqrt(6.0 / (3.0 + Math.sqrt(5.0)) - 2.0 + 2.0 * Math.sqrt(2.0 / (3.0 + Math.sqrt(5.0))));

        dodec[0][0] = -alpha; dodec[0][1] = 0f; dodec[0][2] = beta;
        dodec[1][0] = alpha; dodec[1][1] = 0f; dodec[1][2] = beta;
        dodec[2][0] = -1f; dodec[2][1] = -1f; dodec[2][2] = -1f;
        dodec[3][0] = -1f; dodec[3][1] = -1f; dodec[3][2] = 1f;
        dodec[4][0] = -1f; dodec[4][1] = 1f; dodec[4][2] = -1f;
        dodec[5][0] = -1f; dodec[5][1] = 1f; dodec[5][2] = 1f;
        dodec[6][0] = 1f; dodec[6][1] = -1f; dodec[6][2] = -1f;
        dodec[7][0] = 1f; dodec[7][1] = -1f; dodec[7][2] = 1f;
        dodec[8][0] = 1f; dodec[8][1] = 1f; dodec[8][2] = -1f;
        dodec[9][0] = 1f; dodec[9][1] = 1f; dodec[9][2] = 1f;
        dodec[10][0] = beta; dodec[10][1] = alpha; dodec[10][2] = 0f;
        dodec[11][0] = beta; dodec[11][1] = -alpha; dodec[11][2] = 0f;
        dodec[12][0] = -beta; dodec[12][1] = alpha; dodec[12][2] = 0f;
        dodec[13][0] = -beta; dodec[13][1] = -alpha; dodec[13][2] = 0f;
        dodec[14][0] = -alpha; dodec[14][1] = 0f; dodec[14][2] = -beta;
        dodec[15][0] = alpha; dodec[15][1] = 0f; dodec[15][2] = -beta;
        dodec[16][0] = 0f; dodec[16][1] = beta; dodec[16][2] = alpha;
        dodec[17][0] = 0f; dodec[17][1] = beta; dodec[17][2] = -alpha;
        dodec[18][0] = 0f; dodec[18][1] = -beta; dodec[18][2] = alpha;
        dodec[19][0] = 0f; dodec[19][1] = -beta; dodec[19][2] = -alpha;

        pentagon(0, 1, 9, 16, 5);
        pentagon(1, 0, 3, 18, 7);
        pentagon(1, 7, 11, 10, 9);
        pentagon(11, 7, 18, 19, 6);
        pentagon(8, 17, 16, 9, 10);
        pentagon(2, 14, 15, 6, 19);
        pentagon(2, 13, 12, 4, 14);
        pentagon(2, 19, 18, 3, 13);
        pentagon(3, 0, 5, 12, 13);
        pentagon(6, 15, 8, 10, 11);
        pentagon(4, 17, 8, 15, 14);
        pentagon(4, 12, 5, 16, 17);

        for(int i = 0; i < dodecahedron.size(); i++){
            verts[i] = dodecahedron.get(i);
        }
    }

    void pentagon(int a, int b, int c, int d, int e){
        dodecahedron.add(dodec[a][0]);
        dodecahedron.add(dodec[a][1]);
        dodecahedron.add(dodec[a][2]);

        dodecahedron.add(dodec[b][0]);
        dodecahedron.add(dodec[b][1]);
        dodecahedron.add(dodec[b][2]);

        dodecahedron.add(dodec[c][0]);
        dodecahedron.add(dodec[c][1]);
        dodecahedron.add(dodec[c][2]);

        dodecahedron.add(dodec[d][0]);
        dodecahedron.add(dodec[d][1]);
        dodecahedron.add(dodec[d][2]);

        dodecahedron.add(dodec[e][0]);
        dodecahedron.add(dodec[e][1]);
        dodecahedron.add(dodec[e][2]);
    }

    float[] getVerts(){
        return verts;
    }
}
