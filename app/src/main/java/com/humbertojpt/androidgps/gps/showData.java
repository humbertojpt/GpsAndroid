package com.humbertojpt.androidgps.gps;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class showData extends AppCompatActivity {

    private ArrayList<String> values;
    private ListView mListViewParse;
    private List<ParseObject> ob;
    private String lat;
    private String lon;
    private String prec;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        mListViewParse = (ListView) findViewById(R.id.listView);
        Bundle bundle = getIntent().getExtras();
        lat = bundle.getString("latitud");
        lon = bundle.getString("longitud");
        prec = bundle.getString("precision");
        time = bundle.getString("tiempo");

        Log.d("his", lat + "");
        Log.d("his", lon + "");
        Log.d("his", prec + "");
        Log.d("his", time + "");

        actualizar();
        new GetData().execute();
    }

    public void actualizar() {
        new SendData().execute();
    }

    private class SendData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ParseObject testObject = new ParseObject("gps");
            testObject.put("latitud", lat);
            testObject.put("longitud", lon);
            testObject.put("precision", prec);
            testObject.put("tiempo", time);

            testObject.saveInBackground();
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            new GetData().execute();
        }
    }


    private class GetData extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            values = new ArrayList<String>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "gps");
                ob = query.find();
                for (ParseObject dato : ob){
                    values.add("LAT: "+dato.get("latitud")+ " LONG: " + dato.get("longitud") + "\n PREC: "
                            +dato.get("precision")+ " TIME: "+dato.get("tiempo"));
                }
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(showData.this,
                    android.R.layout.simple_list_item_2,android.R.id.text2, values);
            mListViewParse.setAdapter(adapter1);
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
