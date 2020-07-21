package dgen.utils.schemas.relationships.dependencyFunctions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.schemas.ColumnSchema;

@JsonTypeName("functionalDependency")
public class FunctionalDependency implements DependencyFunction {

    @Override
    public String dependencyName() {
        return "functionalDependency";
    }

    @Override
    public void validate(ColumnSchema start, ColumnSchema end) {
        if (start.isHasNull() || end.isHasNull()) {
            throw new SpecificationException("Null values in functional dependency between columnID "
                    + start.getColumnID() + " and " + end.getColumnID());
        }
    }
}