package com.example.barcodescannerusinggooglesheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton fabscan, fabtxt;
    String barcode;
    MyAdapter adapter;
    ProgressDialog dialog;
    ArrayList<ListItem> list;
    ListItem listItem;
    String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        fabscan = (FloatingActionButton)findViewById(R.id.fabscan);
        fabtxt = (FloatingActionButton)findViewById(R.id.fabtxt);
        final Activity activity = this;

        new ReadData().execute();
        fabscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        fabtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddItem.class));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                listItem = list.get(position);
                Id = listItem.getId();
                new DeleteData().execute();
                new ReadData().execute();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case  R.id.refresh:
                new ReadData().execute();
                break;
            case  R.id.deleteAll:
                new DeleteAll().execute();
                list.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null)
        {
            barcode = result.getContents();
            if (barcode != null) {
                new InsertDataActivity().execute();
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    class DeleteData extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog dialog;
        String result=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Deleting... ");
            dialog.show();

        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(Controller.TAG,"IDVALUE"+Id);
            JSONObject jsonObject = Controller.deleteData(Id);
            Log.i(Controller.TAG, "Json obj "+jsonObject);

            try {
                if (jsonObject != null) {
                    result=jsonObject.getString("result");
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
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        }
    }

    class DeleteAll extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog dialog;
        String result=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Deleting All Data... ");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = Controller.deleteAll();
            Log.i(Controller.TAG, "Json obj "+jsonObject);
            try {
                if (jsonObject != null) {
                    result=jsonObject.getString("result");
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
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            new ReadData().execute();
        }
    }

    class InsertDataActivity extends AsyncTask< Void, Void, Void > {

        String result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Inserting your Value...");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void...params) {
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
            if (result.equals("Barcode Already Exist!"))
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                new ReadData().execute();
            }
        }
    }

    class ReadData extends AsyncTask < Void, Void, Void > {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Fetching all the Data...");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void...params) {
            list = new ArrayList<>();
            JSONObject jsonObject = Controller.read();
            try {
                if (jsonObject != null) {
                    JSONArray array = jsonObject.getJSONArray("items");
                    int lenArray = array.length();
                    if (lenArray > 0) {
                        for (int i = 0; i < lenArray; i++) {
                            JSONObject innerObject = array.getJSONObject(i);

                            String id = innerObject.getString("id");
                            String barcode = innerObject.getString("barcode");

                            listItem = new ListItem(id, barcode);
                            list.add(listItem);
                            adapter = new MyAdapter(getApplicationContext(), list);
                        }
                    }
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
            if (list.size() > 0) {
                listView.setAdapter(adapter);
            } else {
                listView.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}
