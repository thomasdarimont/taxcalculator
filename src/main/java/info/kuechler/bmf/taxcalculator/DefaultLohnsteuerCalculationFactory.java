package info.kuechler.bmf.taxcalculator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DefaultLohnsteuerCalculationFactory implements LohnsteuerCalculationFactory {

    private final Map<String, LohnsteuerCalculationFactory> calculationFactories;

    public DefaultLohnsteuerCalculationFactory() {
        this.calculationFactories = initializeCalculationFactories(
                new HashMap<String, LohnsteuerCalculationFactory>());
    }

    public Set<String> getCalculatorFactoryKeys() {
        return Collections.unmodifiableSet(new TreeSet<String>(calculationFactories.keySet()));
    }

    /**
     * Intended to be overridden in sub-classes.
     * 
     * @param calculators
     * @return
     */
    protected <M extends Map<String, LohnsteuerCalculationFactory>> M initializeCalculationFactories(
            M calculators) {

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2006Big();
            }
        }, "2006", "2006*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2007Big();
            }
        }, "2007", "2007*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2008Big();
            }
        }, "2008", "2008*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2009Big();
            }
        }, "2009", "2009*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2010Big();
            }
        }, "2010", "2010*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2011NovemberBig();
            }
        }, "2011November", "201101-201111");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2011DecemberBig();
            }
        }, "2011December", "2011Dezember", "201112");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2012Big();
            }
        }, "2012", "2012*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2013Big();
            }
        }, "2013", "2013*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2014Big();
            }
        }, "2014", "2014*");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2015Big();
            }
        }, "2015", "201501-201511");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2015DezemberBig();
            }
        }, "2015Dezember", "201512");

        registerInto(calculators, new LohnsteuerCalculationFactory() {
            @Override
            public LohnsteuerCalculation newCalculation(String key) {
                return new Lohnsteuer2016Big();
            }
        }, "2016", "2016*");

        return calculators;
    }

    /**
     * Registers the given {@link LohnsteuerCalculationFactory} in the given {@code calculators}
     * {@link Map} with the provided {@code alias}.
     * <p>
     * {@code keyDefinitions} denote aliases or key-patterns for
     * {@link LohnsteuerCalculationFactory} lookups.
     * <p>
     * <ul>
     * <li>A plain alias without (- or *) defines an exact key</li>
     * <li>An alias ending with a (*) star defines the date range for the full year</li>
     * <li>An alias containing a (-) minus defines a date range</li>
     * <ul>
     * 
     * @param calculators
     * @param factory
     * @param keyDefinitions
     */
    protected void registerInto(Map<String, LohnsteuerCalculationFactory> calculators,
            LohnsteuerCalculationFactory factory, String... keyDefinitions) {

        for (String keyDefinition : keyDefinitions) {

            String key = keyDefinition.trim();

            if (tryRegisterForWholeYear(calculators, factory, key)) {
                continue;
            }

            if (tryRegisterForYearMonthRange(calculators, factory, key)) {
                continue;
            }

            calculators.put(key, factory);
        }
    }

    protected boolean tryRegisterForYearMonthRange(
            Map<String, LohnsteuerCalculationFactory> calculators,
            LohnsteuerCalculationFactory factory, String key) {

        if (!key.contains("-")) {
            return false;
        }

        String[] items = key.split("-");

        int year = (Integer.parseInt(items[0]) / 100);

        int start = Integer.parseInt(items[0]) % 100;
        int end = Integer.parseInt(items[1]) % 100;

        for (int i = start; i <= end; i++) {
            calculators.put(String.format("%s%02d", year, i), factory);
        }

        return true;

    }

    protected boolean tryRegisterForWholeYear(
            Map<String, LohnsteuerCalculationFactory> calculators,
            LohnsteuerCalculationFactory factory, String key) {

        if (!key.endsWith("*")) {
            return false;
        }

        key = key.replace("*", "");

        for (int i = 1; i <= 12; i++) {
            calculators.put(String.format("%s%02d", key, i), factory);
        }

        return true;

    }

    @Override
    public LohnsteuerCalculation newCalculation(String key) {
        return tryCreateCalculation(key, tryLookupFactory(key));
    }

    protected LohnsteuerCalculationFactory tryLookupFactory(String key) {

        LohnsteuerCalculationFactory factory = lookupCalculatorFactory(key);

        if (factory == null) {
            return handleFactoryNotFound(key);
        }

        return factory;
    }

    protected LohnsteuerCalculationFactory lookupCalculatorFactory(String key) {
        return calculationFactories.get(key);
    }

    protected LohnsteuerCalculationFactory handleFactoryNotFound(String key) {
        throw new IllegalStateException("Could not find calculator for key: " + key);
    }

    protected LohnsteuerCalculation tryCreateCalculation(String key,
            LohnsteuerCalculationFactory factory) {
        return factory.newCalculation(key);
    }
}
