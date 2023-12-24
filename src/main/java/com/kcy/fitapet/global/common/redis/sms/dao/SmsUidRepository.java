package com.kcy.fitapet.global.common.redis.sms.dao;

import com.kcy.fitapet.global.common.redis.sms.domain.SmsUid;
import org.springframework.data.repository.CrudRepository;

public interface SmsUidRepository extends CrudRepository<SmsUid, String> {
}
