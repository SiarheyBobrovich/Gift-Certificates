package ru.clevertec.ecl.entity.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.time.LocalDateTime;

public class GiftCertificateListener {

    @PrePersist
    public void persistDate(GiftCertificate giftCertificate) {
        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
    }

    @PreUpdate
    public void updateDate(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }
}
