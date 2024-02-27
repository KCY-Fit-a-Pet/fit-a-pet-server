package kr.co.fitapet.domain.domains.device.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "device_token")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceToken;
    private String os;
    private String deviceName;

    @CreatedDate
    private LocalDateTime createdAt;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private DeviceToken(String deviceToken, String os, String deviceName, Member member) {
        this.deviceToken = deviceToken;
        this.os = os;
        this.deviceName = deviceName;
        this.member = member;
    }

    public static DeviceToken of(String deviceToken, String os, String deviceName, Member member) {
        return new DeviceToken(deviceToken, os, deviceName, member);
    }
}
