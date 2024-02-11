package kr.co.fitapet.api.common.mapstruct;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = "spring")
public interface JsonNullableMapper {
    default <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    default <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable == null ? null : jsonNullable.orElse(null);
    }

    /**
     * nullable한 파라미터가 명시적으로 전달되었는지 확인한다.
     * @return 명시적으로 전달된 경우 true를, 그렇지 않은 경우 false를 반환
     */
    @Condition
    default <T> boolean isPresent(JsonNullable<T> jsonNullable) {
        return jsonNullable != null && jsonNullable.isPresent();
    }
}
