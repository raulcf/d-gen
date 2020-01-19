package dgen;

import dgen.tables.Table;
import dgen.utils.AdHocTableFactory;

/**
 * This class drives the data generation process, which is divided into 4 stages:
 *
 * 1. Parse the input profile and verify it can be satisfied
 * 2. Configure generators so as to satisfy the input profile
 * 3. Generate a profile of ground truth from the configured dataset
 * 4. Materialize the generation process into a specified output format
 *
 */
public class Main {


    public static void main(String[] args) {
        // Example
        int numColumns = 4;
        int numRecords = 10;
        Table example = AdHocTableFactory.makeAllIntegerTable(numColumns, numRecords);

        String csv = example.toCSV();
        System.out.println(csv);
    }
}
