package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.Utils.JwtTokenUtils;
import com.nghianguyen.scnetwork.config.SecurityConfig;
import com.nghianguyen.scnetwork.dtos.UserDTO;
import com.nghianguyen.scnetwork.exception.DataNotFoundException;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
            throw new DataIntegrityViolationException("Your email has been registered");
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
}
