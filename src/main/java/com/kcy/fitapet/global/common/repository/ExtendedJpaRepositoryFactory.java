package com.kcy.fitapet.global.common.repository;


import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class ExtendedJpaRepositoryFactory<T extends Repository<E, ID>, E, ID> extends JpaRepositoryFactoryBean<T, E, ID> {
    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public ExtendedJpaRepositoryFactory(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new InnerRepositoryFactory(em);
    }

    private static class InnerRepositoryFactory extends JpaRepositoryFactory {
        private final EntityManager em;

        public InnerRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        @Override
        protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
            var fragments = super.getRepositoryFragments(metadata);

            if (ExtendedRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
                var impl = super.instantiateClass(
                        ExtendedRepositoryImpl.class,
                        this.getEntityInformation(metadata.getDomainType()), this.em
                );

                fragments = fragments.append(RepositoryComposition.RepositoryFragments.just(impl));
            }

            return fragments;
        }
    }
}
