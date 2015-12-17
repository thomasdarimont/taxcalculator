package info.kuechler.bmf.taxcalculator;

import java.util.Map;

public interface CalculationOutput {

    Map<String, Class<?>> getPropertyTypes();

    Map<String, Object> getProperties();

    <T> T getProperty(String name, Class<T> type);

    boolean containsProperty(String name);
}
