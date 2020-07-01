package dgen.utils.schemas;

import java.util.*;

public class table {
    public int tableID;
    public int numRows;
    public List<columnSchema> columnSchemas = new ArrayList<>();
    public String tableName;
    public boolean randomName = true;

    /**
     * Converts any general column schemas into defined column schemas.
     *
     * @return
     */
    public List<columnSchema> definedColumns() {
        List<columnSchema> newColumnSchemas = new ArrayList<>();

        for (columnSchema c: columnSchemas) {
            if (c instanceof genColumn) {
                genColumn g = (genColumn) c;
                if (g.numColumns >= 0) {
                    for (int i = 0; i < g.numColumns; i++) {
                        newColumnSchemas.add(c);
                    }
                }
                else if (g.minColumns <= g.maxColumns) {
                    Random r = new Random();
                    int numColumns = r.nextInt(g.maxColumns - g.minColumns) + g.minColumns + 1;

                    for (int i = 0; i < numColumns; i++) {
                        newColumnSchemas.add(c);
                    }
                }
                else {
                    ; // TODO: Throw an error
                }
            }
            else {
                newColumnSchemas.add(c);
            }
        }

        return newColumnSchemas;
    }
}
