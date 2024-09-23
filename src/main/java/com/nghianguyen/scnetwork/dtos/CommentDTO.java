package com.nghianguyen.scnetwork.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("parent_id")
    private Long parentId;
    private String content;
    @JsonProperty("post_id")
    private Long postId;
}
