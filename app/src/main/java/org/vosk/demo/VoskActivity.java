// Copyright 2019 Alpha Cephei Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.vosk.demo;

import static org.vosk.demo.SrActivityState.*;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechService;
import org.vosk.android.SpeechStreamService;
import org.vosk.android.StorageService;

import java.io.IOException;
import java.io.InputStream;

public class VoskActivity extends Activity implements
        RecognitionListener, AdapterView.OnItemSelectedListener {

    private String[] MODELS = {"model-en-us", "vosk-model-small-de-0.15"};
//            "vosk-model-en-us-0.22-lgraph", "vosk-model-small-en-us-zamia-0.5"};

    private String selectedModelName = "model-en-us";
    private Model model;
    private SpeechService speechService;
    private SpeechStreamService speechStreamService;
    private TextView resultView;
    private Spinner spin;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_vosk);

        // Setup layout
        resultView = findViewById(R.id.result_text);
        setUiState(STATE_START);

        findViewById(R.id.btnFile).setOnClickListener(view -> recognizeFile());
        findViewById(R.id.btnMic).setOnClickListener(view -> recognizeMicrophone());
        ((ToggleButton) findViewById(R.id.btnPause)).setOnCheckedChangeListener((view, isChecked) -> pause(isChecked));
        this.spin = (Spinner) findViewById(R.id.spinner);
        this.spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, MODELS);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        LibVosk.setLogLevel(LogLevel.INFO);

        initModel();
    }

    private void initModel() {
        StorageService.unpack(this, selectedModelName, "model",
                (model) -> {
                    this.model = model;
                    setUiState(STATE_READY);
                },
                (exception) -> setErrorState("Failed to unpack the model" + exception.getMessage()));
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Recognizer initialization is a time-consuming and it involves IO,
//                // so we execute it in async task
//                initModel();
//            } else {
//                finish();
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (speechService != null) {
            speechService.stop();
            speechService.shutdown();
        }

        if (speechStreamService != null) {
            speechStreamService.stop();
        }
    }

    @Override
    public void onResult(String hypothesis) {
//        resultView.append(hypothesis + "\n");
        String text = hypothesis.substring(hypothesis.indexOf("text")+7, hypothesis.indexOf("}")-1);
        resultView.append("Recognized speech: " + text + "\n");
        resultView.append("JSON output: "+SpeechToJson.getInstance().getJsonString(text) + "\n\n");
    }

    @Override
    public void onFinalResult(String hypothesis) {
        resultView.append(hypothesis + "\n");
        setUiState(STATE_DONE);
        if (speechStreamService != null) {
            speechStreamService = null;
        }
    }

    @Override
    public void onPartialResult(String hypothesis) {
        //resultView.append(hypothesis + "\n");
    }

    @Override
    public void onError(Exception e) {
        setErrorState(e.getMessage());
    }

    @Override
    public void onTimeout() {
        setUiState(STATE_DONE);
    }

    private void setUiState(SrActivityState state) {
        switch (state) {
            case STATE_START:
                resultView.setText(R.string.preparing);
                resultView.setMovementMethod(new ScrollingMovementMethod());
                findViewById(R.id.btnFile).setEnabled(false);
                findViewById(R.id.btnMic).setEnabled(false);
                findViewById(R.id.btnPause).setEnabled((false));
                break;
            case STATE_READY:
                resultView.setText(R.string.ready);
                ((Button) findViewById(R.id.btnMic)).setText(R.string.recognize_microphone);
                findViewById(R.id.btnFile).setEnabled(true);
                findViewById(R.id.btnMic).setEnabled(true);
                findViewById(R.id.btnPause).setEnabled((false));
                break;
            case STATE_DONE:
                ((Button) findViewById(R.id.btnFile)).setText(R.string.recognize_file);
                ((Button) findViewById(R.id.btnMic)).setText(R.string.recognize_microphone);
                findViewById(R.id.btnFile).setEnabled(true);
                findViewById(R.id.btnMic).setEnabled(true);
                findViewById(R.id.btnPause).setEnabled((false));
                findViewById(R.id.spinner).setEnabled((true));
                break;
            case STATE_FILE:
                ((Button) findViewById(R.id.btnFile)).setText(R.string.stop_file);
                resultView.setText(getString(R.string.starting));
                findViewById(R.id.btnMic).setEnabled(false);
                findViewById(R.id.btnFile).setEnabled(true);
                findViewById(R.id.btnPause).setEnabled((false));
                findViewById(R.id.spinner).setEnabled((false));
                break;
            case STATE_MIC:
                ((Button) findViewById(R.id.btnMic)).setText(R.string.stop_microphone);
                resultView.setText(getString(R.string.say_something));
                findViewById(R.id.btnFile).setEnabled(false);
                findViewById(R.id.btnMic).setEnabled(true);
                findViewById(R.id.btnPause).setEnabled((true));
                findViewById(R.id.spinner).setEnabled((false));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
    }

    private void setErrorState(String message) {
        resultView.setText(message);
        ((Button) findViewById(R.id.btnMic)).setText(R.string.recognize_microphone);
        findViewById(R.id.btnFile).setEnabled(false);
        findViewById(R.id.btnMic).setEnabled(false);
    }

    private void recognizeFile() {
        if (speechStreamService != null) {
            setUiState(STATE_DONE);
            speechStreamService.stop();
            speechStreamService = null;
        } else {
            setUiState(STATE_FILE);
            try {
//                Recognizer rec = new Recognizer(model, 16000.f, "[\"one zero zero zero one\", " +
//                        "\"oh zero one two three four five six seven eight nine\", \"[unk]\", \"[one]\", \"[zero]\"]");

                Recognizer rec = new Recognizer(model, 16000.0f);

                InputStream ais = getAssets().open(
                        "10001-90210-01803.wav");
//                        "13579.wav");
                if (ais.skip(44) != 44) throw new IOException("File too short");

                speechStreamService = new SpeechStreamService(rec, ais, 16000);
                speechStreamService.start(this);
            } catch (IOException e) {
                setErrorState(e.getMessage());
            }
        }
    }

    private void recognizeMicrophone() {
        if (speechService != null) {
            setUiState(STATE_DONE);
            speechService.stop();
            speechService = null;
        } else {
            setUiState(STATE_MIC);
            try {
                Recognizer rec = new Recognizer(model, 16000.0f);
                speechService = new SpeechService(rec, 16000.0f);
                speechService.startListening(this);
            } catch (IOException e) {
                setErrorState(e.getMessage());
            }
        }
    }


    private void pause(boolean checked) {
        if (speechService != null) {
            speechService.setPause(checked);
        }
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, MODELS[position]+" selected", Toast.LENGTH_LONG).show();
        selectedModelName = MODELS[position];
        initModel();
        setUiState(STATE_START);
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
}
