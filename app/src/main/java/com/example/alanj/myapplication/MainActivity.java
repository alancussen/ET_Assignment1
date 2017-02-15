package com.example.alanj.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // private fields of the class
    private DBOpenHelper tdb;
    private SQLiteDatabase sdb;

    // private fields of the class
    private TextView tv_display;
    private ListView lv_mainlist;
    private ArrayList<String> al_strings;
    private ArrayAdapter<String> aa_strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pull the list view and the edit text from the xml
        tv_display = (TextView) findViewById(R.id.tv_display);
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);

        // generate an array list with some simple strings
        al_strings = new ArrayList<String>();
//        al_strings.add("first string");
//        al_strings.add("second string");
//        al_strings.add("third string");

        // create an array adapter for al_strings and set it on the listview
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al_strings);
        lv_mainlist.setAdapter(aa_strings);

        // get access to an sqlite database
        tdb = new DBOpenHelper(this, "contact.db", null, 1);
        sdb = tdb.getWritableDatabase();

        // add in a few rows to our database
        ContentValues cv = new ContentValues();
        cv.put("FIRST_NAME", "Alan");
        cv.put("LAST_NAME", "Cussen");
        cv.put("HOME_PHONE", "061345725");
        cv.put("MOBILE_PHONE", "0863427842");
        cv.put("EMAIL", "alan@cussen.com");
        sdb.insert("contact", null, cv);

        cv.put("FIRST_NAME", "John");
        cv.put("LAST_NAME", "Cussen");
        cv.put("HOME_PHONE", "061745264");
        cv.put("MOBILE_PHONE", "0868873267");
        cv.put("EMAIL", "john@cussen.com");
        sdb.insert("contact", null, cv);

        cv.put("FIRST_NAME", "James");
        cv.put("LAST_NAME", "Cussen");
        cv.put("HOME_PHONE", "061463726");
        cv.put("MOBILE_PHONE", "0869615437");
        cv.put("EMAIL", "james@cussen.com");
        sdb.insert("contact", null, cv);

        // run a simple update to change a name
//        ContentValues u = new ContentValues();
//        u.put("FIRST_NAME", "nalA");
//        sdb.update("contact", u, "FIRST_NAME = 'Alan' ", null);

        // name of the table to query
        String table_name = "contact";
        // the columns that we wish to retrieve from the tables
        String[] columns = {"ID", "FIRST_NAME", "LAST_NAME"};
        // where clause of the query. DO NOT WRITE WHERE IN THIS
        String where = null;
        // arguments to provide to the where clause
        String where_args[] = null;
        // group by clause of the query. DO NOT WRITE GROUP BY IN THIS
        String group_by = null;
        // having clause of the query. DO NOT WRITE HAVING IN THIS
        String having = null;
        // order by clause of the query. DO NOT WRITE ORDER BY IN THIS
        String order_by = null;

        // run the query. this will give us a cursor into the database
        // that will enable us to change the table row that we are working with
        Cursor c = sdb.query(table_name, columns, where, where_args, group_by, having, order_by);
        // print out some data from the cursor to the screen
        //TextView tv = (TextView) findViewById(R.id.tv);
        String total_text = "total number of rows: " + c.getCount() + "\n";
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++) {
            total_text += c.getInt(0) + " " + c.getString(1) + " " + c.getString(2) + "\n";
            c.moveToNext();
        }
        al_strings.add(total_text);

        // add in a listener that listens for short clicks on our list items
        lv_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // overridden method that we must implement to get access to short clicks
            public void onItemClick(AdapterView<?> adapterview, View view, int pos, long id) {
                // update the text view to state that a short click was made here
                tv_display.setText("item " + lv_mainlist.getItemAtPosition(pos) + " selected");
            }
        });

        // add in a listener that listens for long clicks on our list items
        lv_mainlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // overridden method that we must implement to get access to long clicks
            public boolean onItemLongClick(AdapterView<?> adapterview, View view, int pos, long id) {
                // update the display with what we have just long clicked
                tv_display.setText("item " + lv_mainlist.getItemAtPosition(pos) + " long clicked");
                // as we are going to change the textview anyway we can automatically return true;
                return true;
            }
        });

    }

    // overridden method that will clear out the contents of the database
    protected void onDestroy() {
        super.onDestroy();

        // run a query that will delete all the rows in our database
        String table = "contact";
        String where = null;
        String where_args[] = null;
        sdb.delete(table, where, where_args);
    }
}
