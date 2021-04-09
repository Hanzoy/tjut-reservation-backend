package com.hanzoy.tjutreservation.pojo.dto.result;

import com.hanzoy.utils.ClassCopyUtils.CopyProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class GetMyReservationsResult {
    private ArrayList<Integer> months;
    private ArrayList<Meeting> info;

    @Data
    public static class Meeting{
        private Integer id;
        private String name;
        private String creator;
        @CopyProperty("roomId")
        private String meetingName;
        private String date;
        private String time;
        private String status;
    }
}
