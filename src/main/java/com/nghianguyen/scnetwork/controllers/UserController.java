package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.dtos.UserDTO;
import com.nghianguyen.scnetwork.dtos.UserLoginDTO;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.response.LoginResponse;
import com.nghianguyen.scnetwork.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;
//    @Autowired
//    private ApiPrefixCheck apiPrefixCheck;

    @GetMapping
    public ResponseEntity<?> getData(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO,
                                      BindingResult result) throws Exception{
        try {
            if(result.hasErrors()){
                List<String> messages = result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(messages);
            }
            User newUser = userService.register(userDTO);
            return ResponseEntity.ok().body(newUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO userLoginDTO,
                                   HttpServletRequest request)
        throws Exception {
        try{
            Locale locale = localeResolver.resolveLocale(request);
            String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            return ResponseEntity.ok(
                    LoginResponse
                            .builder()
                            .message(messageSource.getMessage("user.login.login_successfully", null, locale))
                            .token(token)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
