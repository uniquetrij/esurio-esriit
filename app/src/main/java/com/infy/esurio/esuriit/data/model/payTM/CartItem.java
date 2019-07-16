package com.infy.esurio.esuriit.data.model.payTM;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CartItem extends RealmObject {
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    public Product product;
    public int quantity = 0;
}
