package com.infy.esurio.esuriit.data.model.payTM;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    public long id;
    public String name;
    public String email;
    public String token;
}
