package vitals;

public abstract class VitalsChecker {

  // ... Pure functions from above

  // Consolidated blinking indicator for alert
  private static void blinkWarning() throws InterruptedException {
    for (int i = 0; i < 6; i++) {
      System.out.print("\r* ");
      Thread.sleep(1000);
      System.out.print("\r *");
      Thread.sleep(1000);
    }
  }

  // Main method that checks all vitals and handles warnings
  public static boolean vitalsOk(float temperature, float pulseRate, float spo2) throws InterruptedException {
    if (!isTemperatureOk(temperature)) {
      System.out.println("Temperature is critical!");
      blinkWarning();
      return false;
    }

    if (!isPulseRateOk(pulseRate)) {
      System.out.println("Pulse Rate is out of range!");
      blinkWarning();
      return false;
    }

    if (!isSpo2Ok(spo2)) {
      System.out.println("Oxygen Saturation out of range!");
      blinkWarning();
      return false;
    }

    return true;
  }
}
