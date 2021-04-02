package com.example.a18127223_note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class un_hide_item extends AppCompatActivity {

    ListView listView;
    ArrayList<Integer> hide = new ArrayList<>();

    CustomAdapter_Hide customAdapter_Hide;

    ArrayList<items> note = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_hide_item);

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






        listView = (ListView)findViewById(R.id.test);

        customAdapter_Hide = new CustomAdapter_Hide(un_hide_item.this,
                R.layout.items,note,hide);
        listView.setAdapter(customAdapter_Hide);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("position",position);
                setResult(3, data);
                finish();
            }
        });





    }
}