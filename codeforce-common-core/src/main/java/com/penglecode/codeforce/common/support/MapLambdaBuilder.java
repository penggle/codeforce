package com.penglecode.codeforce.common.support;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 基于JAVA8方法引用(lambda的一种特殊形式)方式来快速通过普通JavaBean构造Map的通用Builder
 * 使用示例：
 *      Account account = ObjectLambdaBuilder.of(Account::new)
 *                 .with(Account::setAcctNo, "27182821221122219")
 *                 .with(Account::setCustNo, "71283828182823291929")
 *                 .with(Account::setIdCode, null)
 *                 .with(Account::setCustName, "阿三")
 *                 .with(Account::setMobile, "13812345678")
 *                 .build();
 *         System.out.println(account);
 *
 *         Map<String,Object> map = MapLambdaBuilder.of(account)
 *                 .with(Account::getAcctNo)
 *                 .with(Account::getCustNo)
 *                 .with(Account::getCustName)
 *                 .with(Account::getIdCode, "61283291929382912X")
 *                 .with(Account::getMobile)
 *                 .build();
 *         System.out.println(map);
 *
 * @author pengpeng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class MapLambdaBuilder<T> {

    private final Supplier<Map<String,Object>> instantiator;

    private final T example;

    private final Map<String,Object[]> mapEntries = new LinkedHashMap<>();

    private boolean trimEmpty = false;

    private MapLambdaBuilder(Supplier<Map<String,Object>> instantiator, T example) {
        Assert.notNull(instantiator, "Parameter 'instantiator' can not be null!");
        this.instantiator = instantiator;
        this.example = example;
    }

    /**
     * 将一个样例对象作为数据来源，构造一个MapLambdaBuilder，默认构造出来的Map类型为LinkedHashMap.
     *
     * @param example   - 样例数据
     * @param <T>
     * @return
     */
    public static <T> MapLambdaBuilder<T> of(T example) {
        return new MapLambdaBuilder<>(LinkedHashMap::new, example);
    }

    /**
     * 将一个样例的Supplier<T>作为数据来源，构造一个MapLambdaBuilder，默认构造出来的Map类型为LinkedHashMap.
     *
     * @param exampleSupplier   - 样例数据的Supplier
     * @param <T>
     * @return
     */
    public static <T> MapLambdaBuilder<T> of(Supplier<T> exampleSupplier) {
        return new MapLambdaBuilder<>(LinkedHashMap::new, exampleSupplier.get());
    }

    /**
     * 将一个样例对象作为数据来源，构造一个MapLambdaBuilder，同时可以指定构造出来的Map实际类型
     *
     * @param instantiator  - 指定构造出来的Map的实际类型
     * @param example       - 样例数据
     * @param <T>
     * @return
     */
    public static <T> MapLambdaBuilder<T> of(Supplier<Map<String,Object>> instantiator, T example) {
        return new MapLambdaBuilder<>(instantiator, example);
    }

    /**
     * 在没有样例数据的情况下构造一个MapLambdaBuilder，默认构造出来的Map类型为LinkedHashMap.
     * 此时后续调用with(...)方法时，第二个参数defaultValue必须要传值!
     *
     * 使用示例：
     *      Map<String,Object> map1 = MapLambdaBuilder.<ProductBaseInfo>ofEmpty()
     *                 .with(ProductBaseInfo::getProductId, 1L)
     *                 .with(ProductBaseInfo::getProductName, "三只松鼠")
     *                 .build();
     *
     * @param <T>
     * @return
     */
    public static <T> MapLambdaBuilder<T> ofEmpty() {
        return new MapLambdaBuilder<>(LinkedHashMap::new, null);
    }

    /**
     * 将指定对象字段的name和value放入map中
     *
     * @param getterReference   - 指定对象的字段
     * @param <R>
     * @return
     */
    public <R> MapLambdaBuilder<T> with(SerializableFunction<T,R> getterReference) {
        if(getterReference != null) {
            Field field = BeanIntrospector.introspectField(getterReference);
            String fieldName = field.getName();
            mapEntries.put(fieldName, new Object[] {getterReference, "example"});
        }
        return this;
    }

    /**
     * 将指定对象字段的name和overrideValue放入map中
     *
     * @param getterReference   - 指定对象的字段
     * @param overrideValue     - 使用overrideValue强制覆盖指定字段的值
     * @param <R>
     * @return
     */
    public <R> MapLambdaBuilder<T> withOverride(SerializableFunction<T,R> getterReference, R overrideValue) {
        if(getterReference != null) {
            Field field = BeanIntrospector.introspectField(getterReference);
            String fieldName = field.getName();
            mapEntries.put(fieldName, new Object[] {getterReference, "override", overrideValue});
        }
        return this;
    }

    /**
     * 将指定对象字段的name和value放入map中
     *
     * @param getterReference   - 指定对象的字段
     * @param defaultValue      - 如果object的指定字段值为空值(null，空串)，则使用defaultValue替代之
     * @param <R>
     * @return
     */
    public <R> MapLambdaBuilder<T> withDefault(SerializableFunction<T,R> getterReference, R defaultValue) {
        if(getterReference != null) {
            Field field = BeanIntrospector.introspectField(getterReference);
            String fieldName = field.getName();
            mapEntries.put(fieldName, new Object[] {getterReference, "default", defaultValue});
        }
        return this;
    }

    /**
     * 剔除空值(null, 空串)
     * @return
     */
    public MapLambdaBuilder<T> trimEmpty() {
        this.trimEmpty = true;
        return this;
    }

    public Map<String,Object> build() {
        final Map<String,Object> map = instantiator.get();
        mapEntries.forEach((fieldName, contexts) -> {
            String command = (String) contexts[1];
            Object fieldValue = null;
            if("override".equals(command)) { //如果是覆盖操作则直接决定了value值
                fieldValue = contexts[2];
            } else { //否则需要尝试从example对象实例中获取
                fieldValue = example != null ? ((SerializableFunction<T,Object>)contexts[0]).apply(example) : null;
            }
            if("default".equals(command)) { //如果存在default值?
                fieldValue = ObjectUtils.isEmpty(fieldValue) ? contexts[2] : fieldValue;
            }
            if(trimEmpty) { //提出所有为空的value?
                if(!ObjectUtils.isEmpty(fieldValue)) {
                    map.put(fieldName, fieldValue);
                }
            } else {
                map.put(fieldName, fieldValue);
            }
        });
        return map;
    }

}
