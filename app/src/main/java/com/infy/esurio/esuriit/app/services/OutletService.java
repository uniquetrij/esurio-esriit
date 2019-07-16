package com.infy.esurio.esuriit.app.services;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.infy.esurio.R;
import com.infy.esurio.esuriit.app.This;
import com.infy.esurio.middleware.DTO.OutletsDTO;

import org.apache.commons.lang3.text.WordUtils;

import static com.infy.esurio.esuriit.app.This.COLORS.GREEN;
import static com.infy.esurio.esuriit.app.This.COLORS.ORANGE;
import static com.infy.esurio.esuriit.app.This.COLORS.RED;


public class OutletService {

    private static class FakerService {


        public static ObservableList<OutletsDTO> outletsFetch() {
            ObservableList<OutletsDTO> list = new ObservableArrayList<>();
            OutletsDTO dto = null;
            dto = new OutletsDTO();
            dto.setIdentifier("empire");
            list.add(dto);

            dto = new OutletsDTO();
            dto.setIdentifier("freshly");
            list.add(dto);

            dto = new OutletsDTO();
            dto.setIdentifier("coffeby2");
            list.add(dto);

            dto = new OutletsDTO();
            dto.setIdentifier("dominos");
            list.add(dto);

            dto = new OutletsDTO();
            dto.setIdentifier("ccd");
            list.add(dto);
            ;


            return list;
        }

        public static void outletsPutImage(OutletsDTO dto, ImageView view) {
            try {
                view.setImageDrawable(This.CONTEXT.self().getResources().getDrawable(R.drawable.class.getField(dto.getIdentifier()).getInt(null), This.CONTEXT.self().getTheme()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public static void outletsPutName(OutletsDTO dto, TextView view) {
            view.setText(WordUtils.capitalizeFully(dto.getIdentifier()));
        }

        public static void outletsPutRating(OutletsDTO dto, RatingBar view) {
            Integer stars = 4;
            switch (dto.getIdentifier()) {
                case "empire":
                    stars = 3;
                    break;
                case "freshly":
                    stars = 4;
                    break;
                case "coffeby2":
                    stars = 3;
                    break;
                case "dominos":
                    stars = 2;
                    break;
                case "ccd":
                    stars = 5;
                    break;
            }
            view.setNumStars(stars);
        }

        public static void outletsPutServiceTime(OutletsDTO dto, TextView view) {
            Integer mins = null;
            switch (dto.getIdentifier()) {
                case "empire":
                    mins = 15;
                    break;
                case "freshly":
                    mins = 10;
                    break;
                case "coffeby2":
                    mins = 5;
                    break;
                case "dominos":
                    mins = 20;
                    break;
                case "ccd":
                    mins = 5;
                    break;

            }

            if (mins <= 5) {
                view.setTextColor(GREEN);
            } else if (mins >= 15) {
                view.setTextColor(RED);
            } else {
                view.setTextColor(ORANGE);
            }
            view.setText(mins + " mins");
        }
    }



    public static ObservableList<OutletsDTO> fetch() {
        This.OUTLETS.clear();
        This.OUTLETS.addAll(FakerService.outletsFetch());
        return This.OUTLETS;
    }

    public static void putImage(OutletsDTO dto, ImageView view) {
        FakerService.outletsPutImage(dto, view);
    }

    public static void putName(OutletsDTO dto, TextView view) {
        FakerService.outletsPutName(dto, view);
    }

    public static void putServiceTime(OutletsDTO dto, TextView view) {
        FakerService.outletsPutServiceTime(dto, view);
    }

    public static void putRating(OutletsDTO dto, RatingBar view) {
        FakerService.outletsPutRating(dto, view);
    }
}
