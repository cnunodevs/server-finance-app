package com.cnunodevs.serverfinanceapp.service.implementation;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.repository.AhorroRepository;
import com.cnunodevs.serverfinanceapp.service.AhorroService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AhorroServiceImpl implements AhorroService {
    
    private final AhorroRepository ahorroRepo;
}
