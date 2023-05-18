package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.data.tag.ResponseTagDto;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.util.TestOrderBuilder.*;

@SpringBootTest(classes = {OrderMapperImpl.class})
class OrderMapperTest {

    @Autowired
    private OrderMapper mapper;

    @Test
    void orderToResponseOrderDto() {
        Order order = builder().build().getOrder();
        ResponseOrderDto expected = builder().build().getResponseOrderDto();

        ResponseOrderDto actual = mapper.orderToResponseOrderDto(order);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createOrderDtoToOrder() {
        Order expected = builder().purchase(null).id(null).build().getOrder();
        User user = expected.getUser();

        GiftCertificate gc = expected.getGiftCertificate();
        ResponseGiftCertificateDto certificateDto = new ResponseGiftCertificateDto(
                gc.getId(),
                gc.getName(),
                gc.getPrice(),
                gc.getDescription(),
                gc.getDuration(),
                gc.getCreateDate(),
                gc.getLastUpdateDate(),
                gc.getTags().stream()
                        .map(t -> new ResponseTagDto(t.getId(), t.getName()))
                        .collect(Collectors.toList())
        );

        Order order = mapper.createOrderDtoToOrder(user, certificateDto);

        assertThat(order.getGiftCertificate()).isEqualTo(expected.getGiftCertificate());
        assertThat(order.getUser()).isEqualTo(expected.getUser());
        assertThat(order.getPrice()).isEqualTo(expected.getGiftCertificate().getPrice());
        assertThat(order.getId()).isNull();
    }
}
