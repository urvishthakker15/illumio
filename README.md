# Flow Log Parser

## Overview

This project provides a tool for parsing flow log data and mapping it to tags based on a lookup table. The program reads flow logs, extracts relevant information, and matches it against a provided lookup table to generate summary statistics.

## Assumptions

1. **Log Format**: The program only supports the default log format (version 2) as specified in the [AWS documentation](https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html). Custom log formats are not supported.
2. **Lookup Table**: The lookup table must be in plain text (.txt) format with three columns: `dstport`, `protocol`, and `tag`. The `protocol` column must contain protocol names in lowercase (e.g., `tcp`, `udp`, `icmp`).
3. **Protocol Mapping**: The protocol numbers and their corresponding names are loaded from a CSV file with the header `Decimal,Keyword,Protocol,IPv6 Extension Header,Reference`. This CSV should contain protocol numbers and names in the appropriate columns.
4. **Get Protocol from Decimal**: We assume the mapping provided [IANA Assignments](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml) are accurate and valid for this scenario.
## Dependencies

- **Java**: The project is implemented in Java 11 or later.
- **Junit**: The project is tested using Junit5.

## Instructions

### Compiling and Running the Program

1. **Clone the Repository**:
```bash
git clone https://github.com/urvishthakker15/illumio.git
cd illumio
```

2. **Compile the Project**:
```bash
javac -d out/production/illumio src/FlowLogParser.java
```

3. **Run the Program**:
```bash
java -cp out/production/illumio FlowLogParser
```

### Testing

1. **Run Tests**:
- Ensure JUnit 5 is in your classpath.
- Run the tests using:
```bash
java -cp "out/production/illumio:out/test/illumio:lib/junit-platform-console-standalone-1.8.2.jar" org.junit.platform.console.ConsoleLauncher --scan-classpath
```

### Configuration

Before running the program, ensure the following files are in the correct locations:
- `src/protocol-numbers.csv`: CSV file containing protocol number mappings
- `src/lookup.txt`: Lookup table for port and protocol to tag mapping
- `src/flow_logs.txt`: Input flow logs to be processed

The program will generate an `output.txt` file in the `src` directory with the results.

### What Would I Have Done With More Time

1. **Enhanced Error Handling**: Implement additional error handling to manage invalid input formats, missing files, and other edge cases more gracefully.
2. **Testing**: I would have added more robust error handling, and covered more edge cases. Also, tested program more comprehensively by testing all functionality (business logic).
3. **Extended Log Format Support**: Implement support for additional log formats or versions if needed.
3. **Optimization**: Optimize the performance of the parsing and processing logic to handle larger files more efficiently.
4. **User Interface**: Develop a user interface (CLI or graphical) to make it easier to interact with the tool and visualize the results.
5. **Documentation**: Add more comprehensive documentation, including detailed descriptions of the configuration options and usage examples.


