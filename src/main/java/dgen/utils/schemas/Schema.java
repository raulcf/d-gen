package dgen.utils.schemas;

import dgen.utils.SpecificationException;

public interface Schema {
    void validate() throws SpecificationException;
}
