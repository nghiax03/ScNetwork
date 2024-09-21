package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.dtos.CommentDTO;
import com.nghianguyen.scnetwork.exception.DataNotFoundException;
import com.nghianguyen.scnetwork.models.Comment;
import com.nghianguyen.scnetwork.models.Post;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.repositories.CommentRepository;
import com.nghianguyen.scnetwork.repositories.PostRepository;
import com.nghianguyen.scnetwork.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(CommentDTO commentDTO, Long id) throws Exception{
        User existingUser = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find user with id: " + commentDTO.getUserId()));
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find post with id: "+ id));
        Comment newComment = Comment
                .builder()
                .user(existingUser)
                .post(existingPost)
                .content(commentDTO.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        //there are 2 comments or more
        if(commentDTO.getParentId() != null){
            Comment parentComment = commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(()-> new DataNotFoundException("Parent comment not found"));
            newComment.setParentComment(parentComment);
        }
        return commentRepository.save(newComment);
    }

    public List<Comment> getCommentsByPostId(Long postId){
        return commentRepository.findByPostIdAndParentCommentIsNull(postId);
    }

    //comments are usually not editable
    public void deleteByComment(Long id) throws Exception{
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Comment is not exist"));
        commentRepository.deleteById(id);
    }
}
