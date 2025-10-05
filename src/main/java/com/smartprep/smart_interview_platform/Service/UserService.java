package com.smartprep.smart_interview_platform.Service;

import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Model.UserStats;
import com.smartprep.smart_interview_platform.Repository.UserRepository;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatsRepository userStatsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public String register(User user){

        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if(existing.isPresent()){
            throw new RuntimeException("User Already Exists !");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        UserStats userStats = new UserStats();
        userStats.setUser(user);
        userStats.setQuestionsSolved(0L);
        userStats.setAcceptedSubmissions(0L);
        userStats.setTotalSubmissions(0L);
        userStats.setAccuracy(0.0);
        userStatsRepository.save(userStats);

        return "Registration Successful";
    }

    public ResponseEntity<String> login(String email, String rawPassword){

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(passwordEncoder.matches(rawPassword, user.getPassword())){
                System.out.println("Login Successfully");
                return ResponseEntity.ok("Login Successful!");

            }
            else {
                throw new RuntimeException("Incorrect Password");
            }
        }
        throw new RuntimeException("User Not Found !");

    }

    public ResponseEntity<User> getUser(String email){

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            System.out.println("Looking for email: " + optionalUser.get().getEmail());
            return ResponseEntity.ok(optionalUser.get());

        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail( String email){
        return userRepository.findByEmail(email);
    }

}
