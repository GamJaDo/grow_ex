package com.gp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gp.dto.ResponseDto;
import com.gp.dto.SignInDto;
import com.gp.dto.SignInResponseDto;
import com.gp.dto.SignUpDto;
import com.gp.entity.UserEntity;
import com.gp.repository.UserRepository;
import com.gp.security.TokenProvider;

@Service
public class AuthService {
	
	@Autowired UserRepository userRepository;
	@Autowired TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public ResponseDto<?> signUp(SignUpDto dto){
		String userEmail = dto.getUserEmail();
		String userPassword = dto.getUserPassword();
		String userPasswordCheck = dto.getUserPasswordCheck();
		
		// email 중복 확인
		try {
			if(userRepository.existsById(userEmail)) {
				return ResponseDto.SetFailed("Existed Email!");
			}
		} catch(Exception error) {
			return ResponseDto.SetFailed("Data Base Error!");
		}
		
		
		// 비밀번호가 서로 다르면 failed response 반환
		if(!userPassword.equals(userPasswordCheck)) {
			return ResponseDto.SetFailed("Password does not matched!");
		}
		
		// UserEntity 생성
		UserEntity userEntity = new UserEntity(dto);
		
		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(userPasswordCheck);
		userEntity.setUserPassword(encodedPassword);
		
		// UserRepository를 이용해서 데이터베이스에 Entity 저장
		try {
			userRepository.save(userEntity);
		} catch(Exception error) {
			return ResponseDto.SetFailed("Data Base Error!");
		}
		
		// 성공시 success response 반환
		return ResponseDto.setSuccess("SignUp Success!", null);
	}
	
	public ResponseDto<SignInResponseDto> signIn(SignInDto dto){
		String userEmail = dto.getUserEmail();
		String userPassword = dto.getUserPassword();
		
		UserEntity userEntity = null;
		try {
			userEntity = userRepository.findByUserEmail(userEmail);
			// 잘못된 이메일
			if(userEntity == null) return ResponseDto.SetFailed("Sign In Failed");
			// 잘못된 패스워드
			if(!passwordEncoder.matches(userPassword, userEntity.getUserPassword()))
				return ResponseDto.SetFailed("Sign In Failed");
		} catch(Exception error) {
			return ResponseDto.SetFailed("Data Base Error!");
		}
		
		String token = tokenProvider.create(userEmail);
		int exprTime = 3600000;
		
		SignInResponseDto signInResponseDto = new SignInResponseDto(token, exprTime, userEntity);
		return ResponseDto.setSuccess("Sign In Success", signInResponseDto);
	}
}
