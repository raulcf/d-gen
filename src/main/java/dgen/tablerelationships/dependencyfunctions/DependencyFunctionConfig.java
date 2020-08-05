package dgen.tablerelationships.dependencyfunctions;

import dgen.tablerelationships.dependencyfunctions.funcdeps.FuncDepConfig;
import dgen.tablerelationships.dependencyfunctions.jaccardsimilarity.JaccardSimilarityConfig;
import dgen.utils.parsers.specs.relationships.dependencyFunctions.DependencyFunction;
import dgen.utils.parsers.specs.relationships.dependencyFunctions.FunctionType;
import dgen.utils.parsers.specs.relationships.dependencyFunctions.FunctionalDependency;
import dgen.utils.parsers.specs.relationships.dependencyFunctions.JaccardSimilarity;

public interface DependencyFunctionConfig {

    static DependencyFunctionConfig specToConfig(DependencyFunction dependencyFunction) {
        switch (dependencyFunction.dependencyName()) {
            case JACCARD_SIMILARITY:
                return JaccardSimilarityConfig.specToConfig((JaccardSimilarity) dependencyFunction);
            case FUNCTIONAL_DEPENDENCY:
                return FuncDepConfig.specToConfig((FunctionalDependency) dependencyFunction);

        }

        return null;
    }


    FunctionType dependencyName();
}
