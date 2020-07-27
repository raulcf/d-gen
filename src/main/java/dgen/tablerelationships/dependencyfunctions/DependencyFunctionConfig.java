package dgen.tablerelationships.dependencyfunctions;

import dgen.tablerelationships.dependencyfunctions.jaccardsimilarity.JaccardSimilarityConfig;
import dgen.utils.specs.relationships.dependencyFunctions.DependencyFunction;
import dgen.utils.specs.relationships.dependencyFunctions.FunctionType;
import dgen.utils.specs.relationships.dependencyFunctions.JaccardSimilarity;

public interface DependencyFunctionConfig {

    static DependencyFunctionConfig specToConfig(DependencyFunction dependencyFunction) {
        switch (dependencyFunction.dependencyName()) {
            case JACCARD_SIMILARITY:
                return JaccardSimilarityConfig.specToConfig((JaccardSimilarity) dependencyFunction);
        }

        return null;
    }


    FunctionType dependencyName();
}
