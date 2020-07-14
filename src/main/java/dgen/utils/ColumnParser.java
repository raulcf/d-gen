package dgen.utils;

import dgen.utils.schemas.ColumnSchema;
import dgen.utils.schemas.DefColumnSchema;
import dgen.utils.schemas.DefForeignKeySchema;
import dgen.utils.schemas.GenColumnSchema;
import dgen.utils.schemas.GenForeignKeySchema;
import dgen.utils.schemas.PrimaryKeySchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class ColumnParser {
    private List<ColumnSchema> columns = new ArrayList<>();
    /* Mapping of columnIDs to columnSchema objects */
    private Map<Integer, ColumnSchema> columnMap = new HashMap<>();

    /**
     * Parses ColumnSchema type objects (DefColumnSchema, GenColumnSchema, etc.) into a list of ColumnSchema objects.
     * @param column ColumnSchema type object to parse
     */
    public void parse(ColumnSchema column) {
        switch (column.schemaType()) {
            case DEFCOLUMN:
                DefColumnSchema defColumn = (DefColumnSchema) column;
                parseColumn(defColumn);
                break;
            case GENCOLUMN:
                GenColumnSchema genColumn = (GenColumnSchema) column;
                parseGenColumn(genColumn);
                break;
            case DEFFOREIGNKEY:
                DefForeignKeySchema defForeignKey = (DefForeignKeySchema) column;
                parseForeignKey(defForeignKey);
                break;
            case GENFOREIGNKEY:
                GenForeignKeySchema genForeignKey = (GenForeignKeySchema) column;
                parseGenForeignKey(genForeignKey);
                break;
            case PRIMARYKEY:
                PrimaryKeySchema primaryKey = (PrimaryKeySchema) column;
                parsePrimaryKey(primaryKey);
                break;
        }

    }

    /**
     * Checks whether a column has a column name, regex name, or random name.
     * @param column TableSchema object to parse.
     */
    private void parseColumnName(ColumnSchema column) {
        if (column.getColumnName() != null) {
            column.setRandomName(false);
            column.setRegexName(null);
        } else column.setRandomName(column.getRegexName() == null);
    }

    /**
     * Checks whether a columnID already exists in a database and throws an error if it is.
     * @param columnID tableID to check.
     */
    private void checkColumnID(int columnID) {
        if (columnMap.containsKey(columnID)) {
            throw new SpecificationException("columnID " + columnID + " already exists");
        }
    }

    /**
     * Generates a new columnID that doesn't already exist.
     * @param r Random object used to generate columnIDs.
     * @return Unique columnID.
     */
    private int generateColumnID(Random r) {
        Set<Integer> columnIDs = columnMap.keySet();

        if (columnIDs.size() == Integer.MAX_VALUE) {
            throw new SpecificationException("Max number of columns reached");
        }

        int columnID = r.nextInt();
        while (columnIDs.contains(columnID)) { columnID = r.nextInt(); }
        return columnID;
    }

    /**
     * Parses a GenColumnSchema object into a list of DefColumnSchema objects.
     * @param genColumn GenColumnSchema object to parse.
     */
    private void parseGenColumn(GenColumnSchema genColumn) {
        genColumn.validate();
        Random r = new Random();

        int numColumns;

        if (genColumn.getNumColumns() != null) {
            numColumns = genColumn.getNumColumns();
        } else {
            int minColumns = genColumn.getMinColumns();
            int maxColumns = genColumn.getMaxColumns();

            numColumns = r.nextInt(maxColumns - minColumns + 1) + maxColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = generateColumnID(r);

            DefColumnSchema defColumn = new DefColumnSchema();
            defColumn.setRandomName(genColumn.isRandomName());
            defColumn.setRegexName(genColumn.getRegexName());
            defColumn.setColumnName(genColumn.getColumnName());
            defColumn.setDataType(genColumn.getDataType());
            defColumn.setColumnID(columnId);

            parseColumn(defColumn);
        }

    }

    /**
     * Parses the name and datatype of a DefColumnSchema object and adds it to the columns list.
     * @param defColumn DefColumnSchema object to parse.
     */
    private void parseColumn(DefColumnSchema defColumn) {
        checkColumnID(defColumn.getColumnID());
        parseColumnName(defColumn);

        DataTypeParser parser = new DataTypeParser();
        parser.parse(defColumn.getDataType());
        defColumn.setDataType(parser.getDataType());

        defColumn.validate();

        columnMap.put(defColumn.getColumnID(), defColumn);
        columns.add(defColumn);
    }

    /**
     * Parses a GenForeignKeySchema object into a list of DefForeignKeySchema objects.
     * @param genForeignKey GenForeignKeySchema object to parse.
     */
    private void parseGenForeignKey(GenForeignKeySchema genForeignKey) {
        genForeignKey.validate();
        Random r = new Random();
        int numColumns;

        if (genForeignKey.getNumColumns() != null) {
            numColumns = genForeignKey.getNumColumns();
            genForeignKey.setMinColumns(null);
            genForeignKey.setMaxColumns(null);
        } else {
            int minColumns = genForeignKey.getMinColumns();
            int maxColumns = genForeignKey.getMaxColumns();

            numColumns = r.nextInt(maxColumns - minColumns + 1) + maxColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = generateColumnID(r);

            DefForeignKeySchema defForeignKey = new DefForeignKeySchema();
            defForeignKey.setRandomName(genForeignKey.isRandomName());
            defForeignKey.setRegexName(genForeignKey.getRegexName());
            defForeignKey.setColumnName(genForeignKey.getColumnName());
            defForeignKey.setColumnID(columnId);

            parseForeignKey(defForeignKey);
        }
    }

    /**
     * Parses a DefForeignKeySchema object and adds it to columns list.
     * @param defForeignKey DefForeignKeySchema object to parse.
     */
    private void parseForeignKey(DefForeignKeySchema defForeignKey) {
        defForeignKey.validate();
        checkColumnID(defForeignKey.getColumnID());
        parseColumnName(defForeignKey);

        columnMap.put(defForeignKey.getColumnID(), defForeignKey);
        columns.add(defForeignKey);
    }

    /**
     * Parses the name and datatype of a PrimaryKeySchema object and adds it to the columns list.
     * @param primaryKey PrimaryKeySchema object to parse.
     */
    private void parsePrimaryKey(PrimaryKeySchema primaryKey) {
        checkColumnID(primaryKey.getColumnID());
        parseColumnName(primaryKey);

        DataTypeParser parser = new DataTypeParser();
        parser.parse(primaryKey.getDataType());
        primaryKey.setDataType(parser.getDataType());

        primaryKey.validate();

        columnMap.put(primaryKey.getColumnID(), primaryKey);
        columns.add(primaryKey);
    }

    public List<ColumnSchema> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnSchema> columns) {
        this.columns = columns;
    }

    public Map<Integer, ColumnSchema> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<Integer, ColumnSchema> columnMap) {
        this.columnMap = columnMap;
    }
}
