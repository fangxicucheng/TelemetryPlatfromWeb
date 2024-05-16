package com.fang.service.commandCountService;

import com.fang.database.postgresql.repository.CommandCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandCountService {
    @Autowired
    private CommandCountRepository commandCountDao;
}
