package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.controller.open_api.UserOpenApi;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.logging.Logging;
import ru.clevertec.ecl.service.UserService;

@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserOpenApi {

    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<Page<ResponseUserDto>> getAllUsers(@PageableDefault(20) Pageable pageable) {
        Page<ResponseUserDto> userDtoPage = userService.findAll(pageable);

        return ResponseEntity.ok(userDtoPage);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getById(@PathVariable Long id) {
        ResponseUserDto userDto = userService.findById(id);

        return ResponseEntity.ok(userDto);
    }
}
