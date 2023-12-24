package com.kcy.fitapet.global.common.redis.oauth;

import org.springframework.data.repository.CrudRepository;

public interface OIDCTokenRepository extends CrudRepository<OIDCToken, String> {
}
