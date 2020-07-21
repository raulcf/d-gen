# Defining a database to be generated
The following document outlines how to create a JSON or YAML file to specify a database to be 
generated using d-gen. This guide is simply an overview/explanation of the specification, 
examples of the syntax can be found in the example_specifications folder.

### Overview
This specification uses a hierarchical structure. At the highest level is a database, which is 
composed of a collection of tables and primary key foreign key relationships (PK-FK) between 
those tables. Beneath databases are tables, which are composed of different types of columns 
and relationships between those columns. At the lowest level, beneath columns, are datatypes, 
which define the types of data within a column.

### Datatypes
Datatypes are at the most basic level of the specification hierarchy and define the data within
a column. The current supported datatypes are integers, floats, strings, and booleans.

#### int:
Optional parameters:
- defaultValue (Defaults to randomly generated values): Integer number to give to all values.
- minValue (Defaults to Java Integer min value): Lower bound on randomly generated values.
- maxValue (Defaults to Java Integer max value): Upper bound on randomly generated values.
- distribution (Defaults to UNIFORM): Statistical distribution to use when generating values.
    - UNIFORM, GAUSSIAN

#### float:
Optional parameters:
- defaultValue (Defaults to randomly generated values): Float number to give to all values.
- minValue (Defaults to Java Float min value): Lower bound on randomly generated values.
- maxValue (Defaults to Java Float max value): Upper bound on randomly generated values.
- distribution (Defaults to UNIFORM): Statistical distribution to use when generating values.
    - UNIFORM, GAUSSIAN

#### string:
Optional parameters:
- defaultValue (Defaults to randomly generated values): String to give to all values.
- regexPattern: Regex pattern to used to generate random strings.
- validChars (Defaults to alphanumerical values): Character to use when generating strings.
- minLength (Defaults to 0): Lower bound on length of randomly generated values.
- maxLength (Defaults to Java Integer max value): Upper bound on length of randomly generated 
values.
- distribution (Defaults to UNIFORM): Statistical distribution to use when generating values.
    - UNIFORM, GAUSSIAN

#### boolean:
Optional Parameters:
- tfRatio (Defaults to 0.5): Float value representing percentage of outputs that are true.

### Columns:
Columns are above datatypes. There are a few types of columns: primary keys, foreign keys, and
regular columns. Primary keys by default are always unique and can't be null. Foreign keys
don't have a datatype since their data comes from a primary key. Regular columns have a
datatype and many other parameters. 

All types of columns (except for primary keys) are either "defined" or "general". Defined 
columns (denoted with the prefix "def") are schemas that only define a single column within a
table. General columns (denoted with the prefix "gen") are schemas that define multiple 
columns within a table.

#### defColumn:
Required parameters:
- columnID: An integer that uniquely identifies this column within a table.
- dataTypeSchema: A datatype schema that defines the data to put within the column.  

Optional parameters:
- columnName: Name to give to column.
- regexName: Regex pattern used to generate random columnName.
- randomName: Whether to randomly generate a columnName.
- unique (Defaults to false): Whether column values have a unique constraint.
- hasNull (defaults to false): Whether column values can have null values.
- nullFrequency: Float representing the percentage of values to be null.

#### genColumn:
Required parameters:
- dataTypeSchema: A datatype schema that defines the data to put within the column.

Optional parameters:
- numColumns (Defaults to randomly generated value): Number of columns to generate following 
this schema.
- minColumns: Lower bound on randomly generated number of columns.
- maxColumns: Upper bound on randomly generated number of columns.
- columnName: Name to give to columns.
- regexName: Regex pattern used to generate random columnName.
- randomName: Whether to randomly generate a columnName.
- unique (Defaults to false): Whether column values have a unique constraint.
- hasNull (defaults to false): Whether column values can have null values.
- nullFrequency: Float representing the percentage of values to be null.

#### defForeignKey:
Required parameters:
- columnID: An integer that uniquely identifies this column within a table.  

Optional parameters:
- columnName: Name to give to column.
- regexName: Regex pattern used to generate random columnName.
- randomName: Whether to randomly generate a columnName.

#### genForeignKey:
Optional parameters:
- numColumns (Defaults to randomly generated value): Number of columns to generate following 
this schema.
- minColumns: Lower bound on randomly generated number of columns.
- maxColumns: Upper bound on randomly generated number of columns.
- columnName: Name to give to columns.
- regexName: Regex pattern used to generate random columnName.
- randomName: Whether to randomly generate a columnName.

#### primaryKeys:
Required parameters:
- columnID: An integer that uniquely identifies this column within a table.
- dataTypeSchema: A datatype schema that defines the data to put within the column.  

Optional parameters:
- columnName: Name to give to column.
- regexName: Regex pattern used to generate random columnName.
- randomName: Whether to randomly generate a columnName.

### Tables
Tables are above columns and consist of a list of column schemas and a list of table 
relationships. Tables are also "defined" or "general" with defined tables (defTables) 
representing a single table and with general tables (genTables) representing multiple.

#### defTable:
Required parameters:
- tableID: An integer that uniquely identifies this table within a database.
- numRows: Number of rows within table.
- tableSchema: List of column schemas.

Optional parameters:
- tableName: Name to give to table.
- regexName: Regex pattern used to generate random tableName.
- randomName: Whether to randomly generate a tableName.
- tableRelationships: A list of table relationships (explained below).

#### genTable:
Required parameters:
- tableSchema: List of column schemas.

Optional parameters:
- numTables (Defaults to randomly generated value): Number of tables to generate from this
schema.
- minTables: Lower bound on randomly generated number of tables.
- maxTables: Upper bound on randomly generated number of tables.
- numRows (Defaults to randomly generated value): Number of rows within table.
- minRows: Lower bound on randomly generated number of rows.
- maxRows: Upper bound on randomly generated number of rows.
- tableName: Name to give to table.
- regexName: Regex pattern used to generate random tableName.
- randomName: Whether to randomly generate a tableName.
- tableRelationships: A list of table relationships (explained below).

### Table Relationships
Table relationships define relationships between columns within a table. Table relationships
are either "defined" or "general". Defined relationships (defTableRelationship) consists of a
mapping between two columnIDs and a dependency function, which determines the type of
relationship between columns. General relationships (genTableRelationships) consists of the
number of relationships to generate, a dependency function, and a graph schema, which 
determines how the relationships are distributed among columns. 

#### defTableRelationship
Required parameters:
- dependencyMap: Mapping between a columnID to a set of columnIDs.
- dependencyFunction: Dependency function that defines the relationships between columns in
dependencyMap (explained below).

#### genTableRelatinoship
Required parameters:
- numRelationships: Number of relationships to create.
- dependencyFunction: Dependency function that defines the relationships between columns in
dependencyMap (explained below).
- graphSchema: Graph that defines how relationships are distributed (explained below).

### Dependency Functions
Dependency functions determine the kind of relationship between columns.

#### Jaccard Similarity
In a Jaccard Similarity, the determinant and dependent share a certain amount of values, 
determined by the similarity value, which is a measure of the intersection divided by the
union.

Required parameters:
- similarity: Float value that's a measure of the intersection between the determinant and
dependent divided by the union.

### Database
Databases are at the highest level of the hierarchy and are composed of a collection of tables
and relationships between those tables. 

Required Parameters:
- databaseName: Name to give to the database.
- tableSchemas: A list of table specifications.  

Optional Parameters:  
- databaseRelationships: A list of PK-FK relationships.

### Database Relationships
Database relationships detail the relation between primary keys and foreign keys within a 
database. PK-FK relationships are either "general" or "defined". Defined PK-FK relationships
(defPKFK) consist of a mapping between the tableID and columnID of a primary key and foreign 
key. General PK-FK relationships (genPKFK) consist of the number of PK-FK relationships to 
create as well as a graph schema to define how those relationships are distributed.

#### defPKFK
Required parameters:
- pkfkMapping: Mapping between the tableID and columnID of a primary key column and a foreign
key column.
    - NOTE: Since YAML and JSON don't support tuples, this would be represented like so:
    "pkTableID:pkColumnID": "pkTableID: pkColumnID"

#### genPKFK
Required parameters:
- numRelationships: Number of PK-FK relationships to generate.
- graphSchema: Graph that defines how relationships are distributed (explained below).

### Graph Schemas
Graph schemas are used to defines how relationships are distributed amongst columns or 
tables.

#### randomGraph
Randomly distributes all relationships with equal probability.

Required parameters:
None