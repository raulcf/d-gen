package dgen.tables;

import dgen.attributegenerators.AttributeNameGenerator;
import dgen.attributegenerators.DefaultAttributeNameGenerator;
import dgen.attributegenerators.RandomAttributeNameGenerator;
import dgen.attributegenerators.RegexAttributeNameGenerator;
import dgen.column.Column;
import dgen.column.ColumnConfig;
import dgen.column.ColumnGenerator;
import dgen.coreconfig.DGException;
import dgen.datatypes.DataType;
import dgen.tablerelationships.TableRelationshipConfig;
import dgen.tablerelationships.dependencyfunctions.DependencyFunctionConfig;
import dgen.tablerelationships.dependencyfunctions.funcdeps.FuncDepConfig;
import dgen.tablerelationships.dependencyfunctions.funcdeps.FuncDepGenerator;
import dgen.tablerelationships.dependencyfunctions.jaccardsimilarity.JaccardSimilarityConfig;
import dgen.tablerelationships.dependencyfunctions.jaccardsimilarity.JaccardSimilarityGenerator;
import dgen.utils.RandomGenerator;
import dgen.utils.specs.relationships.dependencyFunctions.JaccardSimilarity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A table generator is a collection of column generators
 */
public class TableGenerator {

    private AttributeNameGenerator ang;
    private Map<Integer, ColumnGenerator> columnGeneratorMap;

    private int tableID;
    private int numRecords;
    private RandomGenerator rnd;

    public TableGenerator(AttributeNameGenerator ang, Map<Integer, ColumnGenerator> columnGeneratorMap, int numRecords){
        this.ang = ang;
        this.numRecords = numRecords;
        this.columnGeneratorMap = columnGeneratorMap;
    }

    public TableGenerator(TableConfig tableConfig) {
        tableID = tableConfig.getInt("table.id");
        numRecords = tableConfig.getInt("num.rows");
        rnd = new RandomGenerator(tableConfig.getLong("random.seed"));

        if (tableConfig.getString("table.name") != null) {
            this.ang = new DefaultAttributeNameGenerator(tableConfig.getString("table.name"));
        } else if (tableConfig.getString("regex.name") != null) {
            this.ang = new RegexAttributeNameGenerator(rnd, tableConfig.getString("regex.name"));
        } else if (tableConfig.getBoolean("random.name")) {
            this.ang = new RandomAttributeNameGenerator(rnd);
        }

        Map<Integer, ColumnGenerator> columnGenerators = new HashMap<>();
        for (ColumnConfig columnConfig: (List<ColumnConfig>) tableConfig.getObject("column.configs")) {
            ColumnGenerator columnGenerator = new ColumnGenerator(columnConfig);
            columnGenerators.put(columnGenerator.getColumnID(), columnGenerator);
        }
        this.columnGeneratorMap = columnGenerators;

        List<TableRelationshipConfig> tableRelationshipConfigs = (List<TableRelationshipConfig>) tableConfig.getObject("table.relationships");
        Set<Integer> visitedColumns = new HashSet<>();
        for (TableRelationshipConfig tableRelationshipConfig : tableRelationshipConfigs) {
            Map<Integer, Set<Integer>> mappings = (Map<Integer, Set<Integer>>) tableRelationshipConfig.getObject("mappings");

            for (Integer determinant: mappings.keySet()) {
                ColumnGenerator determinantColumnGenerator = columnGeneratorMap.get(determinant);
                List<DataType> determinantData = determinantColumnGenerator.copy().generateColumn(numRecords).getData();
                Set<Integer> dependants = mappings.get(determinant);

                for (Integer dependant: dependants) {
                    ColumnGenerator dependantColumnGenerator = columnGeneratorMap.get(dependant);

                    if (dependantColumnGenerator.getDtg() == null) {
                        throw new DGException("Unsupported relationship with foreign keys");
                    } else {
                        DependencyFunctionConfig dependencyFunctionConfig = (DependencyFunctionConfig) tableRelationshipConfig.getObject("dependency.function.config");
                        switch (dependencyFunctionConfig.dependencyName()) {
                            case JACCARD_SIMILARITY:
                                JaccardSimilarityConfig jaccardSimilarityConfig = (JaccardSimilarityConfig) dependencyFunctionConfig;
                                if (! JaccardSimilarityGenerator.validate(determinantData,
                                        dependantColumnGenerator.copy(), (JaccardSimilarityConfig) dependencyFunctionConfig,
                                        numRecords)) {

                                    if (visitedColumns.contains(dependant)) {
                                        throw new DGException("Cannot fulfill table relationship");
                                    } else {
                                        dependantColumnGenerator.setDtg(new JaccardSimilarityGenerator(tableRelationshipConfig.getLong("random.seed"),
                                                jaccardSimilarityConfig, dependantColumnGenerator.getDtg().copy(),
                                                determinantData, numRecords));
                                    }
                                }
                                break;
                            case FUNCTIONAL_DEPENDENCY:
                                FuncDepConfig funcDepConfig = (FuncDepConfig) dependencyFunctionConfig;
                                if (!FuncDepGenerator.validate(determinantData, dependantColumnGenerator.copy(),
                                        funcDepConfig, numRecords)) {

                                    if (visitedColumns.contains(dependant)) {
                                        throw new DGException("Cannot fulfill table relationship");
                                    } else {
                                        dependantColumnGenerator.setDtg(new FuncDepGenerator(tableRelationshipConfig.getLong("random.seed"),
                                                funcDepConfig, dependantColumnGenerator.getDtg().copy(),
                                                determinantData, numRecords));
                                    }
                                }
                                break;
                        }
                    }
                }
                visitedColumns.add(determinant);
            }
        }
        for (int i: columnGeneratorMap.keySet()) {
            if (columnGeneratorMap.get(i).getDtg() instanceof FuncDepGenerator) {
                System.out.println(((FuncDepGenerator) columnGeneratorMap.get(i).getDtg()).getDependentValues().stream().map(n -> n.value()).collect(Collectors.toList()));
            }
        }
    }

    // TODO: this should be an iterator that provides either columns or rows, depending on the storage orientation

    public Table generateTable() {

        List<Column> columns = new ArrayList<>();
        for (ColumnGenerator cg : columnGeneratorMap.values()) {
            Column c = cg.generateColumn(this.numRecords);
            columns.add(c);
        }

        String attributeName = ang.generateAttributeName();
        Table t = new Table(attributeName, columns);
        return t;
    }

    public ColumnGenerator getColumnGenerator(int columnID) {
        return columnGeneratorMap.get(columnID);
    }

    public void setColumnGenerator(int columnID, ColumnGenerator columnGenerator) {
        this.columnGeneratorMap.replace(columnID, columnGenerator);
    }

    public int getTableID() {
        return tableID;
    }

    public int getNumRecords() {
        return numRecords;
    }

    public AttributeNameGenerator getAng() {
        return ang;
    }

    public void setAng(AttributeNameGenerator ang) {
        this.ang = ang;
    }

    public Map<Integer, ColumnGenerator> getColumnGeneratorMap() {
        return columnGeneratorMap;
    }

    public void setColumnGeneratorMap(Map<Integer, ColumnGenerator> columnGeneratorMap) {
        this.columnGeneratorMap = columnGeneratorMap;
    }

    @Override
    public String toString() {
        return "TableGenerator{" +
                "tableID=" + tableID +
                ", numRecords=" + numRecords +
                ", ang=" + ang +
                ", columnGeneratorMap=" + columnGeneratorMap +
                '}';
    }
}
