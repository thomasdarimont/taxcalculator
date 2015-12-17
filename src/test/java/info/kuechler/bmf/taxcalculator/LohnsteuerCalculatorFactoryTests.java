package info.kuechler.bmf.taxcalculator;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.junit.Test;

public class LohnsteuerCalculatorFactoryTests {

    DefaultLohnsteuerCalculationFactory factory = new DefaultLohnsteuerCalculationFactory();

    @Test
    public void shouldContainGivenFactoryKeys() throws Exception {

        Set<String> keys = factory.getCalculatorFactoryKeys();

        assertThat(keys,
                hasItems("2006" //
                        , "2007" //
                        , "2008" //
                        , "2009" //
                        , "2010" //
                        , "2011November" //
                        , "2011December" //
                        , "2011Dezember" //
                        , "2012" //
                        , "2013" //
                        , "2014" //
                        , "2015" //
                        , "2015Dezember" //
                        , "2016" //
        ));
    }

    private static Date parseDate(String date) throws Exception {
        return new SimpleDateFormat("dd.MM.yyyy").parse("17.12.2015");
    }

    @Test
    public void testCreateKeyFromDate() throws Exception {

        assertThat(CalculationUtils.createKeyFrom(parseDate("17.12.2015")), is("201512"));

        assertThat(CalculationUtils.createKeyFrom(parseDate("3.01.2015")), is("201501"));

        assertThat(CalculationUtils.createKeyFrom(parseDate("27.05.2012")), is("201205"));
    }

    @Test
    public void testCreateCalculatorForDate() throws Exception {

        Date date = parseDate("27.05.2012");

        String key = CalculationUtils.createKeyFrom(date);

        assertThat(factory.newCalculation(key), instanceOf(Lohnsteuer2012Big.class));
    }

    @Test
    public void testCreateCalculatorForDateWithChangeInYear() throws Exception {

        Date untilNovember = parseDate("01.05.2011");
        Date afterNovember = parseDate("01.12.2011");

        String untilNovemberKey = CalculationUtils.createKeyFrom(untilNovember);
        String afterNovemberKey = CalculationUtils.createKeyFrom(afterNovember);

        assertThat(factory.newCalculation(untilNovemberKey),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation(afterNovemberKey),
                instanceOf(Lohnsteuer2011DecemberBig.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2006() {

        LohnsteuerCalculation calc = factory.newCalculation("2006");
        assertThat(calc, instanceOf(Lohnsteuer2006Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2007() {

        LohnsteuerCalculation calc = factory.newCalculation("2007");
        assertThat(calc, instanceOf(Lohnsteuer2007Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2008() {

        LohnsteuerCalculation calc = factory.newCalculation("2008");
        assertThat(calc, instanceOf(Lohnsteuer2008Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2009() {

        LohnsteuerCalculation calc = factory.newCalculation("2009");
        assertThat(calc, instanceOf(Lohnsteuer2009Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2010() {

        LohnsteuerCalculation calc = factory.newCalculation("2010");
        assertThat(calc, instanceOf(Lohnsteuer2010Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2011November() {

        LohnsteuerCalculation calc = factory.newCalculation("2011November");
        assertThat(calc, instanceOf(Lohnsteuer2011NovemberBig.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2011ByYearMonth() {

        assertThat(factory.newCalculation("201101"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201102"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201103"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201104"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201105"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201106"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201107"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201108"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201109"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201110"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201111"),
                instanceOf(Lohnsteuer2011NovemberBig.class));
        assertThat(factory.newCalculation("201112"),
                instanceOf(Lohnsteuer2011DecemberBig.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2011December() {

        LohnsteuerCalculation calc = factory.newCalculation("2011December");
        assertThat(calc, instanceOf(Lohnsteuer2011DecemberBig.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2011DezemberAlias() {

        LohnsteuerCalculation calc = factory.newCalculation("2011Dezember");
        assertThat(calc, instanceOf(Lohnsteuer2011DecemberBig.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2012() {

        LohnsteuerCalculation calc = factory.newCalculation("2012");
        assertThat(calc, instanceOf(Lohnsteuer2012Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2013() {

        LohnsteuerCalculation calc = factory.newCalculation("2013");
        assertThat(calc, instanceOf(Lohnsteuer2013Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2014() {

        LohnsteuerCalculation calc = factory.newCalculation("2014");
        assertThat(calc, instanceOf(Lohnsteuer2014Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2015() {

        LohnsteuerCalculation calc = factory.newCalculation("2015");
        assertThat(calc, instanceOf(Lohnsteuer2015Big.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2015Dezember() {

        LohnsteuerCalculation calc = factory.newCalculation("2015Dezember");
        assertThat(calc, instanceOf(Lohnsteuer2015DezemberBig.class));
    }

    @Test
    public void testCreateCalculatorLohnsteuer2016() {

        LohnsteuerCalculation calc = factory.newCalculation("2016");
        assertThat(calc, instanceOf(Lohnsteuer2016Big.class));
    }
}
