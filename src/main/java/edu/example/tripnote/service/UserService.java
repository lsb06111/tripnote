package edu.example.tripnote.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.example.tripnote.dao.UserDAO;
import edu.example.tripnote.domain.UserDTO;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    // 회원가입 시 비밀번호를 암호화하여 DB에 저장
    public int encryptInsert(UserDTO userDTO) {
        String plainPassword = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(plainPassword);
        userDTO.setPassword(encodedPassword);
        return userDAO.insertUser(userDTO);
    }

    // 사용자 로그인 시 입력된 비밀번호를 검증
    public boolean matchesPassword(String plainPassword, String encodedPassword) {
        return passwordEncoder.matches(plainPassword, encodedPassword);
    }

    // 비밀번호 업데이트 시 암호화하여 DB에 저장
    public int encryptUpdate(int id, String plainPassword) {
        String encodedPassword = passwordEncoder.encode(plainPassword);
        return userDAO.updatePassword(id, encodedPassword);
    }
    
    // 로그인 서비스 로직
    public UserDTO login(String username, String plainPassword) {
        // 1. DAO를 통해 사용자 이름으로 DB에서 사용자 정보(암호화된 비밀번호)를 가져옴
        UserDTO user = userDAO.login(username);
        
        // 2. 사용자가 존재하고, 비밀번호가 일치하는지 확인
        if (user != null && matchesPassword(plainPassword, user.getPassword())) {
            // 비밀번호가 일치하면 사용자 정보 반환
            return user;
        }
        
        // 비밀번호가 일치하지 않거나 사용자가 없으면 null 반환
        return null;
    }
}