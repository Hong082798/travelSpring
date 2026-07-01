package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DashboardStatsVO，包含仪表盘统计数据的视图对象")
public class DashboardStatsVO {

    @Schema(description = "总用户数")
    private Long userCount;

    @Schema(description = "总景点数")
    private Long scenicCount;

    @Schema(description = "总游记数")
    private Long noteCount;

    @Schema(description = "总评论数")
    private Long commentCount;

    public Long getUserCount() { return userCount; }
    public void setUserCount(Long userCount) { this.userCount = userCount; }

    public Long getScenicCount() { return scenicCount; }
    public void setScenicCount(Long scenicCount) { this.scenicCount = scenicCount; }

    public Long getNoteCount() { return noteCount; }
    public void setNoteCount(Long noteCount) { this.noteCount = noteCount; }

    public Long getCommentCount() { return commentCount; }
    public void setCommentCount(Long commentCount) { this.commentCount = commentCount; }
}
