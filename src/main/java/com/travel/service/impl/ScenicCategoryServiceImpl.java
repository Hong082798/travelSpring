package com.travel.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.entity.ScenicCategory;
import com.travel.mapper.ScenicCategoryMapper;
import com.travel.service.ScenicCategoryService;

@Service
public class ScenicCategoryServiceImpl extends ServiceImpl < ScenicCategoryMapper, ScenicCategory > implements ScenicCategoryService {
}
