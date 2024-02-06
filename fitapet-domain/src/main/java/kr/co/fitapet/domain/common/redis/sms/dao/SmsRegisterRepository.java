package kr.co.fitapet.domain.common.redis.sms.dao;

import kr.co.fitapet.domain.common.redis.sms.domain.SmsRegister;
import org.springframework.data.repository.CrudRepository;

public interface SmsRegisterRepository extends CrudRepository<SmsRegister, String> {
}
