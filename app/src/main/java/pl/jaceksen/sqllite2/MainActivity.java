package pl.jaceksen.sqllite2;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBManager dbManager;
    EditText etUserName;
    EditText etPassword;
    int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUserName = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);

        dbManager = new DBManager(this);

    }

    //zapisanie rekordu
    public void saveRecord(View view) {

        ContentValues values = new ContentValues();
        values.put(DBManager.colUserName,etUserName.getText().toString());
        values.put(DBManager.colPassword,etPassword.getText().toString());
        long id = dbManager.Insert(values);
        if(id > 0) {
            Toast.makeText(getApplicationContext(), "Rekord dodany. ID: " + id, Toast.LENGTH_SHORT).show();
            etUserName.setText("");
            etPassword.setText("");
            }
        else
            Toast.makeText(getApplicationContext(),"Nie udało się dodać rekordu",Toast.LENGTH_SHORT).show();
    }

    //odczyt danych
    ArrayList<AdapterItems> listnewsData = new ArrayList<>();
    MyCustomAdapter myadapter;


    public void buLoad(View view) {
        loadElement();
    }

    void loadElement(){

        listnewsData.clear();
        //nazwy kolumn
        String[] projection = {"UserName","Password"};
        String[] selectionArgs = {"%"+etUserName.getText().toString()+"%"};

        //Cursor cursor = dbManager.query(null,null,null,dbManager.colUserName);
        Cursor cursor = dbManager.query(null,"UserName like ?",selectionArgs,dbManager.colUserName);

        if(cursor.moveToFirst()){
            String tableData="";
            do{
                tableData += cursor.getString(cursor.getColumnIndex(dbManager.colUserName)) + " " +
                        cursor.getString(cursor.getColumnIndex(dbManager.colPassword)) + "\n";

                listnewsData.add(new AdapterItems(
                        cursor.getInt(cursor.getColumnIndex(dbManager.colId)),
                        cursor.getString(cursor.getColumnIndex(dbManager.colUserName)),
                        cursor.getString(cursor.getColumnIndex(dbManager.colPassword))
                ));


            } while (cursor.moveToNext());

            //Toast.makeText(getApplicationContext(), tableData, Toast.LENGTH_SHORT).show();
        }

        myadapter = new MyCustomAdapter(listnewsData);

        ListView lsNews = (ListView) findViewById(R.id.lv1);
        lsNews.setAdapter(myadapter);

    }

    //zapisanie updateu
    public void buUpdate(View view) {
        ContentValues values = new ContentValues();
        values.put(DBManager.colUserName,etUserName.getText().toString());
        values.put(DBManager.colPassword,etPassword.getText().toString());
        values.put(DBManager.colId,recordId);
        String[] selectionArgs = {String.valueOf(recordId)};
        dbManager.update(values,"ID=?",selectionArgs);
    }

    //adapter
    private class MyCustomAdapter extends BaseAdapter {

        public ArrayList<AdapterItems> listnewsDataAdapter;

        public MyCustomAdapter(ArrayList<AdapterItems> listnewsDataAdapter){
            this.listnewsDataAdapter=listnewsDataAdapter;
        }

        @Override
        public int getCount() {
            return listnewsDataAdapter.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket,null);

            final AdapterItems s = listnewsDataAdapter.get(position);

            TextView tvId = (TextView) myView.findViewById(R.id.tvId);
            tvId.setText(String.valueOf(s.id));

            TextView tvUserName = (TextView) myView.findViewById(R.id.tvUserName);
            tvUserName.setText(s.userName);

            TextView tvPassword = (TextView) myView.findViewById(R.id.tvPassword);
            tvPassword.setText(s.password);

            Button buDelete = (Button) myView.findViewById(R.id.buDelete);
            buDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] selectionArgs = {String.valueOf(s.id)};
                    int count = dbManager.delete("ID=?",selectionArgs);
                        if(count > 0){
                            loadElement();
                        }
                }
            });

            //załadowanie danych do odpowiednich pól w formularzu
            Button buUpdate = (Button) myView.findViewById(R.id.buUpdate);
            buUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etUserName.setText(s.userName);
                    etPassword.setText(s.password);
                    recordId = s.id;
                }
            });

            return myView;
        }
    }

}
