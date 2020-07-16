package dgen.utils.schemas.relationships.dependencyFunctions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DefColumnSchema;

@JsonTypeName("jaccardSimilarity")
public class JaccardSimilarity implements DependencyFunction {
    private float similarity = (float) 0.5;

    @Override
    public String dependencyName() { return "jaccardSimilarity"; }

    @Override
    public void validate(ColumnSchema start, ColumnSchema end) {
        if (similarity > 1) {
            throw new SpecificationException("Jaccard similarity between columns with columnID " + start.getColumnID()
                    + " and " + end.getColumnID() + " is greater than 1");
        }
        if (start.getDataType().type() == end.getDataType().type()) {
            /**
             * TODO: Add more type checking to make sure that things like the range of random values for ints match.
             */
        } else {
            throw new SpecificationException("Types of " + start.getDataType().type() + " and "
                    + end.getDataType().type() + " not compatible for Jaccard similarity");
        }
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }
}
