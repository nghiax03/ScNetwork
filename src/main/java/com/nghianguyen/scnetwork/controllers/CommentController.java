package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.dtos.CommentDTO;
import com.nghianguyen.scnetwork.models.Comment;
import com.nghianguyen.scnetwork.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/{id}")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO,
                                           @PathVariable Long id)
        throws Exception {
        Comment newComment = commentService.createComment(commentDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId)
    {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) throws Exception {
        commentService.deleteByComment(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Delete comment successfully");
    }
}
