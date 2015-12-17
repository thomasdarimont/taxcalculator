package info.kuechler.bmf.taxcalculator;

import java.util.Map;

public interface LohnsteuerCalculator {

    CalculationOutput calculate(String key, Map<String, Object> input);
}