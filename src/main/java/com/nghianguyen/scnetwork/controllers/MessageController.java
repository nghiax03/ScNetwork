package com.nghianguyen.scnetwork.controllers;

import com.nghianguyen.scnetwork.dtos.MessageDTO;
import com.nghianguyen.scnetwork.models.Message;
import com.nghianguyen.scnetwork.response.ResponseSuccess;
import com.nghianguyen.scnetwork.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseSuccess createMessage(@RequestBody MessageDTO messageDTO) //Authentication, before add filter
            throws Exception {
        try {
            //update sql too foreign Key(render_id, receiver_id) reference user(id) on Delete cascade
            // // Lấy người gửi từ token (authentication)
            //        String senderUsername = authentication.getName();
            Message newMess = messageService
                    .createMessage(messageDTO.getSenderId(), messageDTO.getReceiverId(), messageDTO.getContent());
            return new ResponseSuccess(HttpStatus.CREATED, "Created new message", newMess);
        } catch (Exception e) {
            return new ResponseSuccess(HttpStatus.BAD_REQUEST, "Cannot create message");
        }
    }

    @PutMapping("/{id}")
    public ResponseSuccess updateMess(@PathVariable Long id,
                                      @RequestBody MessageDTO messageDTO) throws Exception{
        try {
            Message updatedMess = messageService.updateMessage(id, messageDTO);
            return new ResponseSuccess(HttpStatus.ACCEPTED,"Updated mess success");
        } catch (Exception e){
            return new ResponseSuccess(HttpStatus.BAD_REQUEST, "Cannot update mess, err:" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseSuccess deleteMess(@PathVariable Long id) throws Exception{
        messageService.deleteMessage(id);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "Delete success id: " + id);
    }
}
