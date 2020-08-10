# Defining a Database
This document explains how to create a JSON/YAML specification to define the schema of a database
to be generated using d-gen. Examples specifications can be found [here](../example_specifications).

### Overview
This specification represents relational databases using a hierarchical structure. At the highest
level is the database which is composed of a collection of tables. Tables are in turn composed 
of columns. Columns within a table can have relationships with each other, and columns within 
multiple tables may have relationships through primary keys and foreign keys. At the lowest level
of the specification structure are datatypes, which determine the type of data within a column. 

### Datatypes
Datatypes are at the lowest level of the specification hierarchy and define the data within
a column. The current supported datatypes are `int`, `float`, `string`, and `boolean`.

#### `int`:
Optional parameters:
- `defaultValue` (Defaults to randomly generated values): Integer number to give to all values within
a column.
- `minValue` (Defaults to Java Integer min value): Lower bound on randomly generated values.
- `maxValue` (Defaults to Java Integer max value): Upper bound on randomly generated values.
- `distribution` (Defaults to `uniform`): Statistical distribution to use when generating values.
    - `uniform`, `gaussian`
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `float`:
Optional parameters:
- `defaultValue` (Defaults to randomly generated values): Float number to give to all values within
a column.
- `minValue` (Defaults to Java Float min value): Lower bound on randomly generated values.
- `maxValue` (Defaults to Java Float max value): Upper bound on randomly generated values.
- distribution (Defaults to `uniform`): Statistical distribution to use when generating values.
    - `uniform`, `gaussian`
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `string`:
Optional parameters:
- `defaultValue` (Defaults to randomly generated values): String to give to all values within a
column.
- `regexPattern` (Optional): Regex pattern to be used when generating random strings.
- `validChars` (Defaults to alphanumerical values): Character to use when generating strings.
- `minLength` (Defaults to 0): Lower bound on length of randomly generated values.
- `maxLength` (Defaults to Java Integer max value): Upper bound on length of randomly generated 
values.
- distribution (Defaults to `uniform`): Statistical distribution to use when generating values.
    - `uniform`, `gaussian`
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `boolean`:
Optional Parameters:
- `tfRatio` (Defaults to 0.5): Float value representing percentage of outputs that are true.
- `randomSeed` (Defaults to random value): Used to randomly generate values.

### Distributions:
Distributions are used to help determine the frequency of certain generated values. Currently
supported distributions are `uniform`, `gaussian`.

#### `uniform`:
No parameters

#### `gaussian`:
Optional Parameters:
- `mean` (Defaults to 0): Float representing the mean of the distribution.
-  `standardDeviation` (Defaults to 1): Float representing the standard deviation of the
distribution.

### Columns:
Columns are comprised of datatypes. In addition to regular columns, there are also primary key columns
and foreign key columns. Primary keys by default are always unique and can't have null values. Foreign 
keys don't have a datatype since their data comes from a primary key.

All types of columns (except for primary keys) are either "defined" or "general". Defined 
columns (denoted with the prefix `def`) define a single column within a
table. General columns (denoted with the prefix `gen`) are schemas that define multiple 
columns within a table.

**Note: Currently there is no real support for null values**

#### `defColumn`:
Required parameters:
- `columnID`: An integer that uniquely identifies this column within a table.
- `dataType`: A datatype schema that defines the data to put within the column. 

Optional parameters:
- `columnName` (Defaults to randomly generated value): Name to give to column.
- `regexName`: Regex pattern used to generate random columnName.
- `randomName` (Defaults to true): Whether to randomly generate a columnName.
- `unique` (Defaults to false): Whether column values have a unique constraint.
- `hasNull` (defaults to false): Whether column values can have null values.
- `nullFrequency`: Float representing the percentage of values to be null.
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `genColumn`:
Required parameters:
- `dataType`: A datatype schema that defines the data to put within the column.

Optional parameters:
- `numColumns` (Defaults to randomly generated value): Number of columns to generate following 
this schema.
- `minColumns`: Lower bound on randomly generated number of columns.
- `maxColumns` (Default TBD): Upper bound on randomly generated number of columns.
- `columnName` (Defaults to randomly generated value): Name to give to columns.
- `regexName`: Regex pattern used to generate random columnName.
- `randomName` (Defaults to true): Whether to randomly generate a columnName.
- `unique` (Defaults to false): Whether column values have a unique constraint.
- `hasNull` (defaults to false): Whether column values can have null values.
- `nullFrequency`: Float representing the percentage of values to be null.
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `defForeignKey`:
Required parameters:
- `columnID`: An integer that uniquely identifies this column within a table.  

Optional parameters:
- `columnName` (Defaults to randomly generated value): Name to give to column.
- `regexName`: Regex pattern used to generate random columnName.
- `randomName` (Defaults to true): Whether to randomly generate a columnName.
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `genForeignKey`:
Optional parameters:
- `numColumns` (Defaults to randomly generated value): Number of columns to generate following 
this schema.
- `minColumns`: Lower bound on randomly generated number of columns.
- `maxColumns` (Default TBD): Upper bound on randomly generated number of columns.
- `columnName` (Defaults to randomly generated value): Name to give to columns.
- `regexName`: Regex pattern used to generate random columnName.
- `randomName` (Defaults to true): Whether to randomly generate a columnName.
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `primaryKeys`:
Required parameters:
- `columnID`: An integer that uniquely identifies this column within a table.
- `dataType`: A datatype schema that defines the data to put within the column.  

Optional parameters:
- `columnName` (Defaults to randomly generated value): Name to give to column.
- `regexName`: Regex pattern used to generate random columnName.
- `randomName` (Defaults to true): Whether to randomly generate a columnName.
- `randomSeed` (Defaults to random value): Used to randomly generate values.

### Tables
Tables are comprised of columns and contain relationship between those columns.
Tables are also "defined" or "general" with defined tables (`defTables`) representing 
a single table and with general tables (`genTables`) representing multiple.

#### `defTable`:
Required parameters:
- `tableID`: An integer that uniquely identifies this table within a database.
- `numRows`: Number of rows of data within table.
- `columns`: List of column schemas.

Optional parameters:
- `tableName`(Defaults to randomly generated value): Name to give to table.
- `regexName`: Regex pattern used to generate random tableName.
- `randomName` (Defaults to true): Whether to randomly generate a tableName.
- `tableRelationships`: A list of table relationships (explained below).
- `randomSeed` (Defaults to random value): Used to randomly generate values.

#### `genTable`:
Required parameters:
- `columns`: List of column schemas.

Optional parameters:
- `numTables` (Defaults to randomly generated value): Number of tables to generate from this
schema.
- `minTables`: Lower bound on randomly generated number of tables.
- `maxTables` (Default TBD): Upper bound on randomly generated number of tables.
- `numRows` (Defaults to randomly generated value): Number of rows of data within table.
- `minRows`: Lower bound on randomly generated number of rows.
- `maxRows` (Default TBD): Upper bound on randomly generated number of rows.
- `tableName` (Defaults to randomly generated value): Name to give to table.
- `regexName`: Regex pattern used to generate random tableName.
- `randomName` (Defaults to true): Whether to randomly generate a tableName.
- `tableRelationships`: A list of table relationships (explained below).
- `randomSeed` (Defaults to random value): Used to randomly generate values.

### Table Relationships
Table relationships define relationships between columns within the same table. Table relationships
are either "defined" or "general". Defined relationships (`defTableRelationship`) consists of a
mapping of columnIDs and a dependency function, which determines the type of
relationship between columns. General relationships (`genTableRelationship`) consists of the
number of relationships to generate, a dependency function, and a graph schema, which 
determines how the relationships are distributed among columns. 

**Note: Currently columns can't have relationships involving foreign keys.**  
**Note: Currently columns can't be the dependant of different relationships with 
different dependency functions.**  
**Note: Although it's possible to create cyclic relationships, doing so will often result in a
failure unless you get lucky with your random seed.**

#### `defTableRelationship`
Required parameters:
- `dependencyMap`: Mapping between a columnID to a set of columnIDs.
- `dependencyFunction`: Dependency function that defines the relationships between columns in
dependencyMap (explained below).

#### `genTableRelatinoship`
Required parameters:
- `numRelationships`: Number of relationships to create.
- `dependencyFunction`: Dependency function that defines the relationships between columns in
dependencyMap (explained below).
- `graph`: Graph schema that defines how relationships are distributed amongst columns (explained
below).
- `randomSeed` (Defaults to random value): Used to randomly generate values.

### Dependency Functions
Dependency functions determine the kind of relationship between columns.

#### `jaccardSimilarity`
In a Jaccard Similarity, the size of the intersection between the determinant and dependent columns 
is determined by the similarity value, which is a measure of the intersection of the columns
divided by the union. 

**Note: The size of the intersection is affected by the size of the size of the union (e.g. two 
columns with a union of 10 and a similarity of 0.63 can't have an intersection of 6.3 values). 
Therefore the actual size of the generated intersection may very slightly.**  

**Note: Datatypes of the determinant and dependent must be the same.**

Required parameters:
- `similarity`: Float value that's a measure of the intersection between the determinant and
dependent divided by the union (also known as the jaccard index).

#### `functionalDependency`
In a functional dependency, every value within the determinant column uniquely identifies the values
within the dependent column.

No parameters

### Databases
Databases are comprised of tables and relationships between those tables.  

Required Parameters:
- `databaseName`: Name to give to the database.
- `tables`: A list of table specifications.  

Optional Parameters:  
- `databaseRelationships`: A list of database relationships (explained below).
- `randomSeed` (Defaults to random value): Used to generate all other random seeds. 

### Database Relationships
Database relationships detail the relation between primary keys and foreign keys within a 
database. PK-FK relationships are either "general" or "defined". Defined PK-FK relationships
(`defPKFK`) consist of a mapping between the tableID and columnID of a primary key and foreign 
key. General PK-FK relationships (`genPKFK`) consist of the number of PK-FK relationships to 
create as well as a graph schema to define how those relationships are distributed.

#### `defPKFK`
Required parameters:
- `pkfkMapping`: Mapping between the tableID and columnID of a primary key column and a foreign
key column.

**Note: Since YAML and JSON don't support tuples, tableID and columnID pairs are represented like
so: "tableID:columnID" (e.g. "3:2" represents a column with tableID 3 and columnID 2)**

#### `genPKFK`
Required parameters:
- `numRelationships`: Number of PK-FK relationships to generate.
- `graph`: Graph schema that defines how relationships are distributed (explained below).

### Graph Schemas
Graph schemas are used to define how relationships are distributed amongst columns or 
tables.

#### `randomGraph`
Randomly distributes all relationships with equal probability.

No parameters.