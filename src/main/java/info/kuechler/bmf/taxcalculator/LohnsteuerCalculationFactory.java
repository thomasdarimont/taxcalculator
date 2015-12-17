package info.kuechler.bmf.taxcalculator;

public interface LohnsteuerCalculationFactory {

    /**
     * Returns a new Instance of a {@link LohnsteuerCalculation}.
     * 
     * @param key
     * @return
     */
    LohnsteuerCalculation newCalculation(String key);
}
