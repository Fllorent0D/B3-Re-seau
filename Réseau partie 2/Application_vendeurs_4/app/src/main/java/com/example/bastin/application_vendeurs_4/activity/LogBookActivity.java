package com.example.bastin.application_vendeurs_4.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.bastin.application_vendeurs_4.R;
import com.example.bastin.application_vendeurs_4.entity.LogBook;
import com.example.bastin.application_vendeurs_4.model.DAOLogBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogBookActivity extends AppCompatActivity {

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
                startActivity(new Intent(LogBookActivity.this, ReservationActivity.class));
                return true;
            case R.id.disponibility_item:
                startActivity(new Intent(LogBookActivity.this, DisponibilityActivity.class));
                return true;
            case R.id.sale_info_item:
                startActivity(new Intent(LogBookActivity.this, SaleInfoActivity.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Log book");

        setContentView(R.layout.activity_log_book);

        ListView logBookListView = (ListView) findViewById(R.id.logBookListView);

        DAOLogBook dao = new DAOLogBook(getApplicationContext());

        dao.open();

        ArrayList<LogBook> listDB = dao.getAll();

        List<HashMap<String, String>> list = new ArrayList();

        HashMap<String, String> element;

        Log.d("LogBook", String.valueOf(listDB.size()));

        for(int i = 0; i < listDB.size() ; i++) {
            LogBook lb1 = listDB.get(i);

            element = new HashMap();
            element.put("text1", lb1.getAction());
            element.put("text2", lb1.getDate());

            list.add(element);
        }

        ListAdapter adapter = new SimpleAdapter(this,
                list,
                android.R.layout.simple_list_item_2,
                new String[]{"text1", "text2"},
                new int[]{android.R.id.text1, android.R.id.text2});

        logBookListView.setAdapter(adapter);
    }
}
