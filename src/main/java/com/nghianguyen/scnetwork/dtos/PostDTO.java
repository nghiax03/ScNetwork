package com.nghianguyen.scnetwork.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDTO {
    private Long userId;
    private String content;
    @JsonProperty("image_url")
    private String imageUrl;
}
