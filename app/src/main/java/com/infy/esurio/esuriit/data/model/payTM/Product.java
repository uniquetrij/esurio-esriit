package com.infy.esurio.esuriit.data.model.payTM;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject {
    @PrimaryKey
    public long id;
    public String name;
    @SerializedName("image")
    public String imageUrl;
    public float price;
}