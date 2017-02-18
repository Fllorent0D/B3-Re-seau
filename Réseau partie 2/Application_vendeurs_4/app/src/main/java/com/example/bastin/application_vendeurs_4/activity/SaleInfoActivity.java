package com.example.bastin.application_vendeurs_4.activity;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.bastin.application_vendeurs_4.utils.G;
import com.example.bastin.application_vendeurs_4.R;
import com.example.bastin.application_vendeurs_4.asynctask.SearchGoodsTask;
import com.example.bastin.application_vendeurs_4.model.DAOLogBook;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;

import dismap.models.TypeAppareil;
import dismap.protocoleDISMAP;
import server.Message;
import server_indep.protocoleINDEP;

public class SaleInfoActivity extends AppCompatActivity implements protocoleDISMAP, protocoleINDEP {

    protected EditText timeEditText;
    protected EditText argumentsEditText;
    protected EditText remarksEditText;
    protected Button sendButton;
    protected CheckBox successSellingCheckbox;
    protected Spinner typesAppareilSpinner;

    protected int time;
    protected String arguments;
    protected String remarks;
    protected boolean successSelling;
    protected TypeAppareil typeAppareil;

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
            case R.id.disponibility_item:
                startActivity(new Intent(SaleInfoActivity.this, DisponibilityActivity.class));
                return true;
            case R.id.logbook_item:
                startActivity(new Intent(SaleInfoActivity.this, LogBookActivity.class));
                return true;
            case R.id.reservation_item:
                startActivity(new Intent(SaleInfoActivity.this, ReservationActivity.class));
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

        setTitle("Salo Info");

        setContentView(R.layout.activity_sale_info);



        timeEditText = (EditText) findViewById(R.id.timeEditText);
        argumentsEditText = (EditText) findViewById(R.id.argumentsEditText);
        remarksEditText = (EditText) findViewById(R.id.remarksEditText);
        successSellingCheckbox = (CheckBox) findViewById(R.id.successSellingCheckbox);
        typesAppareilSpinner = (Spinner) findViewById(R.id.typesAppareilSpinner);
        sendButton = (Button) findViewById(R.id.sendButton);

        typesAppareilSpinner.setEnabled(false);

        new SearchGoods(0).execute();

        successSellingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    typesAppareilSpinner.setEnabled(false);
                else
                    typesAppareilSpinner.setEnabled(true);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm())
                {
                    if(successSellingCheckbox.isChecked())
                        logBook.log("Envoi d'informations d'une vente réussie");
                    else
                        logBook.log("Envoi d'informations d'une vente ratée");

                    new SaleInfo().execute();
                }
            }
        });
    }

    private boolean checkForm() {
        boolean checked = true;

        if(!timeEditText.getText().toString().equals(""))
            time = Integer.parseInt(timeEditText.getText().toString());
        else
        {
            checked = false;
            Toast.makeText(getApplicationContext(), "Veuillez indiquer le temps de la vente", Toast.LENGTH_LONG).show();
        }

        arguments = argumentsEditText.getText().toString();
        remarks = remarksEditText.getText().toString();
        successSelling = successSellingCheckbox.isChecked();
        typeAppareil = (TypeAppareil) typesAppareilSpinner.getSelectedItem();

        if(arguments.equals(""))
        {
            checked = false;
            Toast.makeText(getApplicationContext(), "Veuillez indiquer un argument", Toast.LENGTH_LONG).show();
        }

        if(!successSelling && typeAppareil.getIdTypeAppareil() == 0)
        {
            checked = false;
            Toast.makeText(getApplicationContext(), "Veuillez indiquer un type d'appareil", Toast.LENGTH_LONG).show();
        }

        return checked;
    }

    private class SearchGoods extends SearchGoodsTask {

        public SearchGoods(int idTypePrecis) {
            super(idTypePrecis);
        }

        @Override
        protected void onPostExecute (Message response) {
            if(response.hasParam("error"))
                Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
            else
            {
                ArrayList<TypeAppareil> data = (ArrayList<TypeAppareil>) response.getParam("data");

                TypeAppareil tp = new TypeAppareil();
                tp.setIdTypeAppareil(0);
                tp.setLibelle("Sélectionnez");

                data.add(0, tp);

                TypeAppareil array[] = data.toArray(new TypeAppareil[data.size()]);

                ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, array);

                typesAppareilSpinner.setAdapter(spinnerAdapter);
            }
        }
    }

    private class SaleInfo extends AsyncTask<Void, Void, Message> {

        @Override
        protected Message doInBackground(Void... params) {

            Message request = new Message();
            Message response = new Message();

            request.setType(REQUEST_SALE_INFO);

            request.addParam("idSeller", G.seller.getIdPersonnel());
            request.addParam("time", time);
            request.addParam("arguments", arguments);
            request.addParam("remarks", remarks);

            request.addParam("successSelling", successSelling);

            if(!successSelling)
                request.addParam("idTypeAppareil", typeAppareil.getIdTypeAppareil());

            try {
                G.clientINDEP.sendMessage(request);
                response = G.clientINDEP.receiveMessage();
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
                timeEditText.setText("");
                argumentsEditText.setText("");
                remarksEditText.setText("");
                Toast.makeText(getApplicationContext(), "Votre information a été ajoutée !", Toast.LENGTH_LONG).show();
            }

        }
    }
}
