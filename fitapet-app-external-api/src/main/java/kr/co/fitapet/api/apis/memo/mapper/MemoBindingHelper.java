package kr.co.fitapet.api.apis.memo.mapper;

import kr.co.fitapet.api.apis.memo.dto.MemoPatchReq;
import kr.co.fitapet.api.common.mapstruct.JsonNullableMapper;
import kr.co.fitapet.domain.domains.memo.domain.Memo;
import org.mapstruct.*;

@Mapper(uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface MemoBindingHelper {
//    @Mapping(target = "id", ignore = true)
    Memo map(MemoPatchReq req);

    MemoPatchReq map(Memo memo);

    @InheritConfiguration
    void update(MemoPatchReq update, @MappingTarget Memo destination);
}
