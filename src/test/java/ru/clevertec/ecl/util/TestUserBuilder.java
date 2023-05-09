package ru.clevertec.ecl.util;

import lombok.Builder;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.data.user.ResponseUserDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class TestUserBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String firstName = "firstName";

    @Builder.Default
    private String lastName = "lastName";

    @Builder.Default
    private List<Order> orders = Stream.of(
            new Order(1L, new GiftCertificate(), BigDecimal.ZERO, new User(), LocalDateTime.MAX),
            new Order(1L, new GiftCertificate(), BigDecimal.ZERO, new User(), LocalDateTime.MIN)
    ).collect(Collectors.toList());

    public User getUser() {
        return new User(id, firstName, lastName, orders);
    }

    public Optional<User> getOptionalUser() {
        return Optional.of(getUser());
    }

    public ResponseUserDto getResponseUserDto() {
        return new ResponseUserDto(id, firstName, lastName, orders.stream()
                .map(o -> new ResponseOrderDto(o.getId(), o.getGiftCertificate().getId(), o.getPrice(), o.getPurchase()))
                .collect(Collectors.toList()));
    }
}
