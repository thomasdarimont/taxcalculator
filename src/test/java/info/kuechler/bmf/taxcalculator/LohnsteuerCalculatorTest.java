package info.kuechler.bmf.taxcalculator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LohnsteuerCalculatorTest {

    LohnsteuerCalculator calculator;

    @Before
    public void setup() {
        calculator = new DefaultLohnsteuerCalculator();
    }

    @Test
    public void testCalculateWithInputParameters() throws Exception {

        Map<String, Object> input = new HashMap<>();
        input.put("LZZ", 2); // monthly payment
        input.put("STKL", 1); // tax class
        input.put("RE4", new BigDecimal("223456")); // income in cent
        input.put("LZZFREIB", BigDecimal.ZERO); // Freibeträge
        input.put("PVS", 0); // not in saxony
        input.put("PVZ", 0); // Additional care insurance for employee: birth > 1940, older
                             // than 23, no kids
        input.put("R", 0); // no church
        input.put("ZKF", new BigDecimal("0.5")); // a half child :)
        input.put("KVZ", new BigDecimal("0.90")); // additional med insurance [percent]
        input.put("KRV", 1); // pensions fund: east germany

        input.put("VBEZ", BigDecimal.ZERO);
        input.put("LZZHINZU", BigDecimal.ZERO);
        input.put("SONSTB", BigDecimal.ZERO);
        input.put("VKAPA", BigDecimal.ZERO);
        input.put("VMT", BigDecimal.ZERO);

        CalculationOutput output = calculator.calculate("201512", input);
        
        assertThat(output.getProperty("LSTLZZ", BigDecimal.class).divide(new BigDecimal("100")),
                is(new BigDecimal("239.41")));
        assertThat(
                output.getProperty("SOLZLZZ", BigDecimal.class).divide(new BigDecimal("100")),
                is(new BigDecimal("7.79")));
        
        System.out.println(output.getProperties());
        
    }
}
