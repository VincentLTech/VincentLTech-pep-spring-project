//com.example. is used for maven
//entity is the new model
//repository is the new DAO

package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Message> createMessage(Message message) {
        // Validate messageText and postedBy
        if (
                message.getMessageText() == null //checks to see if the message is empty
                || 
                message.getMessageText().trim().isEmpty() //checks to see without spaces wouldalso be empty
                ||
                message.getMessageText().length() > 255 //checks if it is more than a certain character
                || 
                message.getPostedBy() == null//check to see if the person who posted it is empty
            ) {

            // return ResponseEntity.status(400).body(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // This checks if an Account exists with the given username (postedBy)
        String username = getUsernameByAccountId(message.getPostedBy());
        if (
            username == null //checks to see if the message is empty
            || 
            !accountRepository.existsByUsername(username)//or if it exists
            ) {
            // return ResponseEntity.status(400).body(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Save the new message to the database
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    // Method to retrieve username based on accountId (postedBy)
    public String getUsernameByAccountId(Integer accountId) {
        // Fetch the Account entity from repository
        Account account = accountRepository.findByAccountId(accountId);

        // Return username if account is found, otherwise null
        return account != null ? account.getUsername() : null;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public ResponseEntity<Message> getMessageById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        //checks to see if the message is there
        if (optionalMessage.isPresent()) {
            
            return ResponseEntity.ok(optionalMessage.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<Object> deleteMessageById(Integer messageId) {
        // Check if the message with the given ID exists
        if (messageRepository.existsById(messageId)) {
            //this then deletes it
            messageRepository.deleteById(messageId);
            return ResponseEntity.ok().body("1");
        } else {
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<Object> updateMessageText(Integer messageId, Message newMessageText) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
    
        if (optionalMessage.isEmpty()) {
            // return ResponseEntity.status(400).body("Message not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message not found");
        }
    
        Message message = optionalMessage.get();
    
        // Check if newMessageText is null or empty after trimming
        if (newMessageText.getMessageText() == null || newMessageText.getMessageText().trim().isEmpty()) {
            // return ResponseEntity.status(400).body("Message text cannot be empty");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message text cannot be empty");
        }
    
        // Check if newMessageText exceeds 255 characters
        if (newMessageText.getMessageText().length() > 255) {
            // return ResponseEntity.status(400).body("Message too long: it must have a length of at most 255 characters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message too long: it must have a length of at most 255 characters");
        }
    
        // Update message text and save
        message.setMessageText(newMessageText.getMessageText());
        messageRepository.save(message);
    
        // Return success response. The "1" represents the number of rows updated which is only 1
        return ResponseEntity.ok().body("1"); // Assuming "1" represents the number of rows updated
    }

    public ResponseEntity<List<Message>> getMessagesByAccountId(Integer accountId) {
        //this retrieves the message through the id you pulled.
        List<Message> messages = messageRepository.findByPostedBy(accountId);
        return ResponseEntity.ok(messages);
    }
}