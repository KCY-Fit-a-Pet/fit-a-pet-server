package kr.co.fitapet.domain.common.redis.sms.dao;

import kr.co.fitapet.domain.common.redis.sms.domain.SmsUid;
import org.springframework.data.repository.CrudRepository;

public interface SmsUidRepository extends CrudRepository<SmsUid, String> {
}
