package com.hodolog.api.controller.opendata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class testmain {
  public static void main(String[] args) {
    String startDay = "202501";
    String endDay = "202508";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate startDate = LocalDate.parse(startDay + "01", formatter);
    LocalDate endDate = LocalDate.parse(endDay + "01", formatter);


    while (!startDate.isAfter(endDate)) {
      LocalDate lastDayOfMonth = startDate.withDayOfMonth(startDate.lengthOfMonth());

      System.out.print("Start Date: " + startDate.format(formatter));
      System.out.println(", Last Date: " + lastDayOfMonth.format(formatter));

      startDate = startDate.plusMonths(1);
    }
  }
}
