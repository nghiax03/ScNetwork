package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.Utils.JwtTokenUtils;
import com.nghianguyen.scnetwork.config.SecurityConfig;
import com.nghianguyen.scnetwork.dtos.UserDTO;
import com.nghianguyen.scnetwork.exception.DataNotFoundException;
import com.nghianguyen.scnetwork.exception.DuplicateEmailException;
import com.nghianguyen.scnetwork.models.PictureMain;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.repositories.AvatarRepository;
import com.nghianguyen.scnetwork.repositories.UserRepository;
import com.nghianguyen.scnetwork.response.ResponseSuccess;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostConstruct
    public void isOnlineSetup(){
        if(this.userRepository.count() > 0){
            this.userRepository.setIsOnlineToFalse();
        }
    }

    public User register(UserDTO userDTO) throws Exception {
        String email = userDTO.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new DuplicateEmailException("Email has been registered, please try other email");
        }
        User newUser = userDTO.fromUser();
        String passwordEncode = passwordEncoder.encode(userDTO.getPassword());
        //authorize
        newUser.setPassword(passwordEncode);
        return userRepository.save(newUser);
    }

    public String login(String email, String password) throws  Exception{
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isEmpty()){
            throw new DataNotFoundException("Invalid email / password");
        }

        User newUser = existingUser.get();
        if(!passwordEncoder.matches(password, newUser.getPassword())){
            throw new InvalidParameterException("Password does not match");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password, newUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingUser.get());

    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public PictureMain uploadAvatar(MultipartFile file, String content, Long userId) throws DataNotFoundException {
        User userLoggedInID = userRepository
                .findById(userId).orElseThrow(() -> new DataNotFoundException("Cannot find userId: " + userId));
        List<String> imagePaths = new ArrayList<>();

        String filePath = saveImage(file);
        imagePaths.add(filePath);

        String imagePathString = String.join(",", imagePaths);
        PictureMain newAvatar = PictureMain
                .builder()
                .user(userLoggedInID)
                .filePath(imagePathString)
                .uploadTime(LocalDateTime.now())
                .build();
        return avatarRepository.save(newAvatar);
    }

    private String saveImage(MultipartFile file){
        String uploadDir = "uploads/";
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image:", e);
        }
    }
}
