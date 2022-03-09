package com.spring.sharepod.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardFilterAndCategoryRequestDto {
    private String filtertype;
    private String category;
    private String mapdata;


}
