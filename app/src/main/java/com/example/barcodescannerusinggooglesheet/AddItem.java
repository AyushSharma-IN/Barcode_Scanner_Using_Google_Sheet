package com.example.barcodescannerusinggooglesheet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AddItem extends AppCompatActivity implements View.OnClickListener {
    EditText etBarcode;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        etBarcode = (EditText) findViewById(R.id.etBarcode);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    //This is the part where data is transafeered from Your Android phone to Sheet by using HTTP Rest API calls

    @Override
    public void onClick(View v) {

        if(v==btnSave){
            new InsertDataActivity().execute();
            //Define what to do when button is clicked
        }
    }
    class InsertDataActivity extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        final String barcode = etBarcode.getText().toString().trim();
        String result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(AddItem.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Inserting your values..");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = Controller.insert(barcode);
            Log.i(Controller.TAG, "Json obj ");
            try {
                if (jsonObject != null) {
                    result = jsonObject.getString("result");
                }
            } catch (JSONException je) {
                Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
