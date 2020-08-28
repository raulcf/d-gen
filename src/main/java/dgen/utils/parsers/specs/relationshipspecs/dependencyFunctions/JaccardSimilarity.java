package dgen.utils.parsers.specs.relationshipspecs.dependencyFunctions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.specs.ColumnSpec;

@JsonTypeName("jaccardSimilarity")
public class JaccardSimilarity implements DependencyFunction {
    private float similarity = (float) 0.5;

    @Override
    public FunctionType dependencyName() { return FunctionType.JACCARD_SIMILARITY; }

    @Override
    public void validate(ColumnSpec start, ColumnSpec end) {
        if (similarity > 1 || similarity < 0) {
            throw new SpecificationException("Jaccard similarity between columns with columnID " + start.getColumnID()
                    + " and " + end.getColumnID() + " is greater than 1");
        }
        if (start.getDataTypeSpec().type() == end.getDataTypeSpec().type()) {
            /**
             * TODO: Add more type checking to make sure that things like the range of random values for ints match.
             */
        } else {
            throw new SpecificationException("Types of " + start.getDataTypeSpec().type() + " and "
                    + end.getDataTypeSpec().type() + " not compatible for Jaccard similarity");
        }
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }
}
