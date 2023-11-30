package com.kcy.fitapet.global.common.repository;

import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Slf4j
public class ExtendedRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements ExtendedRepository<T, ID> {
    private final EntityManager em;
    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";

    public ExtendedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;
    }

    @Override
    public T findByIdOrElseThrow(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Class<T> domainType = getDomainClass();
        T result = em.find(domainType, id);

        if (result == null) {
            log.error("{} id not found", domainType.getSimpleName());
            throw new GlobalErrorException(ErrorCode.valueOf("NOT_FOUND_" + getClassName()));
        }
        return result;
    }

    private String getClassName() {
        Class<T> domainType = getDomainClass();
        return StringUtils.capitalize(domainType.getSimpleName())
                .replaceAll("(.)(\\p{javaUpperCase})", "$1_$2")
                .toLowerCase();
    }
}
