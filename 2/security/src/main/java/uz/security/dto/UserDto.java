package uz.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.security.model.entity.type.RoleType;

@AllArgsConstructor
@Data
@Builder
public
class UserDto{
    private String username;
    private String password;
    private RoleType roleType;
}