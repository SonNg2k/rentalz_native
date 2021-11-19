package com.sonxuannguyen.rentalz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private void showEphemeralSnackBar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private Bundle getFormData() {
        // Get views associated with each form field...
        final TextInputLayout nameField = findViewById(R.id.name_field);
        final TextInputLayout reporterNameField
                = findViewById(R.id.reporter_name_field);
        final TextInputLayout addressField = findViewById(R.id.address_field);
        final Spinner typesSpinner = findViewById(R.id.apartment_types_spinner);
        final RadioGroup comfortLevelRadioGroup
                = findViewById(R.id.comfort_level_radio_group);
        final TextInputLayout nBedroomsField
                = findViewById(R.id.n_bedrooms_field);
        final TextInputLayout monthlyPriceField
                = findViewById(R.id.monthly_price_field);
        final TextInputLayout noteField = findViewById(R.id.note_field);

        // Get data of each form field in the form...
        final String apartmentName
                = Objects.requireNonNull(nameField.getEditText()).getText().toString().trim();
        final String reporterName
                = Objects.requireNonNull(reporterNameField.getEditText()).getText().toString().trim();
        final String address
                = Objects.requireNonNull(addressField.getEditText()).getText().toString().trim();
        final String type = typesSpinner.getSelectedItem().toString();

        final int selectedRadioBtnId = comfortLevelRadioGroup.getCheckedRadioButtonId();
        final RadioButton selectedRadioBtn =
                comfortLevelRadioGroup.findViewById(selectedRadioBtnId);
        String comfortLevel = "";
        if (selectedRadioBtn != null)
            comfortLevel = selectedRadioBtn.getText().toString().trim();

        final String nBedrooms
                = Objects.requireNonNull(nBedroomsField.getEditText()).getText().toString().trim();
        final String monthlyPrice
                = Objects.requireNonNull(monthlyPriceField.getEditText()).getText().toString().trim();
        final String note = Objects.requireNonNull(noteField.getEditText()).getText().toString().trim();

        // Add data to the bundle...
        final Bundle formData = new Bundle();
        formData.putString("apartmentName", apartmentName);
        formData.putString("reporterName", reporterName);
        formData.putString("address", address);
        formData.putString("type", type);
        formData.putString("comfortLevel", comfortLevel);
        formData.putString("nBedrooms", nBedrooms);
        formData.putString("monthlyPrice", monthlyPrice);
        formData.putString("note", note);
        return formData;
    }

    /// If empty string is returned, it means there is no validation error.
    private String requiredValidator() {
        final Bundle formData = getFormData();
        if (TextUtils.isEmpty(formData.getString("apartmentName")))
            return "⚠️ Apartment name is required";

        if (TextUtils.isEmpty(formData.getString("reporterName")))
            return "⚠️ Reporter name is required";

        if (TextUtils.isEmpty(formData.getString("address")))
            return "⚠️ Address is required";

        if (formData.getString("type").equals("Select apartment type"))
            return "⚠️ ️Please choose the apartment type";

        if (TextUtils.isEmpty(formData.getString("comfortLevel")))
            return "⚠️ Please select a level of comfort";

        if (TextUtils.isEmpty(formData.getString("nBedrooms")))
            return "⚠️ Please specify the number of bedrooms";

        if (TextUtils.isEmpty(formData.getString("monthlyPrice")))
            return "⚠️ Monthly rental price is required";

        return "";
    }

    private void showConfirmationDialog() {
        final Bundle formData = getFormData();
        String summaryData = "▶ Apartment name: " + formData.getString("apartmentName") + "\n";
        summaryData += "▶ Reporter's name: " + formData.getString("reporterName") + "\n";
        summaryData += "▶ Address: " + formData.getString("address") + "\n";
        summaryData += "▶ Apartment type: " + formData.getString("type") + "\n";
        summaryData += "▶ Comfort level: " + formData.getString("comfortLevel") + "\n";
        summaryData += "▶ Number of bedrooms: " + formData.getString("nBedrooms") + "\n";
        summaryData += "▶ Monthly rental price in US Dollar: " + formData.getString("monthlyPrice") + "\n";
        summaryData += "▶ Note: " + formData.get("note");

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(summaryData)
                .setTitle("Check your data one more time");
        // Add the buttons
        builder.setPositiveButton("CONFIRM", (dialog, id) -> {
            // User clicked OK button
            showEphemeralSnackBar("You have entered all the required data, but there's no database to save this 😢.");
        });
        builder.setNegativeButton("DECLINE", (dialog, id) -> {
            // User cancelled the dialog
        });

        // Create the AlertDialog
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        final Button submitBtn = findViewById(R.id.submit_button);

        submitBtn.setOnClickListener(v -> {
            String errorMsg = requiredValidator();
            if (!errorMsg.isEmpty()) showEphemeralSnackBar(errorMsg);
            else showConfirmationDialog();
        });

        /// Logics to handle the on-item-selected event emitted by the spinner of apartment types...
        final Spinner typesSpinner = findViewById(R.id.apartment_types_spinner);
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
                final String defaultValue
                        = getResources().getStringArray(R.array.apartment_types_array)[0];
                if (adapterView.getItemAtPosition(pos).equals(defaultValue)) return;

                final String type = adapterView.getItemAtPosition(pos).toString();
                adapter.notifyDataSetChanged();
                typesSpinner.setSelection(pos);
                showEphemeralSnackBar(type + " is selected ✅");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}