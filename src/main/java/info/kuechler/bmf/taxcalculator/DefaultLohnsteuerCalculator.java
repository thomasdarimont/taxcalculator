package info.kuechler.bmf.taxcalculator;

import java.util.HashMap;
import java.util.Map;

public class DefaultLohnsteuerCalculator implements LohnsteuerCalculator {

    private final LohnsteuerCalculationFactory factory;

    public DefaultLohnsteuerCalculator(LohnsteuerCalculationFactory factory) {
        this.factory = factory;
    }

    public DefaultLohnsteuerCalculator() {
        this(new DefaultLohnsteuerCalculationFactory());
    }

    @Override
    public CalculationOutput calculate(String key, Map<String, Object> input) {

        LohnsteuerCalculation calculation = factory.newCalculation(key);

        CalculationContext context = new LohnsteuerCalculationContextAdapter(calculation);
        context.setProperties(input);

        calculation.calculate();

        Map<String, Object> output = collectOutput(context);

        return newCalculationOutput(calculation, context, output);
    }

    private Map<String, Object> collectOutput(CalculationContext context) {
        
        Map<String, Object> output = new HashMap<>();

        for (Map.Entry<String, Class<?>> entry : context.getOutputPropertyTypes().entrySet()) {
            output.put(entry.getKey(), context.getProperty(entry.getKey(), entry.getValue()));
        }
        
        return output;
    }

    protected CalculationOutput newCalculationOutput(final LohnsteuerCalculation calculation,
            final CalculationContext context, final Map<String, Object> output) {

        return new CalculationOutput() {

            @Override
            public Map<String, Object> getProperties() {
                return output;
            }

            @Override
            public <T> T getProperty(String name, Class<T> type) {

                if (!containsProperty(name)) {
                    return null;
                }

                return context.getProperty(name, type);
            }

            @Override
            public Map<String, Class<?>> getPropertyTypes() {
                return context.getOutputPropertyTypes();
            }

            @Override
            public boolean containsProperty(String name) {
                return calculation.getOutputPropertyNames().contains(name);
            }
        };
    }
}
