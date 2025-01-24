package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Account> postAccount(Account account) {
        // This is to check if the name is not blank and password is at least 4 characters long
        if (
                account.getUsername() == null // This is to check if the name is not blank 
                || 
                account.getUsername().isBlank() // This is to check if the name is not blank 
                || 
                account.getPassword() == null || account.getPassword().length() < 4// This is to check if the name is at least 4 characters long
            ){
            // return ResponseEntity.status(400).body(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        // AccountRepository.existsByUsername this is an interface method you put in the AccountRepository file
        // AccountRepository.existsByUsername checks to see if the name exist or not in the database.
        if (accountRepository.existsByUsername(account.getUsername())) {
            // CONFLICT
            // return ResponseEntity.status(409).body(null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    
        Account savedAccount = accountRepository.save(account);//////////////////////////////////////////////

        // return ResponseEntity.status(200).body(savedAccount);
        return ResponseEntity.status(HttpStatus.OK).body(savedAccount);
    }

    public ResponseEntity<Account> login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (
                username == null //check to see if the name is empty
                || 
                username.isBlank() //check to see if the name is empty
                || 
                password == null //check to see if the password is empty
                || 
                password.isBlank() //check to see if the password is empty
            ) {
            return ResponseEntity.status(
                // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html
                
                HttpStatus.BAD_REQUEST//check status request 9-4-7

                ).body(null);
        }
        //accountRepository.findByUsernameAndPassword checks to see if the account exist and the password matches.
        if (accountRepository.findByUsernameAndPassword(username, password) == null) {
            //HttpStatus.UNAUTHORIZED   401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            // return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.ok(accountRepository.findByUsernameAndPassword(username, password));
    }
}



