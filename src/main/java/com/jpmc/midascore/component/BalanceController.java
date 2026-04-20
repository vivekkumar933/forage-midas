
package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.Balance;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam long userId) {

        // IMPORTANT: your repository returns UserRecord directly (NOT Optional)
        UserRecord user = userRepository.findById(userId);

        if (user == null) {
            return new Balance(0);
        }

        return new Balance(user.getBalance());
    }
}