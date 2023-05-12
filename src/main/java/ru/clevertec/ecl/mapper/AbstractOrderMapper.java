package ru.clevertec.ecl.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.data.gift_certificate.ResponseGiftCertificateDto;
import ru.clevertec.ecl.data.order.ResponseOrderDto;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

@Mapper(uses = {UserMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AbstractOrderMapper {

    public UserMapper userMapper;
    public GiftCertificateMapper giftCertificateMapper = Mappers.getMapper(GiftCertificateMapper.class);

    @Mapping(target = "giftCertificateId", source = "giftCertificate.id")
    public abstract ResponseOrderDto orderToResponseOrderDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchase", ignore = true)
    @Mapping(target = "price", source = "giftCertificate.price")
    @Mapping(target = "giftCertificate", source = "giftCertificate")
    public abstract Order createOrderDtoToOrder(User user,
                                                ResponseGiftCertificateDto giftCertificate);

    @Mapping(target = "user", ignore = true)
    public abstract Order responseOrderDtoToOrder(ResponseOrderDto orderDto);

}
