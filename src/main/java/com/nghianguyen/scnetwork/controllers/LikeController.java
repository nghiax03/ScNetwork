package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.response.ResponseSuccess;
import com.nghianguyen.scnetwork.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/add")
    public ResponseEntity<?> addLike(@RequestBody Map<String, Object> body) throws Exception {
        Long postId = ((Number) body.get("postId")).longValue();
        Long userId = ((Number) body.get("loggedInUserId")).longValue();

        boolean result = likeService.addLike(postId, userId);
        if(result){
            return new ResponseSuccess(HttpStatus.OK, "success like post");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
