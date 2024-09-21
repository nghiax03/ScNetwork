package com.nghianguyen.scnetwork.response;

import com.nghianguyen.scnetwork.models.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private List<Comment> replies;  // Danh sách các phản hồi
    private Comment parentId;  // ID của bình luận cha (nếu có)
}
