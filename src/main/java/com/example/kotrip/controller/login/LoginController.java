package com.example.kotrip.controller.login;

import com.example.kotrip.dto.user.response.LoginResponseDto;
import com.example.kotrip.service.login.LoginService;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public LoginResponseDto login(@RequestParam String code) throws MalformedURLException {
        return loginService.getAccessToken(code);
    }
}
