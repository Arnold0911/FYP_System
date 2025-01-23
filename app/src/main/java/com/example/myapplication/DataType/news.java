package com.example.myapplication.DataType;

public class news {
    String name, context;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public news(String name, String context) {
        this.name = name;
        this.context = context;
    }
}
