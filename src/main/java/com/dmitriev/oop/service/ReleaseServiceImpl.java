package com.dmitriev.oop.service;

import com.dmitriev.oop.entity.Release;
import com.dmitriev.oop.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Override
    public List<Release> listRelease() {
        return (List<Release>) releaseRepository.findAll();
    }
}
