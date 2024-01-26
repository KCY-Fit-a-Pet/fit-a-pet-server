package com.kcy.fitapet.global.common.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.RepositoryFragment;

public class DefaultSearchQueryDslRepositoryFactory<T extends Repository<E, ID>, E, ID> extends JpaRepositoryFactoryBean<T, E, ID> {
    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public DefaultSearchQueryDslRepositoryFactory(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new QueryDslRepositoryFactory(entityManager);
    }

    private static class QueryDslRepositoryFactory extends JpaRepositoryFactory {
        private final EntityManager em;

        /**
         * Creates a new {@link JpaRepositoryFactory}.
         *
         * @param entityManager must not be {@literal null}
         */
        public QueryDslRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.em = entityManager;
        }

        @Override
        protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
            RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

            if (ExtendedJpaRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
                var impl = super.instantiateClass(
                        DefaultSearchQueryDslRepository.class,
                        this.getEntityInformation(metadata.getDomainType()),
                        this.em
                );
                fragments = fragments.append(RepositoryFragment.implemented(impl));
            }
            return fragments;
        }
    }
}
