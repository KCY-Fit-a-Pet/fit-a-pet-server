package kr.co.fitapet.api.apis.memo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.fitapet.api.apis.memo.dto.MemoCategorySaveRes;
import kr.co.fitapet.api.apis.memo.dto.MemoUpdateReq;
import kr.co.fitapet.api.apis.memo.usecase.MemoUseCase;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import kr.co.fitapet.api.apis.memo.dto.MemoSaveReq;
import kr.co.fitapet.api.apis.memo.dto.SubMemoCategorySaveReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "메모 API", description = "메모 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/pets/{pet_id}")
public class MemoApi {
    private final MemoUseCase memoUseCase;

    @Operation(summary = "서브 메모 카테고리 저장")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "root_memo_category_id", description = "루트 메모 카테고리 ID를 의미하며, 서브 메모 카테고리인 경우 거부됩니다.", in = ParameterIn.PATH, required = true)
    })
    @PostMapping("/root-memo-categories/{root_memo_category_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidRootMemoCategory(#rootMemoCategoryId, #petId)")
    public ResponseEntity<?> saveSubMemoCategory(
            @PathVariable("pet_id") Long petId,
            @PathVariable("root_memo_category_id") Long rootMemoCategoryId,
            @RequestBody @Valid SubMemoCategorySaveReq req
    ) {
        MemoCategorySaveRes res = memoUseCase.saveSubMemoCategory(petId, rootMemoCategoryId, req);
        return ResponseEntity.ok(SuccessResponse.from(res));
    }

    @Deprecated(since = "2024-02-18")
    @Operation(summary = "메모 카테고리 리스트 조회", description = "메모 카테고리 타입이 root인 경우, 서브 메모 카테고리 리스트도 함께 조회합니다.")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_category_id", description = "루트 메모 카테고리 ID를 의미하며, 서브 메모 카테고리인 경우 거부됩니다.", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/memo-categories/{memo_category_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidMemoCategory(#memoCategoryId, #petId)")
    public ResponseEntity<?> getSubMemoCategories(
            @PathVariable("pet_id") Long petId,
            @PathVariable("memo_category_id") Long memoCategoryId
    ) {
        return ResponseEntity.ok(SuccessResponse.from(memoUseCase.findCategoryById(memoCategoryId)));
    }

    @Operation(summary = "서브 메모 카테고리 삭제")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "sub_memo_category_id", description = "서브 메모 카테고리 ID를 의미하며, 루트 메모 카테고리인 경우 거부됩니다.", in = ParameterIn.PATH, required = true)
    })
    @DeleteMapping("/sub-memo-categories/{sub_memo_category_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidSubMemoCategory(#subMemoCategoryId, #petId)")
    public ResponseEntity<?> deleteSubMemoCategory(@PathVariable("pet_id") Long petId, @PathVariable("sub_memo_category_id") Long subMemoCategoryId) {
        return null;
    }

    @Operation(summary = "폴더 안의 메모 리스트 조회 및 검색", description = """
        타입이 root인 경우, sub category 리스트는 별도로 조회해야 합니다. `/api/v2/pets/{pet_id}/root-memo-categories/{root_memo_category_id}`
        search에 검색어를 입력하면 해당 검색어를 포함하는 메모 리스트를 조회합니다. (빈 문자열인 경우, 전체 메모 리스트를 조회합니다.)
        """)
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_category_id", description = "메모 카테고리 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_id", description = "메모 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "search", description = "검색어", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지 사이즈", example = "5", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호", example = "1", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬 기준", example = "createdAt", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "정렬 방식", example = "DESC" , in = ParameterIn.QUERY)
    })
    @GetMapping("/memo-categories/{memo_category_id}/memos")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidMemoCategory(#memoCategoryId, #petId)")
    public ResponseEntity<?> getMemosAndSubCategories(
            @PathVariable("pet_id") Long petId, @PathVariable("memo_category_id") Long memoCategoryId,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @PageableDefault(size = 5, page = 0) @SortDefault.SortDefaults({
                    @SortDefault(sort = "memo.createdAt", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "memoImage.id", direction = Sort.Direction.ASC)}
            ) Pageable pageable
    ) {
        MemoInfoDto.PageResponse res = memoUseCase.findMemosInMemoCategory(memoCategoryId, pageable, search);
        return ResponseEntity.ok(SuccessResponse.from(res));
    }

    /*
     * 메모 API
     */

    @Operation(summary = "반려동물에게 등록된 메모 리스트 조회", description = """
            기본 값으로는 상위/하위 카테고리를 포함한 가장 최근 내역의 메모 리스트를 조회합니다. (페이지 사이즈: 5, 페이지 번호: 1, 정렬 기준: createdAt, 정렬 방식: DESC)""")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "size", description = "페이지 사이즈", example = "5", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호", example = "1", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬 기준", example = "createdAt", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "정렬 방식", example = "DESC" , in = ParameterIn.QUERY)
    })
    @GetMapping("/memos")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getMemosByPet(
            @PathVariable("pet_id") Long petId,
            @PageableDefault(size = 5, page = 0) @SortDefault.SortDefaults({
                    @SortDefault(sort = "memo.createdAt", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "memoImage.id", direction = Sort.Direction.ASC)}
            ) Pageable pageable
    ) {
        MemoInfoDto.PageResponse res = memoUseCase.findMemosByPetId(petId, pageable);
        return ResponseEntity.ok(SuccessResponse.from(res));
    }

    @Operation(summary = "메모 단건 조회")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_category_id", description = "메모 카테고리 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_id", description = "메모 ID", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/memo-categories/{memo_category_id}/memos/{memo_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidMemoCategoryAndMemo(#memoCategoryId, #memoId, #petId)")
    public ResponseEntity<?> getMemo(
            @PathVariable("pet_id") Long petId,
            @PathVariable("memo_category_id") Long memoCategoryId,
            @PathVariable("memo_id") Long memoId
    ) {
        MemoInfoDto.MemoInfo info = memoUseCase.findMemoAndMemoImageUrlsById(memoId);
        return ResponseEntity.ok(SuccessResponse.from(info));
    }

    @Operation(summary = "메모 작성")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_category_id", in = ParameterIn.PATH, required = true)
    })
    @PostMapping("/memo-categories/{memo_category_id}/memos")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidMemoCategory(#memoCategoryId, #petId)")
    public ResponseEntity<?> saveMemo(
            @PathVariable("pet_id") Long petId,
            @PathVariable("memo_category_id") Long memoCategoryId,
            @RequestBody @Valid MemoSaveReq req
    ) {
        return ResponseEntity.ok(SuccessResponse.from(memoUseCase.saveMemo(memoCategoryId, req)));
    }

    @Operation(summary = "메모 삭제")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_category_id", description = "메모 카테고리 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_id", description = "메모 ID", in = ParameterIn.PATH, required = true)
    })
    @DeleteMapping("/memo-categories/{memo_category_id}/memos/{memo_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidMemoCategoryAndMemo(#memoCategoryId, #memoId, #petId)")
    public ResponseEntity<?> deleteMemo(
            @PathVariable("pet_id") Long petId,
            @PathVariable("memo_category_id") Long memoCategoryId,
            @PathVariable("memo_id") Long memoId
    ) {
        memoUseCase.deleteMemo(memoId);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "메모 수정")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_category_id", description = "메모 카테고리 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "memo_id", description = "메모 ID", in = ParameterIn.PATH, required = true)
    })
    @PutMapping("/memo-categories/{memo_category_id}/memos/{memo_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @memoAuthorize.isValidMemoCategoryAndMemo(#memoCategoryId, #memoId, #petId)" +
            "and @memoAuthorize.isValidMemoCategory(#req.memoCategoryId(), #petId)")
    public ResponseEntity<?> putMemo(
            @PathVariable("pet_id") Long petId,
            @PathVariable("memo_category_id") Long memoCategoryId,
            @PathVariable("memo_id") Long memoId,
            @RequestBody @Valid MemoUpdateReq req
    ) {
        memoUseCase.updateMemo(memoId, req);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
