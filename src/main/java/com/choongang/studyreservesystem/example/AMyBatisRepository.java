package com.choongang.studyreservesystem.example;

public class AMyBatisRepository implements ARepository {

    private final MyBatisMapper myBatisMapper;

    public AMyBatisRepository(MyBatisMapper myBatisMapper) {
        this.myBatisMapper = myBatisMapper;
    }

    @Override
    public void save() {

    }
}
