package com.grow_project_backend.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grow_project_backend.dto.SignInDto;
import com.grow_project_backend.dto.SignUpDto;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;
    
    public ResponseEntity<?> signUp(SignUpDto signUpDto) {
        if (userRepository.existsByLoginId(signUpDto.getUserLoginId())) {
            return ResponseEntity.badRequest().body("이미 사용중인 아이디 입니다.");
        }

        if (!signUpDto.getUserPassword().equals(signUpDto.getUserPasswordCheck())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId(signUpDto.getUserLoginId());
        userEntity.setPassword(signUpDto.getUserPassword()); // In real-world, this password should be encoded.
        userEntity.setName(signUpDto.getUserName());
        userRepository.save(userEntity);

        return ResponseEntity.ok(signUpDto);
    }

    public ResponseEntity<?> signIn(SignInDto signInDto, HttpSession session) {
        UserEntity userEntity = userRepository.findByLoginId(signInDto.getUserLoginId());
        if (userEntity == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디입니다.");
        }

        if (!userEntity.getPassword().equals(signInDto.getUserPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
        
        session.setAttribute("user", userEntity);
        
        SignInDto loginResponseDto = new SignInDto();
        loginResponseDto.setUserLoginId(userEntity.getLoginId());

        return ResponseEntity.ok(loginResponseDto);
    }
}
