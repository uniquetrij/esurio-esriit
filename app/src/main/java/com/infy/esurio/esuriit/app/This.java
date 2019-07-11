package com.infy.esurio.esuriit.app;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableList;

import com.github.nkzawa.socketio.client.Socket;
import com.infy.esurio.esuriit.activities.main.MainActivity;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;
import com.infy.esurio.middleware.DTO.OrdersDTO;
import com.infy.esurio.middleware.DTO.OutletsDTO;
import com.infy.esurio.middleware.DTO.ServingsDTO;

import java.util.ArrayList;
import java.util.List;

//import com.infy.esurio.middleware.DTO.OrdersDTO;

public class This {

    public static class Static {
        public static final String URL = "http://192.168.1.100:5000";
    }

    public static class Wrapper<T> {
        public T self = null;

        public void wrap(T t) {
            this.self = t;
        }

        public T self() {
            return self;
        }
    }

    public static final Wrapper<Socket> SOCKET = new Wrapper<>();
    public static final Wrapper<Context> CONTEXT = new Wrapper<>();
    public static final Wrapper<Application> APPLICATION = new Wrapper<>();
    public static final Wrapper<MainActivity> MAIN_ACTIVITY = new Wrapper<>();
    public static final Wrapper<NotificationManager> NOTIFICATION_MANAGER = new Wrapper<>();
    public static final Wrapper<String> FCM_TOKEN = new Wrapper<>();


    public static final ObservableList<FoodcourtsDTO> FOODCOURTS = new ObservableArrayList<>();
}
