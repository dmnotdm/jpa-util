package com.zzz.util.jpa.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by zhizhao.zhang on 2018/12/20 17:02.
 * Description:
 */
public class PropertiesUtils {

    public enum CopyRule {
        ALL, NULL, NOT_NULL
    }

    public static void copyAllProperties(Object source, Object target) {
        copyPropertiesWithSource(source, target, CopyRule.ALL);
    }

    public static void copyAllLocalProperties(Object source, Object target) {
        copyLocalPropertiesWithSource(source, target, CopyRule.ALL);
    }

    public static void copyPropertiesWithSource(Object source, Object target, CopyRule rule) {
        copyProperties(source, target, source, rule);
    }

    public static void copyPropertiesWithTarget(Object source, Object target, CopyRule rule) {
        copyProperties(source, target, target, rule);
    }

    public static void copyLocalPropertiesWithSource(Object source, Object target, CopyRule rule) {
        copyLocalProperties(source, target, source, rule);
    }

    public static void copyLocalPropertiesWithTarget(Object source, Object target, CopyRule rule) {
        copyLocalProperties(source, target, target, rule);
    }

    private static void copyProperties(Object source, Object target, Object checkedObj, CopyRule rule) {
        ReflectionUtils.doWithFields(target.getClass(), field -> {
            try {
                copy(field, source, target, checkedObj, rule);
            } catch (Exception ignore) {
            }
        });
    }

    private static void copyLocalProperties(Object source, Object target, Object checkedObj, CopyRule rule) {
        ReflectionUtils.doWithLocalFields(target.getClass(), field -> {
            try {
                copy(field, source, target, checkedObj, rule);
            } catch (Exception ignore) {
            }
        });
    }

    private static void copy(Field field, Object source, Object target, Object checkedObj, CopyRule rule) throws IllegalAccessException, NoSuchFieldException {
        field.setAccessible(true);
        Object obj = target.getClass() == checkedObj.getClass() ? field.get(checkedObj) : getValue(field.getName(), checkedObj);

        if (rule == CopyRule.ALL) {
            field.set(target, obj);
            return;
        }

        if (obj == null ^ rule == CopyRule.NOT_NULL) {
            field.set(target, checkedObj == source ? obj : field.get(source));
        }
    }

    private static Object getValue(String fieldName, Object source) throws NoSuchFieldException, IllegalAccessException {
        Field field = source.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(source);
    }
}
