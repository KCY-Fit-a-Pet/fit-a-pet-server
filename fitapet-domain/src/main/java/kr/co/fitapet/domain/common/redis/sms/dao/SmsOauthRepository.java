package kr.co.fitapet.domain.common.redis.sms.dao;

import com.kcy.fitapet.global.common.redis.sms.domain.SmsOauth;
import org.springframework.data.repository.CrudRepository;

public interface SmsOauthRepository extends CrudRepository<SmsOauth, String> {
}
