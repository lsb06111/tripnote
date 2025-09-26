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
        UserDTO user = userDAO.login(username);
        
        if (user != null && matchesPassword(plainPassword, user.getPassword())) {
            return user;
        }
        
        return null;
    }
}