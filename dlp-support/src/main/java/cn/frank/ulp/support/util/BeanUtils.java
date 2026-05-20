package cn.frank.ulp.support.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * Bean 工具，扩展 Spring BeanUtils.
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    public BeanUtils() {
    }

    public static void merge(Object source, Object target) throws BeansException {
        merge(source, target, null, (String[]) null);
    }

    public static void merge(Object source, Object target,
                             @Nullable String... ignoreProperties) throws BeansException {
        merge(source, target, null, ignoreProperties);
    }

    public static void merge(Object source, Object target, @Nullable Class<?> editable,
                             @Nullable String... ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName()
                                                   + "] not assignable to Editable class ["
                                                   + editable.getName() + "]");
            }
            actualEditable = editable;
        }

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod == null
                || (ignoreList != null && ignoreList.contains(targetPd.getName()))) {
                continue;
            }
            PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(),
                targetPd.getName());
            if (sourcePd == null) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            if (!ClassUtils.isAssignable(writeMethod.getParameterTypes()[0],
                readMethod.getReturnType())) {
                continue;
            }
            try {
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source);
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                if (Objects.nonNull(value)) {
                    writeMethod.invoke(target, value);
                }
            } catch (Throwable t) {
                throw new FatalBeanException(
                    "Could not copy property '" + targetPd.getName() + "' from source to target",
                    t);
            }
        }
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key.toString(), beanMap.get(key));
            }
        }
        return map;
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        T bean;
        try {
            bean = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }
}
