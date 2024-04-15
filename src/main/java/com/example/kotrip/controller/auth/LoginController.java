package com.example.kotrip.controller.auth;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.login.request.LoginRequestDto;
import com.example.kotrip.dto.login.response.LoginResponseDto;
import com.example.kotrip.dto.reissue.request.ReissueRequestDto;
import com.example.kotrip.dto.reissue.response.ReissueResponseDto;
import com.example.kotrip.service.login.LoginService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class    LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ApiResponse.ok(loginService.login(loginRequestDto.getCode()));
    }

    @PostMapping("/reissue")
    public ApiResponse<ReissueResponseDto> reissue(@Valid @RequestBody ReissueRequestDto reissueRequestDto) {
        return ApiResponse.ok(loginService.reissue(reissueRequestDto));
    }
}