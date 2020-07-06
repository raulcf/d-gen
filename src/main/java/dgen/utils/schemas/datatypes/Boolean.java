package dgen.utils.schemas.datatypes;

import dgen.utils.SpecificationException;

public class Boolean implements DataType {
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
