package com.infy.esurio.esuriit.app.services;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.infy.esurio.R;
import com.infy.esurio.esuriit.app.This;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;

import org.apache.commons.lang3.text.WordUtils;

import static com.infy.esurio.esuriit.app.This.COLORS.GREEN;
import static com.infy.esurio.esuriit.app.This.COLORS.ORANGE;
import static com.infy.esurio.esuriit.app.This.COLORS.RED;

public class FoodcourtService {

    private static class FakerService {

        public static ObservableList<FoodcourtsDTO> foodcourtsFetch() {
            ObservableList<FoodcourtsDTO> list = new ObservableArrayList<>();
            FoodcourtsDTO dto = null;
            dto = new FoodcourtsDTO();
            dto.setIdentifier("heritage");
            list.add(dto);

            dto = new FoodcourtsDTO();
            dto.setIdentifier("terminal");
            list.add(dto);

            dto = new FoodcourtsDTO();
            dto.setIdentifier("b47");
            list.add(dto);

            dto = new FoodcourtsDTO();
            dto.setIdentifier("golconda");
            list.add(dto);

            dto = new FoodcourtsDTO();
            dto.setIdentifier("lotus");
            list.add(dto);


            return list;
        }

        public static void foodcourtsPutImage(FoodcourtsDTO dto, ImageView view) {
            try {
                view.setImageDrawable(This.CONTEXT.self().getResources().getDrawable(R.drawable.class.getField(dto.getIdentifier()).getInt(null), This.CONTEXT.self().getTheme()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public static void foodcourtsPutName(FoodcourtsDTO dto, TextView view) {
            view.setText(WordUtils.capitalizeFully(dto.getIdentifier()));
        }

        public static void foodcourtsPutDistance(FoodcourtsDTO dto, TextView view) {
            Integer distance = null;
            switch (dto.getIdentifier()) {
                case "heritage":
                    distance = 250;
                    break;
                case "terminal":
                    distance = 300;
                    break;
                case "golconda":
                    distance = 300;
                    break;
                case "lotus":
                    distance = 350;
                    break;
                case "b47":
                    distance = 400;
                    break;
            }
            view.setText(distance + " mts.");
        }

        public static void foodcourtsPutNoise(FoodcourtsDTO dto, ProgressBar view) {
            Integer color = null;
            Integer mins = null;
            switch (dto.getIdentifier()) {
                case "heritage":
                    mins = 15;
                    color = ORANGE;
                    break;
                case "terminal":
                    mins = 10;
                    color = GREEN;
                    break;
                case "golconda":
                    mins = 5;
                    color = GREEN;
                    break;
                case "lotus":
                    mins = 20;
                    color = RED;
                    break;
                case "b47":
                    mins = 5;
                    color = GREEN;
                    break;

            }
            view.setProgressTintList(ColorStateList.valueOf(color));
            view.setProgress(mins);
        }

        public static void foodcourtsPutOccupancy(FoodcourtsDTO dto, ProgressBar view) {
            Integer color = null;
            Integer occupancy = null;
            switch (dto.getIdentifier()) {
                case "heritage":
                    color = RED;
                    occupancy = 90;
                    break;
                case "terminal":
                    color = ORANGE;
                    occupancy = 60;
                    break;
                case "golconda":
                    color = GREEN;
                    occupancy = 20;
                    break;
                case "lotus":
                    color = GREEN;
                    occupancy = 30;
                    break;
                case "b47":
                    color = RED;
                    occupancy = 80;
                    break;

            }

            view.setProgressTintList(ColorStateList.valueOf(color));
            view.setProgress(occupancy);
        }

        public static void foodcourtsPutMapIntent(FoodcourtsDTO dto, ImageView view) {
            String geo = null;
            switch (dto.getIdentifier()) {
                case "heritage":
                    geo = "12.847412,77.664573(Heritage)";
                    break;
                case "terminal":
                    geo = "12.849394,77.667225(Terminal)";
                    break;
                case "golconda":
                    geo = "12.847304,77.663318(Golconda)";
                    break;
                case "lotus":
                    geo = "12.848500,77.666972(Lotus)";
                    break;
                case "b47":
                    geo = "12.850317,77.667397(B+47)";
                    break;

            }
            final String finalGeo = geo;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + finalGeo);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    This.MAIN_ACTIVITY.self().startActivity(mapIntent);
                }
            });
        }
    }

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

    public static void putNoise(FoodcourtsDTO dto, ProgressBar view){
        FakerService.foodcourtsPutNoise(dto,view);
    }

    public static void putOccupancy(FoodcourtsDTO dto, ProgressBar view){
        FakerService.foodcourtsPutOccupancy(dto,view);
    }

    public static void putMapIntent(FoodcourtsDTO foodcourtsDTO, ImageView iv_location) {
        FakerService.foodcourtsPutMapIntent(foodcourtsDTO,iv_location);
    }
}
