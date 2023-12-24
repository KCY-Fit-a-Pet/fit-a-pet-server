package com.kcy.fitapet.global.common.redis.sms.dao;

import com.kcy.fitapet.global.common.redis.sms.domain.SmsPassword;
import org.springframework.data.repository.CrudRepository;

public interface SmsPasswordRepository extends CrudRepository<SmsPassword, String> {
}
