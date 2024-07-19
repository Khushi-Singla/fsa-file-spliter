# File Splitter

File Splitter is a Java-based application that splits large CSV or Excel files into smaller, manageable batches. This utility is particularly useful for processing large datasets in environments with limited memory or storage capacity.

## Features

- **Support for Multiple Formats**: Handles CSV and Excel file formats.
- **Batch Processing**: Splits files into configurable batch sizes, measured in megabytes.

## Getting Started

### Prerequisites

- Java 8 or later.
- Maven for dependency management and building the project.

### Installation

1. Clone the repository to your local machine.
   ```sh
   git clone https://github.com/yourusername/file-splitter.git

1. Navigate to the project directory.<pre>cd file-splitter </pre>
2. Use Maven to compile and package the application.<pre>mvn package </pre>

### Usage
To use File Splitter, run the following command from the terminal, replacing path/to/your/file with the actual path to the file you wish to split:
<pre>java -jar target/filesplitter-1.0-SNAPSHOT.jar path/to/your/file</pre>
