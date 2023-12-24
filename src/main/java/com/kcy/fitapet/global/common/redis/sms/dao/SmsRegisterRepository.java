package com.kcy.fitapet.global.common.redis.sms.dao;

import com.kcy.fitapet.global.common.redis.sms.domain.SmsRegister;
import org.springframework.data.repository.CrudRepository;

public interface SmsRegisterRepository extends CrudRepository<SmsRegister, String> {
}
