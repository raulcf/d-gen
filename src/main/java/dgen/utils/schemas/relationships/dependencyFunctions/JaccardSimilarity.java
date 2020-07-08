package dgen.utils.schemas.relationships.dependencyFunctions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.schemas.DefColumnSchema;

@JsonTypeName("jaccardSimilarity")
public class JaccardSimilarity implements DependencyFunction {
    private float similarity = (float) 0.5;

    @Override
    public String dependencyName() { return "jaccardSimilarity"; }

    @Override
    public void validate(DefColumnSchema start, DefColumnSchema end) {
        if (similarity > 1) {
            throw new SpecificationException("Jaccard similarity between columns with columnID " + start.getColumnID()
                    + " and " + end.getColumnID() + " is greater than 1");
        }
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }
}
