package com.travel.service.impl;

import com.travel.service.*;
import com.travel.vo.DashboardStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service  // 交给 Spring 管理，成为一个可被注入的 Bean
public class DashboardServiceImpl implements DashboardService {

    // 注入各个模块的 Service —— 复用它们的 count()，而不是自己写 SQL
    @Autowired
    private UserService userService;
    @Autowired
    private ScenicSpotService scenicSpotService;
    @Autowired
    private TravelNoteService travelNoteService;
    @Autowired
    private CommentService commentService;

    @Override
    public DashboardStatsVO getStats() {
        DashboardStatsVO vo = new DashboardStatsVO();
        // count() 是 MyBatis-Plus ServiceImpl 自带方法，
        // 它是"生成的 SQL"，自动带 is_deleted=0，数的是未删除的记录
        vo.setUserCount( userService.count() );
        vo.setScenicCount( scenicSpotService.count() );
        vo.setNoteCount( travelNoteService.count() );
        vo.setCommentCount( commentService.count() );
        return vo;
    }
}