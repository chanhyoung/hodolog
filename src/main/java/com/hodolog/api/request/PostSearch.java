package com.hodolog.api.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// import static java.lang.Math.max;
// import static java.lang.Math.min;

@Getter
@Setter
@Builder
@ToString
public class PostSearch {
	private String category;
    private List<String> tags;
    private String sort;
	
    // private static final int MAX_SIZE = 2000;

    // @Builder.Default
    // private Integer page = 1;

    // @Builder.Default
    // private Integer size = 10;

    // public long getOffset() {
    //     return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    // }
}
