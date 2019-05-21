package com.denes.myflower;

import java.util.List;


public class Flowers {

    private String name;
    private String statement;
    private int photo_id;
    public List<Flowers> flowers_list;
    public String getName()
    {
        return this.name;
    }
    public String getStatement()
    {
        return this.statement;
    }
    public int getPhoto_id()
    {
        return this.photo_id;
    }

    public Flowers(String name, String statement, int photo_id)
    {
        this.name = name;
        this.statement = statement;
        this.photo_id = photo_id;
    }
    public Flowers()
    {

    }


}
