import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileWriter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class FlowLogParser {

    // Class-level HashMap to store the lookup table (dstport, protocol) -> tag
    private static Map<String, String> lookupTable = new HashMap<>();

    // HashMap to store protocol number (decimal) to protocol name (keyword)
    private static Map<String, String> protocolMap = new HashMap<>();

    // HashMap to count occurrences of each (port, protocol)
    private Map<String, Integer> portProtocolCounts = new HashMap<>();

    // HashMap to count occurrences of each tag
    private Map<String, Integer> tagCounts = new HashMap<>();


    public static void main(String[] args) {

        FlowLogParser parser = new FlowLogParser();

        // Load the protocol mapping CSV file
        String protocolMapFilePath = "src\\protocol-numbers.csv";
        parser.loadProtocolMap(protocolMapFilePath);

        String lookupTableFilePath = "src\\lookup.txt";
        parser.loadLookupTable(lookupTableFilePath); // You can change this to any other file name

        // Process flow logs with the loaded lookup table and protocol mappings
        String logFilePath = "src\\flow_logs.txt";
        parser.processFlowLogs(logFilePath);

        String outputFilePath = "src\\output.txt";
        parser.writeOutputFile(outputFilePath);
    }

    public void processFlowLogs(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 14) {
                    String dstPort = parts[6];
                    String protocol = getProtocolFromNumber(parts[7]);
                    String key = dstPort + "," + protocol;

                    String tag = lookupTable.getOrDefault(key.toLowerCase(), "Untagged");

                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

                    portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing flow logs: " + e.getMessage());
        }
    }

    // Method to load the protocol mappings from the CSV file
    public void loadProtocolMap(String csvFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;  // to skip the header

            // Read each line of the CSV
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // Skip the first line (header)
                    isFirstLine = false;
                    continue;
                }

                // Split the line by commas
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    String decimal = parts[0].trim();
                    String keyword = parts[1].trim();

                    // Store the decimal (protocol number) -> keyword (protocol name) mapping
                    protocolMap.put(decimal, keyword);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading protocol CSV file: " + e.getMessage());
        }
    }

    // Method to get protocol name from the protocol number
    public String getProtocolFromNumber(String protocolNumber) {
        return protocolMap.getOrDefault(protocolNumber, "unknown");
    }

    public void loadLookupTable(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Read each line, split by comma, and store the data in the HashMap
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    String dstport = parts[0].trim();
                    String protocol = parts[1].trim().toLowerCase(); // Case-insensitive protocol
                    String tag = parts[2].trim();

                    // Store the combination of dstport and protocol as the key, tag as the value
                    lookupTable.put(dstport + "," + protocol, tag);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading lookup file: " + e.getMessage());
        }
    }

    public void writeOutputFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Tag Counts:");
            writer.println("Tag,Count");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }


            writer.println("\nPort/Protocol Combination Counts:");
            writer.println("Port,Protocol,Count");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                String[] parts = entry.getKey().split(",");
                writer.println(parts[0] + "," + parts[1] + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}
