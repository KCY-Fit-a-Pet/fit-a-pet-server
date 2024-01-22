package com.kcy.fitapet.global.config;

import com.querydsl.jpa.DefaultQueryHandler;
import com.querydsl.jpa.Hibernate5Templates;
import com.querydsl.jpa.QueryHandler;
import org.hibernate.query.internal.QueryHelper;

public class CustomHibernate5Templates extends Hibernate5Templates {
    @Override
    public QueryHandler getQueryHandler() {
        return DefaultQueryHandler.DEFAULT;
    }
}
