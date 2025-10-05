package com.smartprep.smart_interview_platform.Controller;

import com.smartprep.smart_interview_platform.Model.EmailRequest;
import com.smartprep.smart_interview_platform.Model.LoginRequest;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/login")
//    public ResponseEntity<String> login (@RequestBody LoginRequest loginRequest){
//        ResponseEntity<String> result = userService.login(loginRequest.getEmail(),loginRequest.getPassword());
//        return  result;
//    }

    @PostMapping("/users")
    public ResponseEntity<User> getUser(@RequestBody EmailRequest emailRequest){
        return userService.getUser(emailRequest.getEmail());
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/id/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

}
