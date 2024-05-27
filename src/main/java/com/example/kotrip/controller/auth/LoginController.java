package com.example.kotrip.controller.auth;

import com.example.kotrip.dto.common.ApiResponse;
import com.example.kotrip.dto.login.request.LoginRequestDto;
import com.example.kotrip.dto.login.response.LoginResponseDto;
import com.example.kotrip.dto.reissue.request.ReissueRequestDto;
import com.example.kotrip.dto.reissue.response.ReissueResponseDto;
import com.example.kotrip.service.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Login", description = "로그인 API")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "회원 로그인", description = "로그인할 때 사용하는 API")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ApiResponse.ok(loginService.login(loginRequestDto.getCode()));
    }

    @PostMapping("/reissue")
    @Operation(summary = "회원 토큰 재발급", description = "토큰이 만료되었을 때 재발행하는 API")
    public ApiResponse<ReissueResponseDto> reissue(@Valid @RequestBody ReissueRequestDto reissueRequestDto) {
        return ApiResponse.ok(loginService.reissue(reissueRequestDto));
    }

    @DeleteMapping("/withdrawal")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴할 때 사용하는 API")
    public ApiResponse<String> withdrawal() {
        return ApiResponse.ok(loginService.withdrawal());
    }
}