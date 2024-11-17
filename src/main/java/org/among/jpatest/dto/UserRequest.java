package org.among.jpatest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.among.jpatest.common.Role;
import org.among.jpatest.common.RoleDeserializer;

import java.time.LocalDateTime;

public class UserRequest {
    private String userId;

    private String password;

    // 클라이언트로부터 소문자로 들어올 경우 매핑 위해 대문자 변경 필요해 사용
    @JsonDeserialize(using = RoleDeserializer.class)
    private Role role;

    // 불필요하지만 연습 위해 요청시간도 받는다고 가정..
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", requestTime=" + requestTime +
                '}';
    }
}
