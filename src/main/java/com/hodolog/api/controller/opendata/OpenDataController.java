package com.hodolog.api.controller.opendata;

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
    public void get() {
        openDataService.getBidPblancListInfoCnstwk("1", "20250701", "20250731");
        log.info("OpenDataController getBidPblancListInfoCnstwk called");}
}