package com.kcy.fitapet.global.common.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ExtendedRepository<T, ID extends Serializable> extends ExtendedJpaRepository<T, ID>, DefaultSearchQueryDslRepository<T> {
}
