package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

@Mapper
public interface OrderMapper {

    @Mapping(target = "giftCertificateId", source = "giftCertificate.id")
    ResponseOrderDto orderToResponseOrderDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchase", ignore = true)
    @Mapping(target = "price", source = "giftCertificate.price")
    @Mapping(target = "giftCertificate", source = "giftCertificate")
    Order createOrderDtoToOrder(User user,
                                                ResponseGiftCertificateDto giftCertificate);
}
