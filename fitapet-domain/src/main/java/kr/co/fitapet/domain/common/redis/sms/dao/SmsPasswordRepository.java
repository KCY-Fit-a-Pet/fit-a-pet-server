package kr.co.fitapet.domain.common.redis.sms.dao;

import kr.co.fitapet.domain.common.redis.sms.domain.SmsPassword;
import org.springframework.data.repository.CrudRepository;

public interface SmsPasswordRepository extends CrudRepository<SmsPassword, String> {
}
