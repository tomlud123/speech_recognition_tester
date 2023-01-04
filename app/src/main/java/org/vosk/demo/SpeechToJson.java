package org.vosk.demo;

public class SpeechToJson {

    public String getJsonString(String spokenText) {
        String type = "";
        String content = "";
        String time = "";
        String interval = "";
        String quantity = "";

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
        } else if (spokenText.contains("show temperature")) {
            type = "REQUEST_DATA";
            content = "temperature";
        } else if (spokenText.contains("show blood pressure")) {
            type = "REQUEST_DATA";
            content = "blood pressure";
        } else if (spokenText.contains("set note")) {
            type = "PROTOCOL";
            content = spokenText.substring(spokenText.indexOf("set note") + 9, spokenText.indexOf("end note"));
        } else if (spokenText.contains("set medication")) {
            type = "MEDICATION";
            content = spokenText.substring(spokenText.indexOf("set medication") + 14, spokenText.indexOf("end medication"));
            if (spokenText.contains("at")) {
                time = spokenText.substring(spokenText.indexOf("at") + 3);
            }
            if (spokenText.contains("millilitres")) {
                quantity = spokenText.substring(spokenText.indexOf("millilitres") - 3, spokenText.indexOf("millilitres"));
            }
        } else if (spokenText.contains("show temperature of last")) {
            type = "REQUEST_DATA";
            content = "temperature";
            interval = spokenText.substring(spokenText.indexOf("last") + 5);
        }

        String jsonString = "{\"type\": \"" + type + "\", \"content\": \"" + content + "\"";
        if (!time.equals("")) {
            jsonString += ", \"time\": \"" + time + "\"";
        }
        if (!interval.equals("")) {
            jsonString += ", \"interval\": \"" + interval + "\"";
        }
        if (!quantity.equals("")) {
            jsonString += ", \"quantity\": \"" + quantity + "\"";
        }
        jsonString += "}";
        return jsonString;
    }

}
