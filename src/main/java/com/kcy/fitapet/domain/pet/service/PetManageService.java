package com.kcy.fitapet.domain.pet.service;

import com.kcy.fitapet.domain.pet.dao.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetManageService {
    private final PetRepository petRepository;


}
