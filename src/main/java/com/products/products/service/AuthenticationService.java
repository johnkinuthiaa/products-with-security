package com.products.products.service;

import com.products.products.dto.ReqRes;
import com.products.products.model.User;
import com.products.products.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JWTUtils jwtUtils;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService,JWTUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.jwtUtils =jwtUtils;
    }
    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp =new ReqRes();
        try{
            User ourUsers =new User();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setVerificationCode(generateVerificationCode());
            ourUsers.setVerificationCodeExpireAt(LocalDateTime.now().plusMinutes(30));
            ourUsers.setEnabled(false);
            sendVerificationEmail(ourUsers);

            User ourUsersResult = userRepository.save(ourUsers);

            if(ourUsersResult !=null &&ourUsersResult.getId()>0){
                resp.setUser(ourUsersResult);
                resp.setMessage("user saved successfully");
                resp.setStatusCode(200);
            }
        }
        catch (Exception e){
            resp.setError(e.getMessage());
        }
        return resp;
    }
    public ReqRes signIn(ReqRes signinRequest){
        ReqRes response =new ReqRes();
        try{
            var user =userRepository.findUserByEmail(signinRequest.getEmail()).orElseThrow();
            if(!user.isEnabled()){
                throw new RuntimeException("not yet confirmed email");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinRequest.getEmail(),
                            signinRequest.getPassword()));

            var jwtRefresh =jWTUtils.generateFreshToken(new HashMap<>(),user);
            response.setStatusCode(200);
//            response.setToken(jwt);
//            response.setRefreshToken(jwtRefresh);
            response.setExpiryTime("24Hr");
            response.setMessage("user successfully signed in");
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }
    public ReqRes verifyUser(ReqRes verify){
        ReqRes response =new ReqRes();
        Optional<User> optionalUser =userRepository.findUserByEmail(verify.getEmail());
        if(optionalUser.isPresent()){
            User user =optionalUser.get();
            if(user.getVerificationCodeExpireAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException();
            }
            if(user.getVerificationCode().equals(verify.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                response.setMessage("user is successfully registered");
                response.setStatusCode(200);
                userRepository.save(user);

            }else {
                response.setMessage("verification not successful");
                response.setStatusCode(500);
                throw new RuntimeException("invalid");

            }
        }
        return response;
    }
    public ReqRes resendVerificationCode(String email) throws Exception{
        ReqRes response =new ReqRes();
        Optional<User> optionalUser =userRepository.findUserByEmail(email);
        if(optionalUser.isPresent()){
            User user =optionalUser.get();

            if(user.isEnabled()){
                response.setMessage("verification was already successful");
                response.setStatusCode(500);
                throw new RuntimeException("user is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpireAt(LocalDateTime.now().plusMinutes(10));
            sendVerificationEmail(user);


        }else{
            response.setMessage("user not found");
            response.setStatusCode(500);
        }
        return response;
    }

    private String generateVerificationCode() {
        Random random =new Random();
        int code = random.nextInt(9000000)+100000;
        return String.valueOf(code);
    }

    private void sendVerificationEmail(User user) throws Exception{
        String subject ="account verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage =verificationCode;
        try {
            emailService.sendVerificationEmail(user.getEmail(),subject,htmlMessage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
