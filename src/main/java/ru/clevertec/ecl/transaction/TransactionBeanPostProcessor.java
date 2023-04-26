package ru.clevertec.ecl.transaction;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.clevertec.ecl.util.ReflectionUtil;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public abstract class TransactionBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private final Map<String, Class<?>> classMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (!beanClass.isAnnotationPresent(Transaction.class)) {
            boolean isAnnotated = Arrays.stream(ReflectionUtils.getAllDeclaredMethods(beanClass))
                    .anyMatch(m -> m.isAnnotationPresent(Transaction.class));

            if (isAnnotated) {
                classMap.put(beanName, beanClass);
            }
        } else {
            classMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (classMap.containsKey(beanName)) {
            Class<?> clazz = classMap.get(beanName);
            ClassLoader classLoader = clazz.getClassLoader();
            Class<?>[] interfaces = ReflectionUtil.getAllDeclaredInterfaces(clazz);
            return Proxy.newProxyInstance(
                    classLoader,
                    interfaces,
                    getHandler(clazz, bean, applicationContext.getBean(SessionFactory.class)));
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Lookup
    abstract TransactionInvocationHandler getHandler(Class<?> original, Object target, SessionFactory factory);
}
