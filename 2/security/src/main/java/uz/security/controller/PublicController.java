package uz.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.security.dto.UserDto;
import uz.security.model.entity.Role;
import uz.security.service.UserService;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> getPublic() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body("Ok: DavSDD public page!");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Error: Public page is not opened! - public controller",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/account")
    public ResponseEntity<UserDto> createAccount(@RequestBody UserDto userDto) {
        try {
            String username = userDto.getUsername();
            String password = userDto.getPassword();
            UserDto dto = userService.createUser(username, password, Role.setRole(userDto.getRoleType()));
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            throw new RuntimeException("Error: Account is not created! - public controller", e);
        }

    }

}
