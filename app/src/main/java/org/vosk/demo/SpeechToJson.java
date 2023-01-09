package org.vosk.demo;

import java.util.Locale;

public class SpeechToJson {

    private static SpeechToJson instance;

    private SpeechToJson() {}

    public static SpeechToJson getInstance() {
        if (instance == null) {
            instance = new SpeechToJson();
        }
        return instance;
    }

    //made by openAI - has bugs
    public String getJsonString(String spokenText) {
        String type = "";
        String content = "";
        String time = "";
        String interval = "";

        spokenText = spokenText.replace(".", "").toLowerCase(Locale.ROOT);

        if (!spokenText.contains("ok glasses")){
            return "";
        }

        if (spokenText.contains("stop frame")) {
            type = "GLASSES_COMMAND";
            content = "STOP_FRAME";
        } else if (spokenText.contains("continue frame")) {
            type = "GLASSES_COMMAND";
            content = "CONTINUE_FRAME";
        } else if (spokenText.contains("start session")) {
            type = "GLASSES_COMMAND";
            content = "START_SESSION";
        } else if (spokenText.contains("stop session")) {
            type = "GLASSES_COMMAND";
            content = "STOP_SESSION";
        } else if (spokenText.contains("show")) {
            type = "REQUEST_DATA";
            if (spokenText.contains(" of last")){
                content = spokenText.substring(spokenText.indexOf("show") + 5, spokenText.indexOf(" of last"));
                interval = spokenText.substring(spokenText.indexOf("last") + 5);
            } else {
                content = spokenText.substring(spokenText.indexOf("show") + 5);
            }
        } else if (spokenText.contains("set note")) {
            type = "PROTOCOL";
            if (spokenText.contains("end note")) {
                content = spokenText.substring(spokenText.indexOf("note") + 5, spokenText.indexOf("end note") - 2);
            } else {
                content = spokenText.substring(spokenText.indexOf("note") + 5);
            }
        } else if (spokenText.contains("set medication")) {
            type = "MEDICATION";
            if (spokenText.contains("end medication")) {
                if (spokenText.contains(" at ")) {
                    content = spokenText.substring(spokenText.indexOf("set medication") + 15, spokenText.indexOf(" at "));
                    time = spokenText.substring(spokenText.indexOf(" at ") + 4, spokenText.indexOf("end medication") - 2);
                } else {
                    content = spokenText.substring(spokenText.indexOf("set medication") + 15, spokenText.indexOf("end medication") - 2);
                }
            } else {
                if (spokenText.contains(" at ")) {
                    content = spokenText.substring(spokenText.indexOf("set medication") + 15, spokenText.indexOf(" at "));
                    time = spokenText.substring(spokenText.indexOf(" at ") + 4);
                } else{
                    content = spokenText.substring(spokenText.indexOf("set medication") + 15);
                }
            }
        }

        String jsonString = "{";
        jsonString += "\"type\": \"" + type + "\",";
        jsonString += "\"content\": \"" + content + "\"";
        if (!time.equals("")) {
            jsonString += ",\"time\": \"" + time + "\"";
        }
        if (!interval.equals("")) {
            jsonString += ",\"interval\": \"" + interval + "\"";
        }
        jsonString += "}";

        return jsonString;
    }

}
