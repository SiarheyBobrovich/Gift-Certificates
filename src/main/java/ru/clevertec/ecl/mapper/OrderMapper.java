package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.entity.Order;

@Mapper
public interface OrderMapper {

    @Mapping(target = "giftCertificateId", source = "giftCertificate.id")
    ResponseOrderDto OrderToResponseOrderDto(Order order);
}
