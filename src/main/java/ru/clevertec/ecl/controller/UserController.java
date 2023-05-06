package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<ResponseUserDto>> getAllUsers(@PageableDefault(20) Pageable pageable) {
        Page<ResponseUserDto> userDtoPage = userService.findAll(pageable);
        return ResponseEntity.ok().body(userDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getById(@PathVariable Long id) {
        ResponseUserDto responseUser = userService.findById(id);
        return ResponseEntity.ok().body(responseUser);
    }
}
