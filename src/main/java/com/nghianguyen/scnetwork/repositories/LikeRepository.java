package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.Like;
import com.nghianguyen.scnetwork.models.Post;
import com.nghianguyen.scnetwork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndPost(User user, Post post);
    List<Like> findAllByPost(Post post);
}
