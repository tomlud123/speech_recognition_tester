package org.vosk.demo;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class VoskModelSpinnerListener implements AdapterView.OnItemSelectedListener {

    private String[] MODELS = {"en-us", "small-de-0.15", "test"};
    private Context c;
    private Spinner spinner;

    VoskModelSpinnerListener(Context c, Spinner s) {
        this.c = c;
        this.spinner = s;
        this.spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(c, android.R.layout.simple_spinner_item, MODELS);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
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
        Toast.makeText(c, MODELS[position]+" chosen", Toast.LENGTH_LONG).show();
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
