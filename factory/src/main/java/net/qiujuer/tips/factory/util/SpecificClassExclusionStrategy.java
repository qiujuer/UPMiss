package net.qiujuer.tips.factory.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by QiuJu
 * on 2014/9/16.
 */
public class SpecificClassExclusionStrategy implements ExclusionStrategy {

    private final Class<?> excludedThisClass;
    private final Class<?> excludedThisClassFields;

    /**
     * 过滤器初始化
     *
     * @param excludedThisClass       该类和继承自该类的对象实例将被忽略
     * @param excludedThisClassFields 该类的属性将不被序列化
     */
    public SpecificClassExclusionStrategy(Class<?> excludedThisClass, Class<?> excludedThisClassFields) {
        this.excludedThisClass = excludedThisClass;
        this.excludedThisClassFields = excludedThisClassFields;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz != null && (clazz.equals(excludedThisClass) || shouldSkipClass(clazz.getSuperclass()));
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaringClass().equals(excludedThisClassFields);
    }

}