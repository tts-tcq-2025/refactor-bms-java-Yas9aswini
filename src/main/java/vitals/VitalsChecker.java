package vitals;

public interface Vital {
    boolean isWithinRange();
    String getCriticalMessage();
}

//////////////////////////////////////////////////////
public class PulseRateVital implements Vital {
    private final float pulse;
    private static final float MIN = 60;
    private static final float MAX = 100;

    public PulseRateVital(float pulse) {
        this.pulse = pulse;
    }

    @Override
    public boolean isWithinRange() {
        return pulse >= MIN && pulse <= MAX;
    }

    @Override
    public String getCriticalMessage() {
        return "Pulse Rate is out of range!";
    }
}

public class TemperatureVital implements Vital {
    private final float temperature;
    private static final float MIN = 95;
    private static final float MAX = 102;

    public TemperatureVital(float temperature) {
        this.temperature = temperature;
    }

    @Override
    public boolean isWithinRange() {
        return temperature >= MIN && temperature <= MAX;
    }

    @Override
    public String getCriticalMessage() {
        return "Temperature is critical!";
    }
}

public class Spo2Vital implements Vital {
    private final float spo2;
    private static final float MIN = 90;

    public Spo2Vital(float spo2) {
        this.spo2 = spo2;
    }

    @Override
    public boolean isWithinRange() {
        return spo2 >= MIN;
    }

    @Override
    public String getCriticalMessage() {
        return "Oxygen Saturation out of range!";
    }
}

///////////////////////////////////////////////////////////

public interface VitalChecker {
    boolean checkVitals(List<Vital> vitals);
}

public class StandardVitalChecker implements VitalChecker {
    private final VitalNotifier notifier;

    public StandardVitalChecker(VitalNotifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public boolean checkVitals(List<Vital> vitals) {
        boolean allOk = true;
        for (Vital vital : vitals) {
            if (!vital.isWithinRange()) {
                notifier.notifyCritical(vital.getCriticalMessage());
                allOk = false;
            }
        }
        return allOk;
    }
}
////////////////////////////////////////////////////////////

public interface VitalNotifier {
    void notifyCritical(String message);
}

public class ConsoleVitalNotifier implements VitalNotifier {
    @Override
    public void notifyCritical(String message) {
        System.out.println(message);
        animateWarning();
    }

    private void animateWarning() {
        try {
            for (int i = 0; i < 6; i++) {
                System.out.print("\r* ");
                Thread.sleep(1000);
                System.out.print("\r *");
                Thread.sleep(1000);
            }
            System.out.println(); // Ensure cursor moves to new line
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
/////////////////////////////////////////////////////////////

public class VitalsChecker {
    public static void main(String[] args) {
        float temperature = 99.5f;
        float pulseRate = 55f;
        float spo2 = 89f;

        VitalNotifier notifier = new ConsoleVitalNotifier();
        VitalChecker checker = new StandardVitalChecker(notifier);

        boolean result = checker.checkVitals(Arrays.asList(
                new TemperatureVital(temperature),
                new PulseRateVital(pulseRate),
                new Spo2Vital(spo2)
        ));

        System.out.println("Vitals OK: " + result);
    }
}
