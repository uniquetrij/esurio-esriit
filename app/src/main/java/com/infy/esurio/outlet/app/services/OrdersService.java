package com.infy.esurio.outlet.app.services;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.infy.esurio.middleware.DTO.OrdersDTO;
import com.infy.esurio.outlet.app.This;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;

import org.apache.commons.lang3.text.WordUtils;

public class OrdersService {

    private static class FakerService {

        public static ObservableList<OrdersDTO> fetch() {
            ObservableList<OrdersDTO> list = new ObservableArrayList<>();
            OrdersDTO dto = null;
            dto = new OrdersDTO();

            dto.setIdentifier("#766450");
            list.add(dto);

            dto.setIdentifier("#766451");
            list.add(dto);

            dto.setIdentifier("#766452");
            list.add(dto);

            dto.setIdentifier("#766453");
            list.add(dto);

            dto.setIdentifier("#7566454");
            list.add(dto);

            dto.setIdentifier("#766455");
            list.add(dto);


            return list;
        }

        public static void putNumber(OrdersDTO dto, TextView view) {
            view.setText(WordUtils.capitalizeFully(dto.getIdentifier()));
        }
    }

    public static ObservableList<OrdersDTO> fetch(){
        This.ORDERS.clear();
        This.ORDERS.addAll(FakerService.fetch());
        return This.ORDERS;
    }

    public static void putNumber(OrdersDTO dto, TextView view){
        FakerService.putNumber(dto,view);
    }
}
