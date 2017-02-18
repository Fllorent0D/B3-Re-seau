package com.example.bastin.application_vendeurs_4.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androidplot.Plot;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.xy.XYPlot;
import com.example.bastin.application_vendeurs_4.R;
import com.example.bastin.application_vendeurs_4.utils.G;

import java.io.IOException;
import java.util.ArrayList;

import dismap.models.Stat;
import dismap.protocoleDISMAP;
import server.Message;

public class StatsActivity extends AppCompatActivity implements protocoleDISMAP {

    protected PieChart pie;
    protected Plot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Satistiques");

        setContentView(R.layout.activity_stats);

        pie = (PieChart) findViewById(R.id.pieChart);
        plot = (XYPlot) findViewById(R.id.plot);

        new StatsSoldAppareils().execute();

        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
    }

    public void buildPieChart(ArrayList<Stat> data) {

        SegmentFormatter sf0 = new SegmentFormatter(this, R.xml.pie_formatter_1);
        SegmentFormatter sf1 = new SegmentFormatter(this, R.xml.pie_formatter_2);
        SegmentFormatter sf2 = new SegmentFormatter(this, R.xml.pie_formatter_3);

        SegmentFormatter sf = null;

        for(int i = 0, tmp = 0 ; i < data.size() ; i++, tmp++) {
            Stat stat = data.get(i);

            Segment s = new Segment(stat.getKey(), stat.getValue());

            if(tmp > 2)
                tmp = 0;

            switch (tmp){
                case 0: sf = sf0; break;
                case 1: sf = sf1; break;
                case 2: default: sf = sf2; break;
            }
            
            pie.addSegment(s, sf);
        }

        pie.invalidate();
    }

    private class StatsSoldAppareils extends AsyncTask<Void, Void, Message> {

        @Override
        protected Message doInBackground(Void... params) {
            Message request = new Message();
            Message response = new Message();

            request.setType(REQUEST_STATS_SOLD_APPAREILS);

            try {
                G.clientDISMAP.sendMessage(request);
                response = G.clientDISMAP.receiveMessage();
            } catch (IOException |ClassNotFoundException e) {
                response.addParam("error", e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(Message response) {
            if(response.hasParam("error"))
                Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
            else
                buildPieChart((ArrayList) response.getParam("data"));
        }
    }

    private class StatsStatsTurnover extends AsyncTask<Void, Void, Message> {

        @Override
        protected Message doInBackground(Void... params) {
            Message request = new Message();
            Message response = new Message();

            request.setType(REQUEST_STATS_TURNOVER);

            try {
                G.clientDISMAP.sendMessage(request);
                response = G.clientDISMAP.receiveMessage();
            } catch (IOException |ClassNotFoundException e) {
                response.addParam("error", e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(Message response) {
            if(response.hasParam("error"))
                Toast.makeText(getApplicationContext(), (String) response.getParam("error"), Toast.LENGTH_LONG).show();
        }
    }
}
