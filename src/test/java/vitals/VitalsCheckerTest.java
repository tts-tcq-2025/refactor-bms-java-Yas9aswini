package vitals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class VitalsCheckerTest {

   private ByteArrayOutputStream outContent;
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String captureOutput(Runnable testCode) {
        testCode.run();
        return outContent.toString();
    }

    private void assertVitalsFail(float temp, float pulse, float spo2, String... expectedMessages) {
        String output = captureOutput(() -> assertFalse(VitalsChecker.vitalsOk(temp, pulse, spo2)));
        for (String message : expectedMessages) {
            assertTrue(output.contains(message), "Expected message not found: " + message);
        }
    }

    private void assertVitalsPass(float temp, float pulse, float spo2) {
        assertTrue(VitalsChecker.vitalsOk(temp, pulse, spo2));
    }

    // -------------------------------
    //          TEST CASES
    // -------------------------------

    @Test void testAllVitalsValid() throws InterruptedException {
        assertVitalsPass(98f, 80f, 95f);
    }

    @Test void testTemperatureBelowMin() {
        assertVitalsFail(94.9f, 80f, 95f, "Temperature is critical!");
    }

    @Test void testTemperatureAboveMax() {
        assertVitalsFail(102.1f, 80f, 95f, "Temperature is critical!");
    }

    @Test void testPulseBelowMin() {
        assertVitalsFail(98f, 59.9f, 95f, "Pulse Rate is out of range!");
    }

    @Test void testPulseAboveMax() {
        assertVitalsFail(98f, 100.1f, 95f, "Pulse Rate is out of range!");
    }

    @Test void testSpo2BelowMin() {
        assertVitalsFail(98f, 80f, 89.9f, "Oxygen Saturation out of range!");
    }

    @Test void testAllVitalsBelowMin() {
        assertVitalsFail(94f, 59f, 89f, 
            "Temperature is critical!", 
            "Pulse Rate is out of range!", 
            "Oxygen Saturation out of range!");
    }

    @Test void testAllVitalsAtUpperBoundary() {
        assertVitalsPass(102f, 100f, 90f);
    }

    @Test void testAllVitalsAtLowerBoundary() {
        assertVitalsPass(95f, 60f, 90f);
    }

    @Test void testTempBelowPulseAbove() {
        assertVitalsFail(94f, 101.1f, 95f, 
            "Temperature is critical!", 
            "Pulse Rate is out of range!");
    }

    @Test void testMultipleFailures() {
        assertVitalsFail(102.1f, 59f, 89f, 
            "Temperature is critical!", 
            "Pulse Rate is out of range!", 
            "Oxygen Saturation out of range!");
    }

    @Test void testPulseAboveSpo2Below() {
        assertVitalsFail(96f, 101f, 89f, 
            "Pulse Rate is out of range!", 
            "Oxygen Saturation out of range!");
    }

    @Test void testTempMaxPulseMin() {
        assertVitalsFail(102f, 59f, 95f, 
            "Pulse Rate is out of range!");
    }

    @Test void testPrecisionJustInsideLimits() {
        assertVitalsPass(95.01f, 60.01f, 90.01f);
    }

    @Test void testPrecisionJustOutsideLimits() {
        assertVitalsFail(94.99f, 59.99f, 89.99f, 
            "Temperature is critical!", 
            "Pulse Rate is out of range!", 
            "Oxygen Saturation out of range!");
    }
}
