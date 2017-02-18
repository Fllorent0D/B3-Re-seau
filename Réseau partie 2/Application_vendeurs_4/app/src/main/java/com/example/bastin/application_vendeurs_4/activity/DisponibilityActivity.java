package com.example.bastin.application_vendeurs_4.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bastin.application_vendeurs_4.utils.G;
import com.example.bastin.application_vendeurs_4.R;
import com.example.bastin.application_vendeurs_4.adapter.TypesAppareilAdapter;
import com.example.bastin.application_vendeurs_4.asynctask.SearchGoodsTask;
import com.example.bastin.application_vendeurs_4.model.DAOLogBook;

import java.io.IOException;
import java.util.ArrayList;

import dismap.models.TypeAppareil;
import dismap.models.TypePrecis;
import dismap.protocoleDISMAP;
import server.Message;

public class DisponibilityActivity extends AppCompatActivity implements protocoleDISMAP {

    protected Spinner typesAppareilSpinner;
    protected DAOLogBook logBook;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reservation_item:
                startActivity(new Intent(DisponibilityActivity.this, ReservationActivity.class));
                return true;
            case R.id.logbook_item:
                startActivity(new Intent(DisponibilityActivity.this, LogBookActivity.class));
                return true;
            case R.id.sale_info_item:
                startActivity(new Intent(DisponibilityActivity.this, SaleInfoActivity.class));
                return true;
            case R.id.stats_item:
                startActivity(new Intent(DisponibilityActivity.this, StatsActivity.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logBook = new DAOLogBook(getApplicationContext());
        logBook.open();

        setTitle("Disponibilité");

        setContentView(R.layout.activity_disponibility);


        typesAppareilSpinner = (Spinner) findViewById(R.id.typesAppareilSpinner);

        typesAppareilSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TypePrecis tp = (TypePrecis) typesAppareilSpinner.getSelectedItem();
                logBook.log("Recherche des disponibilités (" + tp.getType() + ")");
                new SearchGoods(tp.getIdTypePrecis()).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new ListTypePrecis().execute();
    }

    private class SearchGoods extends SearchGoodsTask {

        SearchGoods(int idTypePrecis) {
            super(idTypePrecis);
        }

        @Override
        protected void onPostExecute (Message response) {

            if(response.hasParam("error"))
            {
                String error = (String) response.getParam("error");
                Log.d("error", error);
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
            else
            {
                ArrayList<TypeAppareil> list = (ArrayList<TypeAppareil>) response.getParam("data");

                ListView resultsListView = (ListView) findViewById(R.id.resultsListView);

                TypesAppareilAdapter adapter = new TypesAppareilAdapter(getApplicationContext(), list);

                resultsListView.setAdapter(adapter);
            }
        }
    }

    private class ListTypePrecis extends AsyncTask<Void, Void, Message> {

        @Override
        protected Message doInBackground(Void... params) {
            Message request = new Message();
            Message response = new Message();

            request.setType(REQUEST_LIST_TYPES_PRECIS);

            try {
                G.clientDISMAP.sendMessage(request);
                response = G.clientDISMAP.receiveMessage();
            } catch (IOException | ClassNotFoundException e) {
                response.addParam("error", e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute (Message response) {

            if(response.hasParam("error"))
                Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
            else
            {
                ArrayList<TypePrecis> data = (ArrayList) response.getParam("data");

                TypePrecis defaultType = new TypePrecis();
                defaultType.setIdTypePrecis(0);
                defaultType.setType("Tous les types");

                data.add(0, defaultType);

                TypePrecis array[] = data.toArray(new TypePrecis[data.size()]);

                ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, array);
                typesAppareilSpinner.setAdapter(spinnerAdapter);

            }
        }
    }
}
