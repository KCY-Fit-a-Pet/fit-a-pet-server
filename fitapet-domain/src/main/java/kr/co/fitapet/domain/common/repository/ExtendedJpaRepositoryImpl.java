package kr.co.fitapet.domain.common.repository;

import jakarta.persistence.EntityManager;
import kr.co.fitapet.domain.common.exception.DomainErrorCode;
import kr.co.fitapet.domain.common.exception.DomainErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Slf4j
public class ExtendedJpaRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements ExtendedJpaRepository<T, ID> {
    private final EntityManager em;
    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";

    public ExtendedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
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
            throw new DomainErrorException(DomainErrorCode.valueOf("NOT_FOUND_" + getClassName()));
        }
        return result;
    }

    // TODO: 2021-11-30. 이름에 의존적인 메서드 제거하고, 상태 패턴을 적용하여 의존도 낮추기
    private String getClassName() {
        Class<T> domainType = getDomainClass();
        return StringUtils.capitalize(domainType.getSimpleName())
                .replaceAll("(.)(\\p{javaUpperCase})", "$1_$2")
                .toUpperCase();
    }
}
