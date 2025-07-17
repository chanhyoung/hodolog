package com.hodolog.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatsResponse {
    private String category;
    private Integer postCount;
    private Double avgReadCount;
    private LocalDateTime latestPostDate;
}