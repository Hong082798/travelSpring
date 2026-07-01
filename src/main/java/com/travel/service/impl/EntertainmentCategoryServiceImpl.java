package com.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.entity.EntertainmentCategory;
import com.travel.mapper.EntertainmentCategoryMapper;
import com.travel.service.EntertainmentCategoryService;
import org.springframework.stereotype.Service;

@Service
public class EntertainmentCategoryServiceImpl extends ServiceImpl < EntertainmentCategoryMapper,
    EntertainmentCategory > implements EntertainmentCategoryService {
}
