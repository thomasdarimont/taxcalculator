package info.kuechler.bmf.taxcalculator;

import static java.util.Collections.unmodifiableMap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LohnsteuerCalculationContextAdapter implements CalculationContext {

    private static final Set<String> PROPERTY_BLACKLIST = new HashSet<String>(
            Arrays.asList("inputPropertyTypes", "outputPropertyTypes", "properties"));

    private final LohnsteuerCalculation delegate;

    private final Map<String, PropertyDescriptor> pds;

    private final Map<String, Class<?>> inputPropertyTypes;

    private final Map<String, Class<?>> outputPropertyTypes;

    public LohnsteuerCalculationContextAdapter(LohnsteuerCalculation delegate) {

        try {
            this.pds = initPropertyDescriptors(delegate);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        this.delegate = delegate;
        this.inputPropertyTypes = unmodifiableMap(initInputPropertyTypes());
        this.outputPropertyTypes = unmodifiableMap(initOutputPropertyTypes());
    }

    private Map<String, Class<?>> initOutputPropertyTypes() {

        Map<String, Class<?>> outputProperties = new HashMap<>();
        for (String name : delegate.getOutputPropertyNames()) {
            outputProperties.put(name, pds.get(name).getPropertyType());
        }

        return outputProperties;
    }

    private Map<String, Class<?>> initInputPropertyTypes() {

        Map<String, Class<?>> inputProperties = new HashMap<>();
        for (String name : delegate.getInputPropertyNames()) {
            inputProperties.put(name, pds.get(name.toUpperCase()).getPropertyType());
        }

        return inputProperties;
    }

    public Map<String, Class<?>> getOutputPropertyTypes() {
        return outputPropertyTypes;
    }

    public Map<String, Class<?>> getInputPropertyTypes() {
        return inputPropertyTypes;
    }

    private Map<String, PropertyDescriptor> initPropertyDescriptors(
            LohnsteuerCalculation delegate) throws IntrospectionException {

        BeanInfo beanInfo = Introspector.getBeanInfo(delegate.getClass(), Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        Map<String, PropertyDescriptor> cache = new HashMap<>(propertyDescriptors.length);

        for (PropertyDescriptor pd : propertyDescriptors) {

            if (PROPERTY_BLACKLIST.contains(pd.getName())) {
                continue;
            }

            cache.put(pd.getName().toUpperCase(), pd);
        }

        return cache;
    }

    @Override
    public void setProperty(String name, Object value) {

        checkNotNull(name, "name");

        PropertyDescriptor pd = pds.get(name);
        if (pd == null) {
            return;
        }

        trySetValue(value, pd);
    }

    private void trySetValue(Object value, PropertyDescriptor pd) {

        try {
            pd.getWriteMethod().invoke(delegate, value);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ignore) {
            return;
        }
    }

    @Override
    public <T> T getProperty(String name, Class<T> type) {

        checkNotNull(name, "name");
        checkNotNull(type, "type");

        PropertyDescriptor pd = pds.get(name);
        if (pd == null) {
            return null;
        }

        return type.cast(tryReadValue(pd));
    }

    private Object tryReadValue(PropertyDescriptor pd) {
        try {
            return pd.getReadMethod().invoke(delegate);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ignored) {
            return null;
        }
    }

    @Override
    public boolean containsProperty(String name) {

        checkNotNull(name, "name");

        return pds.containsKey(name);
    }

    public void setProperties(Map<String, Object> properties) {

        checkNotNull(properties, "properties");

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            setProperty(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map<String, Object> getProperties() {

        Map<String, Object> properties = new HashMap<>();

        for (String propertyName : delegate.getInputPropertyNames()) {
            properties.put(propertyName, getProperty(propertyName, Object.class));
        }

        for (String propertyName : delegate.getOutputPropertyNames()) {
            properties.put(propertyName, getProperty(propertyName, Object.class));
        }

        return properties;
    }

    private <T> T checkNotNull(T value, String paramName) {

        if (value == null) {
            throw new IllegalArgumentException(paramName + " must not be null!");
        }

        return value;
    }
}
