package com.example.bastin.application_vendeurs_4.asynctask;

import android.os.AsyncTask;

import com.example.bastin.application_vendeurs_4.utils.G;

import java.io.IOException;

import dismap.protocoleDISMAP;
import server.Message;

/**
 * Created by bastin on 13-11-16.
 */

public class SearchGoodsTask extends AsyncTask<Void, Void, Message> implements protocoleDISMAP {
    private int idTypePrecis;

    public SearchGoodsTask(int idTypePrecis) {
        this.idTypePrecis = idTypePrecis;
    }

    @Override
    protected Message doInBackground(Void... params) {
        Message request = new Message();
        Message response = null;

        request.setType(REQUEST_SEARCH_GOODS);

        if(this.idTypePrecis != 0)
            request.addParam("idTypePrecis", this.idTypePrecis);

        try {
            G.clientDISMAP.sendMessage(request);
            response = G.clientDISMAP.receiveMessage();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }
}
