package com.sonxuannguyen.rentalz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        Spinner typesSpinner = (Spinner) findViewById(R.id.apartment_types_spinner);
        typesSpinner.setSelection(0);
        // Create an ArrayAdapter using the string array and a default spinner layout that defines
        // how the selected choice appears in the spinner control.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.apartment_types_array, android.R.layout.simple_spinner_item);
        // Specify the layout the adapter should use to display the list of spinner choices.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        typesSpinner.setAdapter(adapter);
        // Responding to User Selections
        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                String defaultValue = getResources().getStringArray(R.array.apartment_types_array)[0];
                if (adapterView.getItemAtPosition(pos).equals(defaultValue)) return;

                String type = adapterView.getItemAtPosition(pos).toString();
                adapter.notifyDataSetChanged();
                typesSpinner.setSelection(pos);
                Snackbar.make(adapterView, type + " is selected", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}