package main;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilitiesTest {

    @Test
    public void multipleInverseTest() {

        int base = 13;
        BigInteger a;


        for (int i = 1; i < base; i++) {
            a = BigInteger.valueOf(i);
            assertEquals(a.modInverse(BigInteger.valueOf(base)), MathUtilities.multipleInverse(BigInteger.valueOf(base), a));
        }
    }

    @Test
    void computeY ()
    {
        int[] coeff = {9,14,3,10};

        int[][] points = {
                {0, 9},
                {1, 2},
                {2, 10},
                {3, 8},
                {4, 5}
        };

        for (int i = 0; i < points.length; i++) {
            assertEquals(MathUtilities.computeY(points[i][0], coeff, 17), points[i][1]);
        }
    }
}