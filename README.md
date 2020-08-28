# d-gen
d-gen is a tool for generating random data that conforms to user-defined relational database schemas.

d-gen generates databases in 4 steps:
1. d-gen takes a high-level specification file that can contain generalities about the dataset and 
parses it into a low-level specification, removing all ambiguity and generalities.
2. d-gen takes the low-level specification and creates the necessary data generators to create the dataset.
3. d-gen generates the data using those data generators.
4. d-gen serializes the resulting dataset into a standard dataset/database format. 

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

2. Run the `Main` class using Gradle from the command line:
    ```
   gradle run --args="-i SPECIFICATION_PATH -o LOW_LEVEL_SPEC_OUTPUT_PATH"
   ```
   - `SPECIFICATION_PATH` is the path of the specification file that defines the database to generate.
   - `-o LOW_LEVEL_SPEC_OUTPUT_PATH` is an optional parameter that points to the file to write the parsed
   low-level specification to.


 