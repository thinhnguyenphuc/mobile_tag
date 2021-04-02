package com.example.a18127223_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;


import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;


import org.json.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss");
    private static final int STORAGE_PERMISSION_CODE = 3;
    ListView listView;
    ArrayList<Integer> hide = new ArrayList<>();

    CustomArrayAdapter customArrayAdapter;
    int curPos;


    ArrayList<items> note = new ArrayList<>();



    private static final int  REQUEST_CODE_OPEN = 1;
    private static final int  REQUEST_CODE_NEW = 2;
    private static final int  REQUEST_CODE_UN_HIDE = 3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        listView = (ListView)findViewById(R.id.listView);


        customArrayAdapter = new CustomArrayAdapter(MainActivity.this,
                R.layout.items,note,hide);
        listView.setAdapter(customArrayAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail_note = new Intent(MainActivity.this, Details.class);
                detail_note.putExtra("Name",note.get(position).Title);
                detail_note.putExtra("Time",note.get(position).time);
                detail_note.putExtra("Tag",note.get(position).tag);
                detail_note.putExtra("Content", note.get(position).content);
                curPos = position;
                startActivityForResult(detail_note, REQUEST_CODE_OPEN);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.popup);
                curPos = position;

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return menuItemClicked(item);
                    }
                });
                popup.show();
                return true;
            }
        });





    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_OPEN) {
            if(resultCode == Activity.RESULT_OK) {

                LocalDateTime now = LocalDateTime.now();
                items tmp = new items(data.getStringExtra("name"),timeFormat.format(now) ,data.getStringExtra("tag"), data.getStringExtra("content"));
                note.set(curPos, tmp);

            }
            else if(resultCode== 2){

                note.remove(curPos);
            }
        } else if(requestCode == REQUEST_CODE_NEW){
            if(resultCode== Activity.RESULT_OK){
                LocalDateTime now = LocalDateTime.now();
                items tmp = new items(data.getStringExtra("name"),timeFormat.format(now) ,data.getStringExtra("tag"), data.getStringExtra("content"));
                note.add(tmp);
            }
        }else if(requestCode == REQUEST_CODE_UN_HIDE){
            if(resultCode==3){
                int tmp = data.getIntExtra("position",0);
                for(int i=0;i<hide.size();i++){
                    if(hide.get(i)==tmp){
                        hide.remove(i);
                    }
                }
            }
        }
        else if(requestCode == 4){
            if(resultCode==4){
                int position = data.getIntExtra("position",0);
                Intent detail_note = new Intent(MainActivity.this, Details.class);
                detail_note.putExtra("Name",note.get(position).Title);
                detail_note.putExtra("Time",note.get(position).time);
                detail_note.putExtra("Tag",note.get(position).tag);
                detail_note.putExtra("Content", note.get(position).content);
                curPos = position;
                startActivityForResult(detail_note, REQUEST_CODE_OPEN);
            }
        }

        try {
            this.writeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        customArrayAdapter.clear();
        customArrayAdapter.addAll(note);
        customArrayAdapter.notifyDataSetChanged();
    }
    private void writeData() throws JSONException {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        SharedPreferences write= this.getSharedPreferences("notes",MODE_PRIVATE);
        SharedPreferences.Editor editor=write.edit();
        int quantity = this.note.size();
        editor.putInt("quantity",quantity);
        editor.putInt("hideQuantity",hide.size());
        JSONObject hideList = new JSONObject();
        for(int i=0;i<hide.size();i++){
            hideList.put(String.valueOf(i),hide.get(i));
        }
        editor.putString("hideList",hideList.toString());

        if(quantity==0){
            editor.commit();
        } else{
            for(int i=0;i<quantity;i++){
                JSONObject tmp = new JSONObject();
                tmp.put("title",this.note.get(i).Title);
                tmp.put("time",this.note.get(i).time);
                tmp.put("tag",this.note.get(i).tag);
                tmp.put("content",this.note.get(i).content);
                editor.putString(String.valueOf(i),tmp.toString());
                editor.commit();
            }
        }

    }
    private void readData() throws JSONException {
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        SharedPreferences read= this.getSharedPreferences("notes",MODE_PRIVATE);
        int quantity=read.getInt("quantity",0);
        JSONObject hideList = new JSONObject(read.getString("hideList",""));

        int hideQuantity = read.getInt("hideQuantity",0);


        for(int i=0;i<hideQuantity;i++){
            this.hide.add(hideList.getInt(String.valueOf(i)));
        }


        for(int i=0;i<quantity;i++){
            String tmp = read.getString(String.valueOf(i),"");
            JSONObject tmpJSON = new JSONObject (tmp);
            items itemTmp = new items((String) tmpJSON.get("title")
                    ,(String) tmpJSON.get("time")
                    ,(String) tmpJSON.get("tag")
                    ,(String) tmpJSON.get("content"));
            this.note.add(itemTmp);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            readData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {

        try {
            this.writeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPause();
    }
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission },
                    requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private boolean menuItemClicked(MenuItem item)  {
        switch (item.getItemId()) {
            case R.id.menu_delete:{
                this.note.remove(curPos);
                customArrayAdapter.notifyDataSetChanged();
                break;
            } case R.id.menu_change_tag:{
                change_tag();
                break;
            } case R.id.menu_hide:{
                this.hide.add(curPos);
                customArrayAdapter.notifyDataSetChanged();
                break;
            }
        }
        return true;
    }

    private void change_tag(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Tag");

        final EditText input = new EditText(MainActivity.this);
        input.setText(note.get(curPos).tag);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                items tmp =  note.get(curPos);
                tmp.tag = input.getText().toString();
                note.set(curPos,tmp);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {
                Intent detail_note = new Intent(MainActivity.this, Details.class);
                detail_note.putExtra("Name","");
                detail_note.putExtra("Time","");
                detail_note.putExtra("Tag","");
                detail_note.putExtra("Content", "");
                startActivityForResult(detail_note, REQUEST_CODE_NEW);
                break;
            } case R.id.unhide:{
                Intent un_hideNote = new Intent(MainActivity.this, un_hide_item.class);
                JSONObject tmpNotes = new JSONObject();
                for(int i=0;i<note.size();i++){
                    JSONObject tmp = new JSONObject();
                    try {
                        tmp.put("title",note.get(i).Title);
                        tmp.put("time",note.get(i).time);
                        tmp.put("tag",note.get(i).tag);
                        tmp.put("content",note.get(i).content);
                        tmpNotes.put(String.valueOf(i),tmp);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                un_hideNote.putExtra("quantity",note.size());
                un_hideNote.putExtra("notes",tmpNotes.toString());
                un_hideNote.putExtra("hide",hide);
                startActivityForResult(un_hideNote,REQUEST_CODE_UN_HIDE);
                break;
            } case R.id.search_tag:{
                Intent search_tag = new Intent(MainActivity.this, Search_Tag.class);
                JSONObject tmpNotes = new JSONObject();
                for(int i=0;i<note.size();i++){
                    JSONObject tmp = new JSONObject();
                    try {
                        tmp.put("title",note.get(i).Title);
                        tmp.put("time",note.get(i).time);
                        tmp.put("tag",note.get(i).tag);
                        tmp.put("content",note.get(i).content);
                        tmpNotes.put(String.valueOf(i),tmp);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                search_tag.putExtra("quantity",note.size());
                search_tag.putExtra("notes",tmpNotes.toString());
                search_tag.putExtra("hide",hide);
                startActivityForResult(search_tag,4);


                break;
            } case R.id.search_content:{
                Intent search_content = new Intent(MainActivity.this, Search_Content.class);
                JSONObject tmpNotes = new JSONObject();
                for(int i=0;i<note.size();i++){
                    JSONObject tmp = new JSONObject();
                    try {
                        tmp.put("title",note.get(i).Title);
                        tmp.put("time",note.get(i).time);
                        tmp.put("tag",note.get(i).tag);
                        tmp.put("content",note.get(i).content);
                        tmpNotes.put(String.valueOf(i),tmp);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                search_content.putExtra("quantity",note.size());
                search_content.putExtra("notes",tmpNotes.toString());
                search_content.putExtra("hide",hide);
                startActivityForResult(search_content,4);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}



