package dgen.parsertests;

import dgen.utils.SpecificationException;
import dgen.utils.specs.datatypespecs.BooleanSpec;
import dgen.utils.specs.datatypespecs.FloatSpec;
import dgen.utils.specs.datatypespecs.IntegerSpec;
import dgen.utils.specs.datatypespecs.StringSpec;
import org.junit.Test;

public class DataTypeValidateTest {

    @Test(expected = SpecificationException.class)
    public void testIntegerValidate() {
        // Check that min value can't be greater than max value
        IntegerSpec integerSpec = new IntegerSpec();
        integerSpec.setMinValue(10);
        integerSpec.setMaxValue(0);
        integerSpec.validate();
    }

    @Test(expected = SpecificationException.class)
    public void testFloatValidate() {
        // Check that min value can't be greater than max value
        FloatSpec floatSpec = new FloatSpec();
        floatSpec.setMinValue(10f);
        floatSpec.setMaxValue(0f);
        floatSpec.validate();
    }

    @Test(expected = SpecificationException.class)
    public void testStringValidate() {
        // Check that min value can't be greater than max value
        StringSpec stringSpec = new StringSpec();
        stringSpec.setMinLength(10);
        stringSpec.setMaxLength(0);
        stringSpec.validate();
    }

    @Test(expected = SpecificationException.class)
    public void testBooleanValidate() {
        // Check that tfRatio <= 1
        BooleanSpec booleanSpec = new BooleanSpec();
        booleanSpec.setTfRatio(1.5f);
        booleanSpec.validate();
    }
}
