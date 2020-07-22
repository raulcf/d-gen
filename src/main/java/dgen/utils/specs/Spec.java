package dgen.utils.specs;

import dgen.utils.SpecificationException;

public interface Spec {
    void validate() throws SpecificationException;
    SpecType specType();
}
