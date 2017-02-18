package com.example.bastin.application_vendeurs_4.asynctask;

import android.os.AsyncTask;

import com.example.bastin.application_vendeurs_4.utils.G;

import java.io.IOException;

import dismap.protocoleDISMAP;
import server.Message;

/**
 * Created by bastin on 13-11-16.
 */

public class ListClientsTask extends AsyncTask<Void, Void, Message> implements protocoleDISMAP {
    @Override
    protected Message doInBackground(Void... params) {
        Message response = new Message();
        Message request = new Message();

        request.setType(REQUEST_LIST_CLIENTS);

        try {
            G.clientDISMAP.sendMessage(request);
            response = G.clientDISMAP.receiveMessage();
        } catch (IOException | ClassNotFoundException e) {
            response.addParam("error", e.getMessage());
        }

        return response;
    }
}
