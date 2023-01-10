package org.vosk.demo;

import java.util.Arrays;
import java.util.List;
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

    //experimental, made with openAI
    public String getJsonString(String spokenText) {
        String type = "";
        String content = "";
        String time = "";
        String interval = "";

        spokenText = phoneticReplacement(spokenText.replace(".", "").replace(",", "")
                .toLowerCase(Locale.ROOT).replace("the ", ""));

        if (!spokenText.contains("okay glasses")){
            return "not trigerred";
        }

        if (spokenText.contains(" stop frame")) {
            type = "GLASSES_COMMAND";
            content = "STOP_FRAME";
        } else if (spokenText.contains(" continue frame")) {
            type = "GLASSES_COMMAND";
            content = "CONTINUE_FRAME";
        } else if (spokenText.contains(" start session")) {
            type = "GLASSES_COMMAND";
            content = "START_SESSION";
        } else if (spokenText.contains(" stop session")) {
            type = "GLASSES_COMMAND";
            content = "STOP_SESSION";
        } else if (spokenText.contains(" show")) {
            type = "REQUEST_DATA";
            if (spokenText.contains(" of last")){
                content = spokenText.substring(spokenText.indexOf("show") + 5, spokenText.indexOf(" of last"));
                interval = spokenText.substring(spokenText.indexOf("last") + 5);
            } else {
                content = spokenText.substring(spokenText.indexOf("show") + 5);
            }
        } else if (spokenText.contains(" set note")) {
            type = "PROTOCOL";
            if (spokenText.contains(" end note")) {
                content = spokenText.substring(spokenText.indexOf("note") + 5, spokenText.indexOf("end note") - 2);
            } else {
                content = spokenText.substring(spokenText.indexOf("note") + 5);
            }
        } else if (spokenText.contains(" set medication")) {
            type = "MEDICATION";
            if (spokenText.contains(" end medication")) {
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

    private String phoneticReplacement(String txt){

        List<String> phoneticOkay = Arrays.asList(" ok ", " oakie ", " okie ", " oh wait ", " oh k ", " okai ", " oh eight ", " oh key ");

        for (String phoneticWord : phoneticOkay) {
            if (txt.contains(phoneticWord)) {
                txt = txt.replace(phoneticWord, " okay ");
            }
        }

        List<String> phoneticGlasses = Arrays.asList(" lasses ", " plaques ", " tasses ", " grasses ", " masques ", " tasks ",
                " gasks ", " casks ", " casques ", " gasses ", " colors ", " masses ", " classes ", " blesses ", " passes ",
                " lashes ", " gashes ", " crashes ", " splashes ", " good says ", " glass ", " places ", " good as his ");

        for (String phoneticWord : phoneticGlasses) {
            if (txt.contains(phoneticWord)) {
                txt = txt.replace(phoneticWord, " glasses ");
            }
        }

        List<String> phoneticFrame = Arrays.asList(" name", " flame", " aim", " claim", " shame", " blame",
                " tame", " game", " same", " came", " framer", " crying", " praying", " free", " fame" , " frank",
                " friend", " fray");

        for (String phoneticWord : phoneticFrame) {
            if (txt.contains(phoneticWord)) {
                txt = txt.replace(phoneticWord, " frame");
            }
        }

        List<String> phoneticSetNote = Arrays.asList(" said no ", " said not ", " said noted ", " said note ", " set noted ",
                " sit not ", " sit out ", " sick note ");

        for (String phoneticWord : phoneticSetNote) {
            if (txt.contains(phoneticWord)) {
                txt = txt.replace(phoneticWord, " set note ");
            }
        }
        return txt;
    }
}
