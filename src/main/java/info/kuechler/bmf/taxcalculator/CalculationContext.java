package info.kuechler.bmf.taxcalculator;

import java.util.Map;

public interface CalculationContext {

    void setProperties(Map<String, Object> properties);

    Map<String, Object> getProperties();

    void setProperty(String name, Object value);

    <T> T getProperty(String name, Class<T> type);

    boolean containsProperty(String name);

    Map<String, Class<?>> getInputPropertyTypes();

    Map<String, Class<?>> getOutputPropertyTypes();
}
