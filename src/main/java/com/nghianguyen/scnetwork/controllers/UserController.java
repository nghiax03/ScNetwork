package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.Utils.LocalizationUtils;
import com.nghianguyen.scnetwork.Utils.MessageKeys;
import com.nghianguyen.scnetwork.dtos.UserDTO;
import com.nghianguyen.scnetwork.dtos.UserLoginDTO;
import com.nghianguyen.scnetwork.models.Message;
import com.nghianguyen.scnetwork.models.PictureMain;
import com.nghianguyen.scnetwork.models.User;
import com.nghianguyen.scnetwork.response.LoginResponse;
import com.nghianguyen.scnetwork.response.ResponseSuccess;
import com.nghianguyen.scnetwork.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LocalizationUtils localizationUtils;
//    @Autowired
//    private ApiPrefixCheck apiPrefixCheck;

    @GetMapping
    public ResponseEntity<?> getData(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }
    @PostMapping("/register")
    public ResponseSuccess register(@RequestBody @Valid UserDTO userDTO,
                                       BindingResult result) throws Exception{
            User newUser = userService.register(userDTO);
            return new ResponseSuccess(HttpStatus.CREATED, "Register success");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO userLoginDTO,
                                   HttpServletRequest request)
        throws Exception {
        try{
            String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            return ResponseEntity.ok(
                    LoginResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                   LoginResponse
                           .builder()
                           .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED))
                           .build()
            );
        }
    }

    @PostMapping("/upload_avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file,
                                        @RequestParam("content") String content,
                                        @RequestParam("id") Long userId) throws Exception {
        try {
            if(userId == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if(file.getSize() > 10 * 1024 * 1024){
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                        "File is too large! Maxium size is 10MB");
            }

            String contentType = file.getContentType();
            if(contentType == null){
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }
            PictureMain pictureMain = userService.uploadAvatar(file, content, userId);
            return ResponseEntity.status(HttpStatus.OK).body("set avatar success");
        }
        catch (Exception e){
            return null;
        }
    }
}
