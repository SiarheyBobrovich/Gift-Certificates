package ru.clevertec.ecl.transaction;

import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transaction {

    boolean readOnly() default false;

    IsolationLevel isolationLevel() default IsolationLevel.READ_COMMITTED;

    @Getter
    enum IsolationLevel {

        READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
        REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
        SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

        private final int level;

        IsolationLevel(int level) {
            this.level = level;
        }
    }
}
