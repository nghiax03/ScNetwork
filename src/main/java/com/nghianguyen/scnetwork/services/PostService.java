package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.dtos.PostDTO;
import com.nghianguyen.scnetwork.exception.DataNotFoundException;
import com.nghianguyen.scnetwork.models.Post;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.repositories.PostRepository;
import com.nghianguyen.scnetwork.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    //post can update, create, delete, find?, pageable
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Post createPost(List<MultipartFile> files, String content, Long id)
            throws Exception{
        User exisitingUser = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot post content, please restart your login"));
        List<String> imagePaths = new ArrayList<>();
       for(MultipartFile file : files){
           String filePath = saveImage(file);
           imagePaths.add(filePath);
       }
        String imagePathsString = String.join(",", imagePaths);
       Post newPost = Post
               .builder()
               .imageUrl(imagePathsString)
               .user(exisitingUser)
               .content(content)
               .build();
       return postRepository.save(newPost);
    }

    private String saveImage(MultipartFile file) {
        String uploadDir = "uploads/";
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image:", e);
        }
    }

    //only update content or delete all
    public Post updatePost(Long id, PostDTO postDTO) throws DataNotFoundException {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find post content, come back"));
        existingPost.setContent(postDTO.getContent());
        return postRepository.save(existingPost);
    }

    public void deletePost(Long id) throws Exception{
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find post content, come back"));
        this.postRepository.deleteById(id);
    }

}
