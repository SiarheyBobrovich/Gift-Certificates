package ru.clevertec.ecl.util;

import lombok.Builder;
import lombok.Getter;
import ru.clevertec.ecl.data.order.CreateOrderDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Builder(toBuilder = true)
@Getter
public class TestOrderBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private GiftCertificate giftCertificate = CertificateBuilder.builder().build().getEntity();

    @Builder.Default
    private BigDecimal price = BigDecimal.ONE;

    @Builder.Default
    private User user = TestUserBuilder.builder().build().getUser();

    @Builder.Default
    private LocalDateTime purchase = LocalDateTime.MAX;

    public Order getOrder() {
        return new Order(id, giftCertificate, price, user, purchase);
    }

    public Optional<Order> getOptionalOrder() {
        return Optional.of(getOrder());
    }

    public ResponseOrderDto getResponseOrderDto() {
        return new ResponseOrderDto(id, giftCertificate.getId(), price, purchase);
    }

    public CreateOrderDto getCreateOrderDto() {
        return new CreateOrderDto(user.getId(), giftCertificate.getId());
    }
}
