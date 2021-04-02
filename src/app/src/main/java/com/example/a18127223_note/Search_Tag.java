package com.example.a18127223_note;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search_Tag extends AppCompatActivity {

    private ListView listView;
    private ArrayList<items> note = new ArrayList<>();
    private ArrayList<Integer> hide = new ArrayList<>();
    private ArrayList<items> note_Search = new ArrayList<>();
    private CustomArrayAdapter customArrayAdapter;
    private SearchView searchView_tag;
    private int curPos;
    public Intent data = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__tag);


        Intent receiver = getIntent();
        this.hide = receiver.getIntegerArrayListExtra("hide");
        int quantity = receiver.getIntExtra("quantity",0);
        String tmp = receiver.getStringExtra("notes");
        JSONObject notes;
        try {
            notes = new JSONObject(tmp);
            for(int i=0;i<quantity;i++){
                JSONObject itemTmp = (JSONObject) notes.get(String.valueOf(i));
                this.note.add( new items(itemTmp.get("title").toString(),
                        itemTmp.get("time").toString(),
                        itemTmp.get("tag").toString(),
                        itemTmp.get("content").toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView = (ListView) findViewById(R.id.listView_search_tag);
        customArrayAdapter = new CustomArrayAdapter(this,
                R.layout.items,note,hide);
        listView.setAdapter(customArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPos = position;
                data.putExtra("position",curPos);
                setResult(4,data);
                finish();
            }
        });



        searchView_tag = (SearchView)findViewById(R.id.input_search_tag);

        searchView_tag.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String tag_key = searchView_tag.getQuery().toString();
                listView.setAdapter(null);
                note_Search.clear();
                for(int i=0;i<note.size();i++){
                    if(note.get(i).tag.toLowerCase().contains(tag_key.toLowerCase())){
                        note_Search.add(note.get(i));
                    }
                }
                customArrayAdapter = new CustomArrayAdapter(Search_Tag.this,
                        R.layout.items,note_Search,hide);
                listView.setAdapter(customArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0;i<note.size();i++){
                            if(note.get(i)==note_Search.get(position)){
                                curPos = i;
                            }
                        }
                        data.putExtra("position",curPos);
                        setResult(4,data);
                        Search_Tag.this.finish();
                    }
                });
                return true;
            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };

    }

}