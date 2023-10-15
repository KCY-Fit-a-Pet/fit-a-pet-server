package com.kcy.fitapet.global.common.util.dtobind;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link Dto} 어노테이션이 선언된 클래스를 JSend 형식의 Map으로 변환한다.
 */
@Slf4j
@Component
public final class DtoMapper {
    private final Map<String, Object> format = new HashMap<>();

    /**
     * data 객체를 JSend 형식의 Map으로 변환한다. <br/>
     * 이 때, data 객체는 @Dto 어노테이션이 선언되어 있어야 하며, name 속성이 JSON의 key 값으로 사용된다. <br/>
     * 응답 도메인이 여러 개인 경우, 외부 클래스에서 중첩 클래스를 관리하는 필드는 @JsonIgnore 어노테이션을 선언해야 하며, <br/>
     * 최종 응답 형태에서 외부 클래스와 동일한 레벨로 분리된다. <br/>
     * 중첩 클래스는 @InnerDto 어노테이션을 선언해야 하며, name 속성이 JSON의 key 값으로 사용된다. <br/>
     * 중첨 클래스 내에서 직렬화를 원치 않는 필드는 @JsonIgnore 어노테이션을 선언해야 한다. (혹은 getter 메서드를 선언하지 않으면 된다.)
     * @param data Object : @Dto 타입 객체
     * @return Map<String, Object>
     *
     * @todo 10/14/2023 Pageable 객체 처리
     * @body Pageable 객체를 JSend 형식의 Map으로 변환할 수 있도록 처리한다. (PageDto를 상속받아서 사용)
     */
    public <T> Map<String, Object> from(T data) {
        // 1. data의 클래스를 가져온다.
        Class<?> type = data.getClass();

        // 2. 외부 및 내부 클래스를 가져온다.
        // (단, Builder 클래스 및 @Dto가 정의되지 않은 클래스는 제외한다.)
        List<Object> instances = Arrays.stream(type.getNestMembers())
                .filter(cl -> !cl.getName().endsWith("Builder") || cl.isAnnotationPresent(Dto.class))
                .map(this::getInstance)// 3. 클래스 생성자를 이용해 인스턴스 생성
                .toList();

        // 4. data의 값을 도메인 별로 binding하여 Map 형태로 포매팅한다. setter가 없으므로 getter 메서드를 이용한다.
        // (단, @JsonIgnore가 선언된 필드는 내부 클래스에 포함되지 않으며, 단지 중첩 클래스에 값을 복사하기 위해 사용한다.)
        instances.forEach(instance -> {
            if (instance.getClass().isAnnotationPresent(InnerDto.class)) {
                format.put(instance.getClass().getAnnotation(InnerDto.class).name(), extractInnerDtoValues(data, instance));
            } else {
                format.put(instance.getClass().getAnnotation(Dto.class).name(), extractDtoValues(data, instance));
            }
        });

        return format;
    }

    private Object getInstance(Class<?> cl) {
        	Object instance;
            try {
                instance = cl.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                log.error("[ERROR] Dto 클래스에 기본 생성자가 없습니다.");
                throw new GlobalErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                log.error("[ERROR] Dto 클래스를 생성할 수 없습니다.");
                throw new GlobalErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            return instance;
    }

    private <T> Object extractInnerDtoValues(T data, Object instance) {
        Class<?> declaringClass = instance.getClass().getDeclaringClass();
        String name = instance.getClass().getAnnotation(InnerDto.class).name();
        String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);

        log.info("[INFO] 내부 클래스 {} - 외부 클래스 {}", instance.getClass().getName(), declaringClass.getName());
        log.info("[INFO] 외부 클래스 메서드 호출 : getterName : {}", getterName);

        try {
            Method getter = declaringClass.getMethod(getterName);
            Object value = getter.invoke(data);

            return ObjectUtils.isEmpty(value) ? null : value;
        } catch (Exception e) {
            log.error("[ERROR] Dto 클래스의 getter 메서드를 호출할 수 없거나 필드에 값을 할당할 수 없습니다.");
            throw new GlobalErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private <T> Map<String, Object> extractDtoValues(T data, Object instance) {
        Map<String, Object> fields = new HashMap<>();
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonIgnore.class)) continue;

            String fieldName = field.getName();
            String fieldGetterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            try {
                Method fieldGetter = instance.getClass().getMethod(fieldGetterName);
                Object fieldValue = fieldGetter.invoke(data);

                fields.put(fieldName, fieldValue);
            } catch (Exception e) {
                log.error("[ERROR] Dto 클래스의 getter 메서드를 호출할 수 없거나 필드에 값을 할당할 수 없습니다.");
                throw new GlobalErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        return fields;
    }
}
