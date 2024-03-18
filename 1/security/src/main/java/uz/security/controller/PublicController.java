package uz.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PublicController {

    @RequestMapping("/")
    public ResponseEntity<String> getPublic() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body("Ok: DavSDD public page!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
