
package com.example.a18127223_note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Details extends AppCompatActivity {


    private String name;
    private String time;
    private String tag;
    private String contentText;
    private TextView title;
    private TextView timeView;
    private TextView tagView;
    private AREditText contentView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        Intent receiver = getIntent();
        this.name = receiver.getStringExtra("Name");
        this.time = receiver.getStringExtra("Time");
        this.tag = receiver.getStringExtra("Tag");
        this.contentText = receiver.getStringExtra("Content");


        this.contentView = (AREditText) findViewById(R.id.detail_textView_Content);
        IARE_Toolbar ToolBar = (IARE_Toolbar)findViewById(R.id.Toolbar);
        CustomEditor cusEditor = new CustomEditor(ToolBar,contentView);

        this.title = (TextView)findViewById(R.id.detail_textView_Title);
        this.timeView = (TextView)findViewById(R.id.detail_textView_Time);
        this.tagView = (TextView)findViewById(R.id.detail_textView_Tag);


        title.setText(name);
        timeView.setText(time);
        tagView.setText(tag);
        contentView.fromHtml(contentText);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:{
                Intent data = new Intent();
                data.putExtra("name", title.getText().toString());
                data.putExtra("content", contentView.getHtml());
                data.putExtra("tag", tagView.getText().toString());
                setResult(RESULT_OK, data);
                finish();
                break;
            } case R.id.delete:{
                Intent data = new Intent();
                setResult(2, data);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}