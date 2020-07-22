package dgen.utils.specs.relationships.dependencyFunctions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.SpecificationException;
import dgen.utils.specs.ColumnSpec;

@JsonTypeName("functionalDependency")
public class FunctionalDependency implements DependencyFunction {

    @Override
    public String dependencyName() {
        return "functionalDependency";
    }

    @Override
    public void validate(ColumnSpec start, ColumnSpec end) {
        if (start.isHasNull() || end.isHasNull()) {
            throw new SpecificationException("Null values in functional dependency between columnID "
                    + start.getColumnID() + " and " + end.getColumnID());
        }
    }
}