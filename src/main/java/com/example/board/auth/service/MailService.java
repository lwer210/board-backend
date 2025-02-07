package com.example.board.auth.service;

import com.example.board.auth.controller.request.PasswordResetRequest;
import com.example.board.auth.controller.response.UserResponse;
import com.example.board.auth.persistence.entity.UserEntity;
import com.example.board.auth.persistence.repository.UserEntityRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String from;

    public UserResponse reset(PasswordResetRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));// TODO 커스텀 예외로 교체 필요

        String newPassword = randPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        UserEntity newUser = userRepository.save(user);

        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            helper.setFrom(from);
            helper.setTo(request.getEmail());
            helper.setSubject(user.getNickname() + "님 임시 비밀번호 발급 이메일");

            String content = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; width: 400px;'>"
                    + "<h2 style='color: #333; text-align: center;'>임시 비밀번호 안내</h2>"
                    + "<p style='font-size: 16px; color: #555;'>안녕하세요, " + user.getNickname() + "님!</p>"
                    + "<p style='font-size: 16px; color: #555;'>요청하신 임시 비밀번호를 발급해 드립니다.</p>"
                    + "<div style='background: #f4f4f4; padding: 10px; font-size: 18px; font-weight: bold; text-align: center; border-radius: 5px;'>"
                    + newPassword
                    + "</div>"
                    + "<p style='font-size: 14px; color: #777; margin-top: 20px;'>"
                    + "보안을 위해 로그인 후 반드시 비밀번호를 변경해주세요.</p>"
                    + "<p style='font-size: 12px; color: #aaa;'>"
                    + "이 이메일은 발신 전용이며, 회신할 수 없습니다.</p>"
                    + "</div>";

            helper.setText(content, true);

            mailSender.send(mimeMessage);

            return UserResponse.builder()
                    .id(newUser.getId())
                    .email(newUser.getEmail())
                    .nickname(user.getNickname())
                    .role(newUser.getRole())
                    .createdAt(newUser.getCreatedAt())
                    .updatedAt(newUser.getUpdatedAt())
                    .build();

        }catch(Exception e){
            throw new RuntimeException("이메일 발송에 실패하였습니다."); // TODO 커스텀 예외 교체 필요
        }
    }

    public String randPassword(){
        char[] chars = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '@', '!'
        };

        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < 8; i++){
            int index = random.nextInt(chars.length);
            builder.append(chars[index]);
        }

        return builder.toString();
    }
}
