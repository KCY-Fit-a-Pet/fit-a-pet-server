package com.kcy.fitapet.domain.care.service;

import com.kcy.fitapet.domain.care.dao.CareDateRepository;
import com.kcy.fitapet.domain.care.dao.CareRepository;
import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.care.domain.CareDate;
import com.kcy.fitapet.domain.member.dao.MemberRepository;
import com.kcy.fitapet.domain.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareSaveService {
    private final CareRepository careRepository;
    private final CareDateRepository careDateRepository;


}
