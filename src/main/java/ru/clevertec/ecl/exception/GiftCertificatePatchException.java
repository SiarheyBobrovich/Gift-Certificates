package ru.clevertec.ecl.exception;

public class GiftCertificatePatchException extends AbstractServiceException {

    public GiftCertificatePatchException(String message) {
        super(message);
    }

    @Override
    public int getCode() {
        return 40002;
    }
}
