package dgen.utils.parsers.specs.relationships.dependencyFunctions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dgen.utils.parsers.SpecificationException;
import dgen.utils.parsers.specs.ColumnSpec;

@JsonTypeName("functionalDependency")
public class FunctionalDependency implements DependencyFunction {

    @Override
    public FunctionType dependencyName() {
        return FunctionType.FUNCTIONAL_DEPENDENCY;
    }

    @Override
    public void validate(ColumnSpec start, ColumnSpec end) {
        if (start.isHasNull() || end.isHasNull()) {
            throw new SpecificationException("Null values in functional dependency between columnID "
                    + start.getColumnID() + " and " + end.getColumnID());
        }
    }
}