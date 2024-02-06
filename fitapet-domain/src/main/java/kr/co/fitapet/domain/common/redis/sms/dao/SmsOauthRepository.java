package kr.co.fitapet.domain.common.redis.sms.dao;

import kr.co.fitapet.domain.common.redis.sms.domain.SmsOauth;
import org.springframework.data.repository.CrudRepository;

public interface SmsOauthRepository extends CrudRepository<SmsOauth, String> {
}
