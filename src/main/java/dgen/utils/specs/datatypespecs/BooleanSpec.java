package dgen.utils.specs.datatypespecs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;

@JsonTypeName("boolean")
public class BooleanSpec implements DataTypeSpec {
    private float tfRatio = (float) 0.5;
    private Long randomSeed;

    @Override
    public DataTypes type() { return DataTypes.BOOLEAN; }

    @Override
    public void validate() {
        if (tfRatio > 1) {
            throw new SpecificationException("Boolean tfRatio of " + java.lang.Float.toString(tfRatio)
                    + " is greater than 1.");
        }
    }

    @Override
    public DataTypeSpec copy() {
        BooleanSpec booleanSpec = new BooleanSpec();
        booleanSpec.setTfRatio(tfRatio);
        booleanSpec.setRandomSeed(randomSeed);

        return booleanSpec;
    }

    public float getTfRatio() {
        return tfRatio;
    }

    public void setTfRatio(float tfRatio) {
        this.tfRatio = tfRatio;
    }

    public Long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }
}
