package dgen.utils;

import dgen.utils.schemas.*;
import dgen.utils.schemas.DefForeignKeySchema;

import java.util.*;

public class ColumnParser {
    private List<ColumnSchema> columns = new ArrayList<>();
    private Map<Integer, ColumnSchema> columnIDMap = new HashMap<>();

    public List<ColumnSchema> getColumns() {
        return columns;
    }
    public void setColumns(List<ColumnSchema> columns) {
        this.columns = columns;
    }

    public Map<Integer, ColumnSchema> getColumnIDMap() {
        return columnIDMap;
    }
    public void setColumnIDMap(Map<Integer, ColumnSchema> columnIDMap) {
        this.columnIDMap = columnIDMap;
    }

    /**
     * Parses ColumnSchema type objects (DefColumnSchema, GenColumnSchema, etc.) into a list of ColumnSchema objects.
     * @param column ColumnSchema type object to parse
     */
    public void parse(ColumnSchema column) {
        switch (column.schemaType()) {
            case "defColumn":
                DefColumnSchema defColumn = (DefColumnSchema) column;
                parseColumn(defColumn);
                break;
            case "genColumn":
                GenColumnSchema genColumn = (GenColumnSchema) column;
                parseGenColumn(genColumn);
                break;
            case "defForeignKey":
                DefForeignKeySchema defForeignKey = (DefForeignKeySchema) column;
                parseForeignKey(defForeignKey);
                break;
            case "genForeignKey":
                GenForeignKeySchema genForeignKey = (GenForeignKeySchema) column;
                parseGenForeignKey(genForeignKey);
                break;
            case "primaryKey":
                PrimaryKeySchema primaryKey = (PrimaryKeySchema) column;
                parsePrimaryKey(primaryKey);
                break;
        }

    }

    /**
     * Parses a GenColumnSchema object into a list of DefColumnSchema objects.
     * @param genColumn GenColumnSchema object to parse.
     */
    private void parseGenColumn(GenColumnSchema genColumn) {
        Random r = new Random();

        int numColumns;

        genColumn.validate();

        if (genColumn.getNumColumns() != null) {
            numColumns = genColumn.getNumColumns();
        } else {
            int minColumns = genColumn.getMinColumns();
            int maxColumns = genColumn.getMaxColumns();

            numColumns = r.nextInt(maxColumns - minColumns + 1) + maxColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = r.nextInt(); // TODO: Find better way to generate tableID

            DefColumnSchema defColumn = new DefColumnSchema(); // TODO: Find smarter way of copying
            defColumn.setRandomName(genColumn.isRandomName());
            defColumn.setRegexName(genColumn.getRegexName());
            defColumn.setColumnName(genColumn.getColumnName());
            defColumn.setDataType(genColumn.getDataType());
            defColumn.setColumnID(columnId);

            parseColumn(defColumn);
        }

    }

    /**
     * Parses the name and datatype of a DefColumnSchema object.
     * @param defColumn DefColumnSchema object to parse.
     */
    private void parseColumn(DefColumnSchema defColumn) {
        if (defColumn.getColumnName() != null) { // TODO: Find way to reduce code duplication. Might need to modify ColumnSchema interface.
            defColumn.setRandomName(false);
            defColumn.setRegexName(null);
        } else if (defColumn.getRegexName() != null) {
            defColumn.setRandomName(false);
        }

        DataTypeParser parser = new DataTypeParser();
        parser.parse(defColumn.getDataType());
        defColumn.setDataType(parser.getDataType());

        defColumn.validate();

        columnIDMap.put(defColumn.getColumnID(), defColumn);
        columns.add(defColumn);
    }

    /**
     * Parses a GenForeignKeySchema object into a list of DefForeignKeySchema objects.
     * @param genForeignKey GenForeignKeySchema object to parse.
     */
    private void parseGenForeignKey(GenForeignKeySchema genForeignKey) {
        Random r = new Random();

        int numColumns;

        genForeignKey.validate();

        if (genForeignKey.getNumColumns() != null) {
            numColumns = genForeignKey.getNumColumns();
            genForeignKey.setMinColumns(null);
            genForeignKey.setMaxColumns(null);
        } else {
            int minColumns = genForeignKey.getMinColumns();
            int maxColumns = genForeignKey.getMaxColumns();

            assert minColumns <= maxColumns;

            numColumns = r.nextInt(maxColumns - minColumns + 1) + maxColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = r.nextInt(); // TODO: Find better way to generate tableID

            DefForeignKeySchema defForeignKey = new DefForeignKeySchema(); // TODO: Find smarter way of copying
            defForeignKey.setRandomName(genForeignKey.isRandomName());
            defForeignKey.setRegexName(genForeignKey.getRegexName());
            defForeignKey.setColumnName(genForeignKey.getColumnName());
            defForeignKey.setColumnID(columnId);

            parseForeignKey(defForeignKey);
        }
    }

    /**
     * Parses the name of a DefForeignKeySchema object.
     * @param defForeignKey DefForeignKeySchema object to parse.
     */
    private void parseForeignKey(DefForeignKeySchema defForeignKey) {
        if (defForeignKey.getColumnName() != null) {
            defForeignKey.setRandomName(false);
            defForeignKey.setRegexName(null);
        } else if (defForeignKey.getRegexName() != null) {
            defForeignKey.setRandomName(false);
        }

        defForeignKey.validate();

        columnIDMap.put(defForeignKey.getColumnID(), defForeignKey);
        columns.add(defForeignKey);
    }

    /**
     * Parses the name and datatype of a PrimaryKeySchema object.
     * @param primaryKey PrimaryKeySchema object to parse.
     */
    private void parsePrimaryKey(PrimaryKeySchema primaryKey) {
        if (primaryKey.getColumnName() != null) {
            primaryKey.setRandomName(false);
            primaryKey.setRegexName(null);
        } else if (primaryKey.getRegexName() != null) {
            primaryKey.setRandomName(false);
        }

        DataTypeParser parser = new DataTypeParser();
        parser.parse(primaryKey.getDataType());
        primaryKey.setDataType(parser.getDataType());

        primaryKey.validate();

        columnIDMap.put(primaryKey.getColumnID(), primaryKey);
        columns.add(primaryKey);
    }
}
