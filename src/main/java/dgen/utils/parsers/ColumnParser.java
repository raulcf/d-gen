package dgen.utils.parsers;

import dgen.utils.parsers.specs.*;

import java.util.*;


public class ColumnParser {
    private List<ColumnSpec> columns = new ArrayList<>();
    /* Mapping of columnIDs to columnSchema objects */
    private Map<Integer, ColumnSpec> columnMap = new HashMap<>();

    private RandomGenerator rnd;

    public ColumnParser(RandomGenerator rnd) { this.rnd = rnd; }

    /**
     * Parses ColumnSchema type objects (DefColumnSchema, GenColumnSchema, etc.) into a list of ColumnSchema objects.
     * @param column ColumnSchema type object to parse
     */
    public void parse(ColumnSpec column) {
        switch (column.specType()) {
            case DEFCOLUMN:
                DefColumnSpec defColumn = (DefColumnSpec) column;
                parseColumn(defColumn);
                break;
            case GENCOLUMN:
                GenColumnSpec genColumn = (GenColumnSpec) column;
                parseGenColumn(genColumn);
                break;
            case DEFFOREIGNKEY:
                DefForeignKeySpec defForeignKey = (DefForeignKeySpec) column;
                parseForeignKey(defForeignKey);
                break;
            case GENFOREIGNKEY:
                GenForeignKeySpec genForeignKey = (GenForeignKeySpec) column;
                parseGenForeignKey(genForeignKey);
                break;
            case PRIMARYKEY:
                PrimaryKeySpec primaryKey = (PrimaryKeySpec) column;
                parsePrimaryKey(primaryKey);
                break;
        }

    }

    /**
     * Checks whether a column has a column name, regex name, or random name.
     * @param column TableSchema object to parse.
     */
    private void parseColumnName(ColumnSpec column) {
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
     * @return Unique columnID.
     */
    private int generateColumnID() {
        Set<Integer> columnIDs = columnMap.keySet();

        if (columnIDs.size() == Integer.MAX_VALUE) {
            throw new SpecificationException("Max number of columns reached");
        }

        int columnID = rnd.nextInt();
        while (columnIDs.contains(columnID)) { columnID = rnd.nextInt(); }
        return columnID;
    }

    /**
     * Parses a GenColumnSchema object into a list of DefColumnSchema objects.
     * @param genColumn GenColumnSchema object to parse.
     */
    private void parseGenColumn(GenColumnSpec genColumn) {
        genColumn.validate();

        int numColumns;

        if (genColumn.getNumColumns() != null) {
            numColumns = genColumn.getNumColumns();
        } else {
            int minColumns = genColumn.getMinColumns();
            int maxColumns = genColumn.getMaxColumns();

            numColumns = rnd.nextInt(maxColumns - minColumns) + minColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = generateColumnID();

            DefColumnSpec defColumn = new DefColumnSpec();
            defColumn.setRandomName(genColumn.isRandomName());
            defColumn.setRegexName(genColumn.getRegexName());
            defColumn.setColumnName(genColumn.getColumnName());
            defColumn.setDataTypeSpec(genColumn.getDataTypeSpec().copy());
            defColumn.setColumnID(columnId);

            parseColumn(defColumn);
        }


    }

    /**
     * Parses the name and datatype of a DefColumnSchema object and adds it to the columns list.
     * @param defColumn DefColumnSchema object to parse.
     */
    private void parseColumn(DefColumnSpec defColumn) {
        checkColumnID(defColumn.getColumnID());
        parseColumnName(defColumn);

        if (defColumn.getRandomSeed() == null) {
            defColumn.setRandomSeed(rnd.nextLong());
        }

        DataTypeParser parser = new DataTypeParser(defColumn.getDataTypeSpec(), rnd);
        parser.parse();
        defColumn.setDataTypeSpec(parser.getDataTypeSpec());

        defColumn.validate();

        columnMap.put(defColumn.getColumnID(), defColumn);
        columns.add(defColumn);
    }

    /**
     * Parses a GenForeignKeySchema object into a list of DefForeignKeySchema objects.
     * @param genForeignKey GenForeignKeySchema object to parse.
     */
    private void parseGenForeignKey(GenForeignKeySpec genForeignKey) {
        genForeignKey.validate();
        int numColumns;

        if (genForeignKey.getNumColumns() != null) {
            numColumns = genForeignKey.getNumColumns();
            genForeignKey.setMinColumns(null);
            genForeignKey.setMaxColumns(null);
        } else {
            int minColumns = genForeignKey.getMinColumns();
            int maxColumns = genForeignKey.getMaxColumns();

            numColumns = rnd.nextInt(maxColumns - minColumns) + maxColumns;
        }

        for (int i = 0; i < numColumns; i++) {
            int columnId = generateColumnID();

            DefForeignKeySpec defForeignKey = new DefForeignKeySpec();
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
    private void parseForeignKey(DefForeignKeySpec defForeignKey) {
        defForeignKey.validate();
        checkColumnID(defForeignKey.getColumnID());
        parseColumnName(defForeignKey);

        if (defForeignKey.getRandomSeed() == null) {
            defForeignKey.setRandomSeed(rnd.nextLong());
        }

        columnMap.put(defForeignKey.getColumnID(), defForeignKey);
        columns.add(defForeignKey);
    }

    /**
     * Parses the name and datatype of a PrimaryKeySchema object and adds it to the columns list.
     * @param primaryKey PrimaryKeySchema object to parse.
     */
    private void parsePrimaryKey(PrimaryKeySpec primaryKey) {
        checkColumnID(primaryKey.getColumnID());
        parseColumnName(primaryKey);

        if (primaryKey.getRandomSeed() == null) {
            primaryKey.setRandomSeed(rnd.nextLong());
        }

        DataTypeParser parser = new DataTypeParser(primaryKey.getDataTypeSpec(), rnd);
        parser.parse();
        primaryKey.setDataTypeSpec(parser.getDataTypeSpec());

        primaryKey.validate();

        columnMap.put(primaryKey.getColumnID(), primaryKey);
        columns.add(primaryKey);
    }

    public List<ColumnSpec> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnSpec> columns) {
        this.columns = columns;
    }

    public Map<Integer, ColumnSpec> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<Integer, ColumnSpec> columnMap) {
        this.columnMap = columnMap;
    }
}
