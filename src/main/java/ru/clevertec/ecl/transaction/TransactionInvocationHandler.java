package ru.clevertec.ecl.transaction;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Scope("prototype")
public class TransactionInvocationHandler implements InvocationHandler {

    private final Class<?> originalClass;
    private final Object target;
    private final SessionFactory sessionFactory;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final boolean classAnnotationPresent = originalClass.isAnnotationPresent(Transaction.class);
        final Method originalMethod = ReflectionUtils.findMethod(originalClass, method.getName(), method.getParameterTypes());
        final boolean methodAnnotationPresent = Objects.nonNull(originalMethod) && originalMethod.isAnnotationPresent(Transaction.class);
        boolean isPresent = classAnnotationPresent || methodAnnotationPresent;
        final Object result;
        Transaction transaction;

        if (isPresent) {
            if (methodAnnotationPresent) {
                transaction = originalMethod.getAnnotation(Transaction.class);

            } else {
                transaction = originalClass.getAnnotation(Transaction.class);
            }
            try (Session currentSession = this.sessionFactory.getCurrentSession()) {
                final boolean readOnly = transaction.readOnly();
                final int isolationLevel = transaction.isolationLevel().getLevel();
                boolean active = currentSession.getTransaction().isActive();

                if (!active) {
                    currentSession.getTransaction().begin();
                }

                currentSession.doWork(connection -> connection.setReadOnly(readOnly));
                currentSession.doWork(connection -> connection.setTransactionIsolation(isolationLevel));

                result = ReflectionUtils.invokeMethod(method, target, args);

                currentSession.getTransaction().commit();
            }
        } else {
            result = ReflectionUtils.invokeMethod(method, target, args);
        }
        return result;
    }
}
