package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //checks to see if the user exist
    boolean existsByUsername(String username);
    //searches the user
    Account findByUsername(String username);
    //logging into and verifying password(technically)
    Account findByUsernameAndPassword(String username, String password);
    //identifying the account.
    Account findByAccountId(Integer accountId);
}