import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

class FlowLogParserTest {

    private FlowLogParser parser;

    @BeforeEach
    void setUp() {
        parser = new FlowLogParser();
        setupProtocolMap();
    }

    private void setupProtocolMap() {
        Map<String, String> protocolMap = new HashMap<>();
        protocolMap.put("6", "tcp");
        protocolMap.put("17", "udp");
        parser.setProtocolMap(protocolMap);
    }

    @Test
    void testGetProtocolFromNumber() {
        assertEquals("tcp", parser.getProtocolFromNumber("6"));
        assertEquals("udp", parser.getProtocolFromNumber("17"));
        assertEquals("unknown", parser.getProtocolFromNumber("999"));
    }


}






