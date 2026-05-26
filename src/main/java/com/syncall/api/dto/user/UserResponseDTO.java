package com.syncall.api.dto.user;

import com.syncall.api.model.Role;
import com.syncall.api.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String name;
    private String email;
    private Role role;
    private Long companyId;
    private String companyName;


    public static UserResponseDTO from(User user){
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .companyId(user.getCompany().getId())
                .companyName(user.getCompany().getName())
                .role(user.getRole())
                .build();

    }

}
