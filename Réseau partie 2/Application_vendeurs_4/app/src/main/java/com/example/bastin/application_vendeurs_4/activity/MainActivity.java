package com.example.bastin.application_vendeurs_4.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bastin.application_vendeurs_4.utils.G;
import com.example.bastin.application_vendeurs_4.R;
import com.example.bastin.application_vendeurs_4.model.DAOLogBook;
import com.example.bastin.application_vendeurs_4.network.Client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dismap.models.Personnel;
import dismap.protocoleDISMAP;
import server.Message;

public class MainActivity extends AppCompatActivity implements protocoleDISMAP {

    protected DAOLogBook logBook;
    protected Button connectionButton;
    protected Spinner languagesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logBook = new DAOLogBook(getApplicationContext());
        logBook.open();

        setTitle("Connexion");

        G.clientDISMAP = new Client();
        G.clientDISMAP.setAddr("10.222.64.15");
        G.clientDISMAP.setPort(8000);

        G.clientINDEP = new Client();
        G.clientINDEP.setAddr("10.222.64.15");
        G.clientINDEP.setPort(6001);

        new Connect().execute();
        setContentView(R.layout.activity_main);

        languagesSpinner = (Spinner) findViewById(R.id.languagesSpinner);

        connectionButton = (Button) findViewById(R.id.connectionButton);
        connectionButton.setOnClickListener(v -> new Login().execute());
    }

    public void setLanguage(String language) {

        String code;

        switch(language)
        {
            case "English":
                code = "en";
                break;
            case "Deutsch":
                code = "de";
                break;
            case "Francais":
            default:
                code = "fr";
        }

        Locale locale = new Locale(code);
        Locale.setDefault(locale);

        Resources resources = getApplicationContext().getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    private class Login extends AsyncTask<Void, Void, HashMap<String, Message>> {

        private String login;
        private String password;

        @Override
        protected void onPreExecute () {
            EditText loginEditText = (EditText) findViewById(R.id.loginEditText);
            EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

            login = loginEditText.getText().toString();
            password = passwordEditText.getText().toString();
        }

        @Override
        protected HashMap<String, Message> doInBackground(Void... params) {
            Message request = new Message();
            Message response = new Message();
            HashMap<String, Message> responses = new HashMap();

            request.setType(REQUEST_LOGIN);
            request.addParam("login", login);
            request.addParam("password", password);

            logBook.log("Tentative de connexion...");

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(login.getBytes());
                md.update(password.getBytes());
                long temps = (new Date()).getTime();

                double alea = Math.random();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos);
                bdos.writeLong(temps); bdos.writeDouble(alea);
                md.update(baos.toByteArray());
                byte[] msgD = md.digest();

                request.addParam("password", msgD);
                request.addParam("time", temps);
                request.addParam("random", alea);

                G.clientDISMAP.sendMessage(request);
                responses.put("DISMAP", G.clientDISMAP.receiveMessage());

                G.clientINDEP.sendMessage(request);
                responses.put("INDEP", G.clientINDEP.receiveMessage());


            } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e) {
                response.addParam("error", e.getMessage());
            }

            responses.put("DEFAULT", response);

            return responses;
        }

        @Override
        protected void onPostExecute (HashMap<String, Message> responses) {

            for(Map.Entry<String, Message> entry : responses.entrySet()) {
                Message response = entry.getValue();

                if(response.hasParam("error"))
                {
                    Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
                    logBook.log("Tentative de connexion échouée");
                    return;
                }
            }

            Message response = responses.get("DISMAP");

            Personnel seller = (Personnel) response.getParam("seller");
            logBook.log("Connexion du vendeur : " + seller.getLogin());
            G.seller = seller;

            setLanguage((String) languagesSpinner.getSelectedItem());

            Intent intent = new Intent(MainActivity.this, DisponibilityActivity.class);
            Toast.makeText(getApplicationContext(), "Vous êtes authentifié !", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }

    private class Connect extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response;

            try {
                G.clientDISMAP.connect();
                G.clientINDEP.connect();
                response = "Vous êtes connecté au serveur !";
            } catch (IOException e) {
                response = e.getMessage();
            }

            return response;
        }

        @Override
        protected void onPostExecute (String response) {
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        }
    }
}
