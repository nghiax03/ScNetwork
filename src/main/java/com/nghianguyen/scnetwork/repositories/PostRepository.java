package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
