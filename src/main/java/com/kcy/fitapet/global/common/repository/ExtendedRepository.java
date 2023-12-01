package com.kcy.fitapet.global.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ExtendedRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    /**
     * ID로 조회한 결과가 없을 경우 예외를 발생시키는 메서드
     * @param id : 조회할 ID
     * @return : 조회한 결과 도메인
     */
    T findByIdOrElseThrow(ID id);
}
