package ru.clevertec.ecl.exception;

public class GiftCertificateNotFoundException extends EntityNotFoundException {

    public GiftCertificateNotFoundException(Long id) {
        super(id);
    }

    public int getCode() {
        return 404002;
    }
}
