package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.dtos.CommentDTO;
import com.nghianguyen.scnetwork.models.Comment;
import com.nghianguyen.scnetwork.response.ResponseData;
import com.nghianguyen.scnetwork.response.ResponseError;
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
    public ResponseData<?> createComment(@RequestBody CommentDTO commentDTO,
                                         @PathVariable Long id)
        throws Exception {
        try{
            Comment newComment = commentService.createComment(commentDTO, id);
            return new ResponseData<>(HttpStatus.OK.value(),"Created a comment");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(),
                    "Cannot create comment: " + e.getMessage());
        }
    }
    @GetMapping("/post/{postId}")
    public ResponseData<List<Comment>> getCommentsByPostId(@PathVariable Long postId)
    {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return new ResponseData<>(HttpStatus.OK.value(), "get all comments");
    }

    @PutMapping("/{id}")
    public ResponseSuccess updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO)
            throws Exception {
        this.commentService.updateComment(id, commentDTO);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "Updated comment with id Cm: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseSuccess deleteComment(@PathVariable Long id)
            throws Exception {
        commentService.deleteCommentById(id);
        return new ResponseSuccess(HttpStatus.NO_CONTENT, "Delete comment successfully");
    }
}
