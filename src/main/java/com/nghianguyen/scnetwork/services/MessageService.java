package com.nghianguyen.scnetwork.services;

import com.nghianguyen.scnetwork.dtos.MessageDTO;
import com.nghianguyen.scnetwork.exception.DataNotFoundException;
import com.nghianguyen.scnetwork.models.Message;
import com.nghianguyen.scnetwork.repositories.MessageRepository;
import com.nghianguyen.scnetwork.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PostRepository postRepository;

    public Message createMessage(Long senderId, Long receiverId, String content){
        Message newMessage =
                Message
                        .builder()
                        .senderId(senderId)
                        .receiverId(receiverId)
                        .content(content)
                        .sentAt(new Date())
                        .build();
        return messageRepository.save(newMessage);
    }

    public List<Message> getAllMessage(Long userId){
        return messageRepository.findByReceiverId(userId);
    }

    public Message updateMessage(Long id, MessageDTO messageDTO) throws DataNotFoundException {
        Message existingMess = messageRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find message with id: " + id));
        existingMess.setContent(messageDTO.getContent());
        return messageRepository.save(existingMess);
    }

    public void deleteMessage(Long id) throws Exception{
        Message message = messageRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find message with id: " + id));
        messageRepository.deleteById(id);
    }
}
