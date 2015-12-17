package info.kuechler.bmf.taxcalculator;

import java.util.Set;

public interface LohnsteuerCalculation {

    Set<String> getInputPropertyNames();

    Set<String> getOutputPropertyNames();

    void calculate();
}
