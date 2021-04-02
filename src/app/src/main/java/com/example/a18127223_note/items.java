package com.example.a18127223_note;

import java.util.Date;

public class items {
    String Title;
    String time;
    String tag;
    String content;
    ;
    public items(String Title, String time, String tag, String content) {
        this.Title = Title;
        this.time = time;
        this.tag = tag;
        this.content = content;
    }
    public String getTile() {
        return Title;
    }
    public String getTime() {
        return time;
    }
    public String getTag(){return tag;}
    public String getContent(){return content;}
    @Override
    public String toString() {
        return "items{" +
                "tilte='" + Title + '\'' +
                ", time='" + time + '\'' +
                ", tag=" + tag +
                '}';
    }
}
