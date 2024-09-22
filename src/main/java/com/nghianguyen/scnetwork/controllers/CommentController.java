package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.dtos.CommentDTO;
import com.nghianguyen.scnetwork.models.Comment;
import com.nghianguyen.scnetwork.response.ResponseSuccess;
import com.nghianguyen.scnetwork.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseSuccess deleteComment(@PathVariable Long id, BindingResult result)
            throws Exception {
        if(result.hasErrors()){
            List<String> messages = result.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage)
                    .toList();
            return new ResponseSuccess(HttpStatus.BAD_REQUEST, "Cannot delete");
        }
        commentService.deleteCommentById(id);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "Delete comment successfully");
    }
}
