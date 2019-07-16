package com.infy.esurio.esuriit.app.services;

import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.infy.esurio.R;
import com.infy.esurio.esuriit.app.This;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;
import com.infy.esurio.middleware.DTO.OutletsDTO;
import com.infy.esurio.middleware.DTO.ServingsDTO;

import org.apache.commons.lang3.text.WordUtils;

import static com.infy.esurio.esuriit.app.This.COLORS.GREEN;
import static com.infy.esurio.esuriit.app.This.COLORS.RED;


public class ServingsService {

    private static class FakerService {


        public static ObservableList<ServingsDTO> fetch() {
            ObservableList<ServingsDTO> list = new ObservableArrayList<>();
            ServingsDTO dto = null;
            dto = new ServingsDTO();
            dto.setIdentifier("masala dosa");
            list.add(dto);

            dto = new ServingsDTO();
            dto.setIdentifier("chicken biriyani");
            list.add(dto);

            dto = new ServingsDTO();
            dto.setIdentifier("mixed noodles");
            list.add(dto);

            dto = new ServingsDTO();
            dto.setIdentifier("egg friedrice");
            list.add(dto);

            dto = new ServingsDTO();
            dto.setIdentifier("chicken shawarma");
            list.add(dto);

            dto = new ServingsDTO();
            dto.setIdentifier("chilli paneer");
            list.add(dto);

            return list;
        }


        public static void putImage(ServingsDTO dto, ImageView view) {
            try {
                view.setImageDrawable(This.CONTEXT.self().getResources().getDrawable(R.drawable.class.getField(dto.getIdentifier().replace(" ", "_")).getInt(null), This.CONTEXT.self().getTheme()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public static void putName(ServingsDTO dto, TextView view) {
            view.setText(WordUtils.capitalizeFully(dto.getIdentifier()));
        }

        public static void putPrice(ServingsDTO dto, TextView view) {
            Integer price = null;
            switch (dto.getIdentifier()) {
                case "masala dosa":
                    price = 45;
                    break;
                case "chicken biriyani":
                    price = 88;
                    break;
                case "mixed noodles":
                    price = 95;
                    break;
                case "egg friedrice":
                    price = 77;
                    break;
                case "chicken shawarma":
                    price = 65;
                    break;
                case "chilli paneer":
                    price = 70;
                    break;
            }

            view.setText("₹ " + price);
        }

        public static void putInfo(ServingsDTO dto, ImageView view) {
            Integer color = null;
            switch (dto.getIdentifier()) {
                case "masala dosa":
                    color = GREEN;
                    break;
                case "chicken biriyani":
                    color = RED;
                    break;
                case "mixed noodles":
                    color = RED;
                    break;
                case "egg friedrice":
                    color = RED;
                    break;
                case "chicken shawarma":
                    color = RED;
                    break;
                case "chilli paneer":
                    color = GREEN;
                    break;
            }
            System.out.println("COLOR: "+ color);

            view.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }


    public static ObservableList<ServingsDTO> fetch() {
        This.SERVINGS.clear();
        This.SERVINGS.addAll(FakerService.fetch());
        return This.SERVINGS;
    }

    public static void putImage(ServingsDTO dto, ImageView view) {
        FakerService.putImage(dto, view);
    }

    public static void putName(ServingsDTO dto, TextView view) {
        FakerService.putName(dto, view);
    }

    public static void putPrice(ServingsDTO dto, TextView view) {
        FakerService.putPrice(dto, view);
    }

    public static void putInfo(ServingsDTO dto, ImageView view) {
        FakerService.putInfo(dto, view);
    }
}
