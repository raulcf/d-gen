# d-gen
d-gen is a tool for generating random data that conforms to user-defined relational database schemas.

## Quick Start
As a pre-requisite, d-gen requires Java. All packages and dependencies are handled through a gradle wrapper.

1. Clone this repository:
    ```
    git clone https://github.com/raulcf/d-gen
    cd d-gen
    ```
2. Install dependencies using gradle:
    ```
    ./gradlew build
    ```

Now, the `main` method of `Main.java` can be ran using `./gradlew run`.

## Data Generation Tutorial
1. Create a [specification file](example_specifications/specification_outline.md). Specification files are
how you determine the database schema and datatypes of the data to be generated.

2. Parse the specification file:
    ```
   SpecificationParser specificationParser = new SpecificationParser();
   specificationParser.parseJson("path/to/specification.json");
    ```
   Parsing a specification creates a low-level spec that contains all the details omitted in your specification.
   This low-level spec can be written as a JSON (although it isn't necessary to do so):
    ```
   specificationParser.write("path/to/write.json");
    ```

3. Create the dataset generator:
    ```
   DatasetGenerator datasetGenerator = DatasetConfig.specToGenerator(specificationParser.getDatabase());
    ```

4. Generate the dataset:
    ```
   Dataset dataset = datasetGenerator.generate();
    ``` 

5. Serialize the dataset to CSV, Postgres, or Parquet:
    ```
   CSVSerializer csvSerializer = new CSVSerializer(dataset);
   csvSerializer.serialize("path/to/directory");
   
   PostgresSerializer postgresSerializer = new PostgresSerializer(database);
   postgresSerializer.serialize("path/to/script");
   
   ParquetSerializer parquetSerializer = new ParquetSerializer(database);
   parquetSerializer.serialize("path/to/directory");
    ```


 