package com.example.bastin.application_vendeurs_4.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bastin.application_vendeurs_4.utils.G;
import com.example.bastin.application_vendeurs_4.R;
import com.example.bastin.application_vendeurs_4.asynctask.ListClientsTask;
import com.example.bastin.application_vendeurs_4.asynctask.SearchGoodsTask;
import com.example.bastin.application_vendeurs_4.model.DAOLogBook;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import dismap.models.TypeAppareil;
import dismap.protocoleDISMAP;
import server.Message;

public class ReservationActivity extends AppCompatActivity implements protocoleDISMAP {

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
                startActivity(new Intent(ReservationActivity.this, DisponibilityActivity.class));
                return true;
            case R.id.logbook_item:
                startActivity(new Intent(ReservationActivity.this, LogBookActivity.class));
                return true;
            case R.id.sale_info_item:
                startActivity(new Intent(ReservationActivity.this, SaleInfoActivity.class));
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

        setTitle("Réservation");

        setContentView(R.layout.activity_reservation);

        new ListClients().execute();
        new SearchGoods(0).execute();

        Button reservationButton = (Button) findViewById(R.id.reservationButton);

        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Reserve().execute();
            }
        });

        Button noticeCashierButton = (Button) findViewById(R.id.noticeCashierButton);

        noticeCashierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logBook.log("Demande d'une réservation");
                new NoticeCashier().execute();
            }
        });
    }

    private class NoticeCashier extends AsyncTask<Void, Void, Message> {

        protected Spinner clientSpinner;
        protected dismap.models.Client client;

        public NoticeCashier() {
            clientSpinner = (Spinner) findViewById(R.id.clientSpinner);
            client = (dismap.models.Client) clientSpinner.getSelectedItem();
        }

        @Override
        protected Message doInBackground(Void... params) {
            Message request = new Message();
            Message response = new Message();
            request.addParam("notification", "Arrivée immitante du client " + client.getPrenom() + " " + client.getNom());

            try {
                InetAddress addr = InetAddress.getByName("10.59.14.104");

                Socket socket = new Socket(addr, 10000);

                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                oos.flush();

                oos.writeObject(request);
                oos.flush();

            } catch (IOException e) {
                response.addParam("error", e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute (Message response) {
            if(response.hasParam("error"))
                Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "La notification a bien été envoyée !", Toast.LENGTH_LONG).show();
        }
    }


    private class ListClients extends ListClientsTask {

        @Override
        protected void onPostExecute (Message response) {
            if(response.hasParam("error"))
                Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
            else
            {
                Spinner spinner = (Spinner) findViewById(R.id.clientSpinner);

                ArrayList<dismap.models.Client> data = (ArrayList<dismap.models.Client>) response.getParam("data");

                dismap.models.Client array[] = data.toArray(new dismap.models.Client[data.size()]);

                ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, array);

                spinner.setAdapter(spinnerAdapter);
            }
        }
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
                Spinner spinner = (Spinner) findViewById(R.id.appareilSpinner);

                ArrayList<TypeAppareil> data = (ArrayList<TypeAppareil>) response.getParam("data");

                TypeAppareil array[] = data.toArray(new TypeAppareil[data.size()]);

                ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, array);

                spinner.setAdapter(spinnerAdapter);
            }
        }
    }

    private class Reserve extends AsyncTask<Void, Void, Message> {

        protected Spinner clientSpinner;
        protected Spinner appareilSpinner;
        protected EditText quantityEditText;

        protected dismap.models.Client client;
        protected TypeAppareil typeAppareil;
        protected int quantity;

        public Reserve() {
            clientSpinner = (Spinner) findViewById(R.id.clientSpinner);
            client = (dismap.models.Client) clientSpinner.getSelectedItem();

            appareilSpinner = (Spinner) findViewById(R.id.appareilSpinner);
            typeAppareil = (TypeAppareil) appareilSpinner.getSelectedItem();

            quantityEditText = (EditText) findViewById(R.id.quantityEditText);
            quantity = Integer.parseInt(quantityEditText.getText().toString());
        }

        @Override
        protected Message doInBackground(Void... params) {
            Message request = new Message();
            Message response = new Message();

            request.setType(REQUEST_TAKE_GOODS);
            request.addParam("idClient", client.getId());
            request.addParam("idTypeAppareil", typeAppareil.getIdTypeAppareil());
            request.addParam("quantity", quantity);

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
            if(response.hasParam("error")) {
                Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
            }
            else
            {
                quantityEditText.setText("");
                Toast.makeText(getApplicationContext(), "Réservation effectuée !", Toast.LENGTH_LONG).show();
            }
        }
    }
}
