package main;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilitiesTest {

    @Test
    public void multipleInverseTest() {
        BigInteger a = BigInteger.probablePrime(128,new Random());
        assertEquals(BigInteger.TEN.modInverse(a), MathUtilities.multipleInverse(a, BigInteger.TEN));
    }
}