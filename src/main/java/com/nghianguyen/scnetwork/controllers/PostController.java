package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.dtos.PostDTO;
import com.nghianguyen.scnetwork.models.Post;
import com.nghianguyen.scnetwork.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("${api.prefix}/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestParam("files") List<MultipartFile> files,
                                        @RequestParam("content") String content,
                                        @PathVariable("id") Long userId) throws Exception {
        try{
            for(MultipartFile file : files){
                if(file.getSize() > 10 * 1024 * 1024){
                    throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                            "File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("FIle must be an image");
                }
            }
            Post newPost = postService.createPost(files, content, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContentPost(@PathVariable Long id, @RequestBody PostDTO postDTO)
        throws Exception {
        try {
           Post updatedPost = postService.updatePost(id, postDTO);
           return ResponseEntity.ok().body(updatedPost);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) throws Exception{
        try{
            postService.deletePost(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Delete post successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
