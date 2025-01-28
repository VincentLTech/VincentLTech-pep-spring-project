// /**
//  * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
//  * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
//  * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
//  * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
//  */
// public class SocialMediaController {
// }

package com.example.controller;
import java.util.List;

// import Model.Account;
import com.example.entity.Account;

// import Model.Message;
import com.example.entity.Message;

// import Service.AccountService;

// import Service.MessageService;

// import io.javalin.Javalin;
// import io.javalin.http.Context;


import com.example.service.AccountService;
import com.example.service.MessageService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocialMediaController {

    private final AccountService accountService;//     AccountService accountService = new AccountService();
    private final MessageService messageService;//     MessageService messageService = new MessageService();

    @Autowired
    public SocialMediaController( AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")//app.post("/register", this::postAccount);
    public ResponseEntity<Account> postAccount(@RequestBody Account account) {
        return accountService.postAccount(account);
    }
        // private void postAccount(Context ctx) throws JsonProcessingException {
        //     ObjectMapper mapper = new ObjectMapper();
        //     Account account = mapper.readValue(ctx.body(), Account.class);
        //     Account registeredAccount = accountService.createAccount(account);
        //     if(registeredAccount==null){
        //         ctx.status(400);
        //     }
        //     else{
        //         ctx.json(mapper.writeValueAsString(registeredAccount));
        //     }
        // }

    @PostMapping("/login")//app.post("/login", this::loginAccount);
    public ResponseEntity<Object> login(@RequestBody Account account) {
        try {
            ResponseEntity<Account> response = accountService.login(account);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    //     private void loginAccount(Context ctx) throws JsonProcessingException {
    //         ObjectMapper mapper = new ObjectMapper(); 
    //         Account account = mapper.readValue(ctx.body(), Account.class);
    //         Account checkedAccount = accountService.checkIfAccountExist(account);
    //         if(checkedAccount==null){
    //             ctx.status(401);
    //         }
    //         else{
    //             ctx.json(mapper.writeValueAsString(checkedAccount));
    //         }
    //     }
    @PostMapping("/messages")//app.post("/messages", this::createMessage);
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }
    //     private void createMessage(Context ctx) throws JsonProcessingException {
    //         ObjectMapper mapper = new ObjectMapper(); 
    //         Message message = mapper.readValue(ctx.body(), Message.class);
    //         Message createMessage = messageService.addMessage(message);
    //         if(createMessage==null){
    //             ctx.status(400);
    //         } else{
    //             ctx.json(mapper.writeValueAsString(createMessage));
    //         }
    //     }
    @GetMapping("/messages")//app.get("/messages", this::getAllMessages);
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }
    //     private void getAllMessages(Context ctx) throws JsonProcessingException {
    //         List<Message> messages = messageService.getAllMessages();
    //         ctx.json(messages);
    //     }

    @GetMapping("/messages/{messageId}")//app.get("/messages/{message_id}", this::getMessageById);
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageById(messageId);
    }
    //     private void getMessageById(Context ctx) throws JsonProcessingException {
    //         ObjectMapper mapper = new ObjectMapper(); 
    //         int id = Integer.parseInt(ctx.pathParam("message_id"));
    //         Message getMessage = messageService.getMessageById(id);
    //         if(getMessage==null){
    //             ctx.status(200);
    //         } else{
    //             ctx.json(mapper.writeValueAsString(getMessage));
    //         }
    //     }
    @DeleteMapping("messages/{messageId}")//app.delete("/messages/{message_id}", this::deleteMessageByMessageId);
    public ResponseEntity<Object> deleteMessage(@PathVariable Integer messageId) {
        return messageService.deleteMessageById(messageId);
    }
    //     private void deleteMessageByMessageId(Context ctx) throws JsonProcessingException {
            
    //         int id = Integer.parseInt(ctx.pathParam("message_id"));
    //         Message message = messageService.getMessageById(id);
    //         if (message!=null) {// The message exists, so delete it
    //             messageService.removeMessage(message);
    //             ctx.json(message);
    //         } else {
    //             ctx.status(200);
    //         }
    //     }
    @PatchMapping("messages/{messageId}")//app.patch("/messages/{message_id}", this::updateMessageByMessageId);
    public ResponseEntity<Object> updateMessageText(@PathVariable Integer messageId, @RequestBody Message newMessageText) {
        return messageService.updateMessageText(messageId, newMessageText);
    }
    //     private void updateMessageByMessageId(Context ctx) throws JsonProcessingException {
    //         int id = Integer.parseInt(ctx.pathParam("message_id"));
    //         Message message = ctx.bodyAsClass(Message.class);
    //         // Message message = messageService.getMessageById(id);

    //         try{
    //             if (message!=null) {// The message exists, so update it
    //                 messageService.updateMessage(message,id);
    //                 System.out.println(messageService.getMessageById(id));
    //                 ctx.json(messageService.getMessageById(id));
                    
    //             } else {
    //                 ctx.status(400);
    //             }
    //         }
    //         catch(Exception e){
    //             ctx.status(400);
    //         }
    //     }
    @GetMapping("accounts/{accountId}/messages")//app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
    //     private void getMessagesByAccountId(Context ctx) {
    //         try {
    //             int accountId = Integer.parseInt(ctx.pathParam("account_id"));

    //             // Call the messageService to retrieve messages by account ID
    //             List<Message> messages = messageService.getMessagesByAccountId(accountId);
    //             if (!messages.isEmpty()) {
    //                 // If messages are found, send them as a JSON response
    //                 ctx.json(messages);
    //             } else {
    //                 // If no messages are found, send an empty JSON response
    //                 ctx.json(messages);
    //                 ctx.status(200);
    //             }
    //         } catch (Exception e) {
    //             // Handle ServiceException and set the status code to 400 (Bad Request)
    //             ctx.status(400);
    //         }
    //     }
    // }
}