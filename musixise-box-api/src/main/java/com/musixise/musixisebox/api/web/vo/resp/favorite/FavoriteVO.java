package com.musixise.musixisebox.api.web.vo.resp.favorite;

import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class FavoriteVO {

    private Long id;

    @ApiModelProperty(value = "标题", example = "music title")
    private String title;

    @ApiModelProperty(value = "封面", example = "https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png")
    private String cover;

    @ApiModelProperty(value = "描述", example = "")
    private String content;

    @ApiModelProperty(value = "音频地址", example = "http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt")
    private String url;

    @ApiModelProperty(value = "关注收藏(0=未收藏,1=已收藏)", example = "0")
    private Integer followStatus;

    @ApiModelProperty(value = "收藏时间", example = "2018-04-06 17:00:17")
    private String createdDate;

    @ApiModelProperty(value = "作者ID", example = "7")
    private Long userId;

    @ApiModelProperty(value = "收藏数", example = "9999")
    private Integer collectNum;

    @ApiModelProperty(value = "文件HASH值", example = "71fadeb408e4f2271f815eed3af47214")
    private String fileHash;

    @ApiModelProperty(value = "用户信息", example = "")
    private UserVO userVO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }
}
