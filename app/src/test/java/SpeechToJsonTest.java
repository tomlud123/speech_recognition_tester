import static org.junit.Assert.*;

import org.vosk.demo.SpeechToJson;

public class SpeechToJsonTest {

    @org.junit.Test
    public void getJsonString() {

        String spokenTxt = "blablabla.";
        assertEquals("not trigerred",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, blablabla.";
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

        spokenTxt = "OK Glasses, show pulse.";
        assertEquals("{\"type\": \"REQUEST_DATA\",\"content\": \"pulse\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        //Testcase U07.1

        spokenTxt = "OK Glasses, set note patient james does feels ill, end note.";
        assertEquals("{\"type\": \"PROTOCOL\",\"content\": \"patient james does feels ill\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, set note patient james does feels ill.";
        assertEquals("{\"type\": \"PROTOCOL\",\"content\": \"patient james does feels ill\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        //Testcase U07.2/08

        spokenTxt = "OK Glasses, set medication three hundered millilitres fresh water, end medication.";
        assertEquals("{\"type\": \"MEDICATION\",\"content\": \"three hundered millilitres fresh water\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, set medication bla bla.";
        assertEquals("{\"type\": \"MEDICATION\",\"content\": \"bla bla\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        spokenTxt = "OK Glasses, set medication two hundered millilitres fresh water at eleven o'clock, end medication\n.";
        assertEquals("{\"type\": \"MEDICATION\",\"content\": \"two hundered millilitres fresh water\",\"time\": \"eleven o'clock\"}",
                SpeechToJson.getInstance().getJsonString(spokenTxt));

        //U09

        spokenTxt = "OK Glasses, show pulse of last three days.";
        String resp = SpeechToJson.getInstance().getJsonString(spokenTxt);
        assertEquals("{\"type\": \"REQUEST_DATA\",\"content\": \"pulse\",\"interval\": \"three days\"}",
                resp);
    }
}