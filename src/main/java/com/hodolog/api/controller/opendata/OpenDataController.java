package com.hodolog.api.controller.opendata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/openapi")
@RequiredArgsConstructor    
public class OpenDataController {

  private final OpenDataService openDataService;

  @GetMapping("/getBidPblancListInfoCnstwk")
  public void bidPblancListInfoCnstwk() {
    String startMonth = "202508";
    String endMonth = "202508";

    // 로그 파일 생성
    String logFileName = String.format("bidPblancListInfoCnstwk_%s_%s_%s.log", startMonth, endMonth,
      LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    FileLogger fileLogger = new FileLogger(logFileName);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate startDate = LocalDate.parse(startMonth + "01", formatter);
    LocalDate endDate = LocalDate.parse(endMonth + "01", formatter);

    while (!startDate.isAfter(endDate)) {
      LocalDate lastDayOfMonth = startDate.withDayOfMonth(startDate.lengthOfMonth());

    //   openDataService.getBidPblancListInfoCnstwk("1", "20250701", "20250731");
      openDataService.getBidPblancListInfoCnstwk(fileLogger, "1", startDate.format(formatter), lastDayOfMonth.format(formatter));

      startDate = startDate.plusMonths(1);
    }
  }
}