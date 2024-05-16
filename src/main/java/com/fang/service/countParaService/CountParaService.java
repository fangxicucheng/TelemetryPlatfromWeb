package com.fang.service.countParaService;

import com.fang.database.postgresql.repository.CountParaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountParaService {
    @Autowired
    private CountParaRepository countParaDao;
}
