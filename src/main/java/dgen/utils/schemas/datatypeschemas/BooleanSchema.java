package dgen.utils.schemas.datatypeschemas;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;

@JsonTypeName("boolean")
public class BooleanSchema implements DataTypeSchema {
    private float tfRatio = (float) 0.5;

    @Override
    public Type type() { return Type.BOOLEAN; }

    @Override
    public void validate() {
        if (tfRatio > 1) {
            throw new SpecificationException("Boolean tfRatio of " + java.lang.Float.toString(tfRatio)
                    + " is greater than 1.");
        }
    }

    public float getTfRatio() {
        return tfRatio;
    }

    public void setTfRatio(float tfRatio) {
        this.tfRatio = tfRatio;
    }
}
