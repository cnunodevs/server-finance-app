package com.cnunodevs.serverfinanceapp.model.mapper;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.dto.PortafolioDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Portafolio;

@Service
public class PortafolioMapper implements GenericMapper<Portafolio, PortafolioDTO>{

    @Override
    public Portafolio dtoToPojo(PortafolioDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dtoToPojo'");
    }

    @Override
    public PortafolioDTO pojoToDto(Portafolio pojo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pojoToDto'");
    }
    
}
