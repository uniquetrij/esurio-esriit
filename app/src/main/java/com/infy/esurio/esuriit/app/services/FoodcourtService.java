package com.infy.esurio.esuriit.app.services;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.ObservableList;

import com.infy.esurio.R;
import com.infy.esurio.esuriit.app.This;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;

public class FoodcourtService {

    public static int ORANGE = Color.parseColor("#ff9900");
    public static int GREEN = Color.parseColor("#006633");
    public static int RED = Color.parseColor("#990000");

    public static ObservableList<FoodcourtsDTO> fetch(){
        This.FOODCOURTS.clear();
        This.FOODCOURTS.addAll(FakerService.foodcourtsFetch());
        return This.FOODCOURTS;
    }

    public static void putImage(FoodcourtsDTO dto, ImageView view){
        FakerService.foodcourtsPutImage(dto,view);
    }

    public static void putName(FoodcourtsDTO dto, TextView view){
        FakerService.foodcourtsPutName(dto,view);
    }

    public static void putDistance(FoodcourtsDTO dto, TextView view){
        FakerService.foodcourtsPutDistance(dto,view);
    }

    public static void putServiceTime(FoodcourtsDTO dto, TextView view){
        FakerService.foodcourtsPutServiceTime(dto,view);
    }

    public static void putOccupancy(FoodcourtsDTO dto, TextView view){
        FakerService.foodcourtsPutOccupancy(dto,view);
    }

    public static void putMapIntent(FoodcourtsDTO foodcourtsDTO, ImageView iv_location) {
        FakerService.foodcourtsPutMapIntent(foodcourtsDTO,iv_location);
    }
}
