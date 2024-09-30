package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.exception.DataNotFoundException;
import com.nghianguyen.scnetwork.models.Like;
import com.nghianguyen.scnetwork.models.Post;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.repositories.LikeRepository;
import com.nghianguyen.scnetwork.repositories.PostRepository;
import com.nghianguyen.scnetwork.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;

@Service
@Transactional
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public boolean addLike(Long postId, Long loggedInUserId) throws Exception{
        Post post = postRepository.findById(postId).orElseThrow(null);
        User user = userRepository.findById(loggedInUserId).orElseThrow(null);
        if(post == null || user == null){
            throw new Exception("Bad req");
        }
        Like likeByPostAndUser = likeRepository.findByUserAndPost(user, post);
        if(likeByPostAndUser == null){
            Like like = Like
                    .builder()
                    .count(1L)
                    .user(user)
                    .post(post)
                    .build();
            return this.likeRepository.save(like) != null;
        }
        else {
            throw new InvalidParameterException("Failure post like messages");
        }
    }

    public int getAllLikesForPost(Long postId) throws Exception{
        Post post = postRepository.findById(postId).orElseThrow(null);
        if(post == null){
            throw new DataNotFoundException("Cannot find postId: " + postId);
        }
        return this.likeRepository.findAllByPost(post).size();
    }
}
