import static org.junit.Assert.*;

import org.vosk.demo.SpeechToJson;

public class SpeechToJsonTest {

    @org.junit.Test
    public void getJsonString() {
        String spokenTxt = "OK blablabla.";
        assertEquals("{\"type\": \"\",\"content\": \"\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, stop frame.";
        assertEquals("{\"type\": \"GLASSES_COMMAND\",\"content\": \"STOP_FRAME\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, continue frame.";
        assertEquals("{\"type\": \"GLASSES_COMMAND\",\"content\": \"CONTINUE_FRAME\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, start session.";
        assertEquals("{\"type\": \"GLASSES_COMMAND\",\"content\": \"START_SESSION\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, stop session.";
        assertEquals("{\"type\": \"GLASSES_COMMAND\",\"content\": \"STOP_SESSION\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));


        spokenTxt = "OK Glasses, show temperature.";
        assertEquals("{\"type\": \"REQUEST_DATA\",\"content\": \"temperature\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, show pulse."; //fails
        assertEquals("{\"type\": \"REQUEST_DATA\",\"content\": \"pulse\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        //TODO: fix and fill more examples
    }
}