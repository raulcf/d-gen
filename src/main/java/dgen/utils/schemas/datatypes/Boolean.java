package dgen.utils.schemas.datatypes;

public class Boolean implements DataType {
    private float tfRatio = (float) 0.5; // TODO: To be decided later

    public Type type() { return Type.BOOLEAN; }

    public float getTfRatio() {
        return tfRatio;
    }

    public void setTfRatio(float tfRatio) {
        this.tfRatio = tfRatio;
    }
}
