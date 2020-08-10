package dgen.utils.parsers.specs;

import dgen.utils.parsers.SpecificationException;

public interface Spec {
    void validate() throws SpecificationException;
    SpecType specType();
}
