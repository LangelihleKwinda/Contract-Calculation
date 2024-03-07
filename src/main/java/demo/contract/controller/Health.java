package demo.contract.controller;

import demo.contract.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Slf4j
public class Health {

    private final UserService userService;

    public Health(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "ping/v1")
    public ResponseEntity<String> ping() {
        log.info("Ping...");
        return new ResponseEntity<>("The application is up", HttpStatus.OK);
    }
}

