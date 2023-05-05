package ru.clevertec.ecl.patcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.exception.GiftCertificatePatchException;
import ru.clevertec.ecl.pageable.Patch;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GiftCertificatePatcher implements Patcher<GiftCertificate> {

    private final ObjectMapper mapper;

    @Override
    public void applyPatch(GiftCertificate giftCertificate, Patch patch) {
        Field field = ReflectionUtils.findField(giftCertificate.getClass(), patch.field());
        if (Objects.isNull(field)) {
            throw new GiftCertificatePatchException(String.format("Field %s does not exist", patch.field()));
        }
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        Object o = null;
        try {
            o = mapper.readValue(patch.value(), fieldType);
        } catch (JsonProcessingException e) {
            throw new GiftCertificatePatchException(String.format("Illegal json format for value: %s", patch.value()));
        }

        ReflectionUtils.setField(field, giftCertificate, o);
    }
}
