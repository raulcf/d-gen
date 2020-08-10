package dgen.parsertests;

import dgen.utils.parsers.DataTypeParser;
import dgen.utils.parsers.RandomGenerator;
import dgen.utils.parsers.specs.datatypespecs.BooleanSpec;
import dgen.utils.parsers.specs.datatypespecs.FloatSpec;
import dgen.utils.parsers.specs.datatypespecs.IntegerSpec;
import dgen.utils.parsers.specs.datatypespecs.StringSpec;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataTypeParserTest {
    DataTypeParser dataTypeParser;
    long randomSeed;

    @Before
    public void setup() {
        randomSeed = 1;
        dataTypeParser = new DataTypeParser(new IntegerSpec(), new RandomGenerator(randomSeed));
    }

    @Test
    public void testParseDataType() {
        // Checking that random seeds are the same
        IntegerSpec integerSpec = new IntegerSpec();
        dataTypeParser.setDataTypeSpec(integerSpec);
        dataTypeParser.parse();
        IntegerSpec parsedIntegerSpec = (IntegerSpec) dataTypeParser.getDataTypeSpec();

        dataTypeParser = new DataTypeParser(new IntegerSpec(), new RandomGenerator(randomSeed));

        IntegerSpec integerSpec1 = new IntegerSpec();
        integerSpec1.setDefaultValue(0);
        dataTypeParser.setDataTypeSpec(integerSpec1);
        dataTypeParser.parse();
        IntegerSpec parsedIntegerSpec1 = (IntegerSpec) dataTypeParser.getDataTypeSpec();

        assertNotNull(parsedIntegerSpec.getRandomSeed());
        assertEquals(parsedIntegerSpec.getRandomSeed(), parsedIntegerSpec1.getRandomSeed());
    }

    @Test
    public void testParseInt() {
        // Checking that max and min values are null
        IntegerSpec integerSpec = new IntegerSpec();
        integerSpec.setDefaultValue(0);
        dataTypeParser.setDataTypeSpec(integerSpec);
        dataTypeParser.parse();
        IntegerSpec parsedIntegerSpec = (IntegerSpec) dataTypeParser.getDataTypeSpec();

        assertNull(parsedIntegerSpec.getMinValue());
        assertNull(parsedIntegerSpec.getMaxValue());
    }

    @Test
    public void testParseFloat() {
        // Checking that max and min values are null
        FloatSpec floatSpec = new FloatSpec();
        floatSpec.setDefaultValue(0f);
        dataTypeParser.setDataTypeSpec(floatSpec);
        dataTypeParser.parse();
        FloatSpec parsedFloatSpec = (FloatSpec) dataTypeParser.getDataTypeSpec();

        assertNull(parsedFloatSpec.getMinValue());
        assertNull(parsedFloatSpec.getMaxValue());
    }

    @Test
    public void testParseString() {
        // Checking that max and min and regex values are null
        StringSpec stringSpec = new StringSpec();
        stringSpec.setDefaultValue("hello world!");
        dataTypeParser.setDataTypeSpec(stringSpec);
        dataTypeParser.parse();
        StringSpec parsedStringSpec = (StringSpec) dataTypeParser.getDataTypeSpec();

        assertNull(parsedStringSpec.getMaxLength());
        assertNull(parsedStringSpec.getMinLength());
        assertNull(parsedStringSpec.getRegexPattern());

        // Checking that max and min values are null
        stringSpec = new StringSpec();
        stringSpec.setRegexPattern("[a-z]{2}");
        dataTypeParser.setDataTypeSpec(stringSpec);
        dataTypeParser.parse();
        parsedStringSpec = (StringSpec) dataTypeParser.getDataTypeSpec();

        assertNull(parsedStringSpec.getMaxLength());
        assertNull(parsedStringSpec.getMinLength());
    }

    @Test
    public void testParseBoolean() {
        BooleanSpec booleanSpec = new BooleanSpec();
        dataTypeParser.setDataTypeSpec(booleanSpec);
        dataTypeParser.parse();
        BooleanSpec parsedBooleanSpec = (BooleanSpec) dataTypeParser.getDataTypeSpec();
    }

}
