package com.musixise.musixisebox.api.web.vo.req.work;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class WorkMeta {

    @ApiModelProperty(value = "作品ID", example = "0")
    private Long id;

    @ApiModelProperty(value = "作品标题", required = true, example = "1")
    @NotNull(message = "作品标题不能为空")
    @Size(min = 3, max = 50)
    private String title;

    @ApiModelProperty(value = "封面", example = "https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png")
    private String cover;

    @ApiModelProperty(value = "描述", example = "")
    private String content;

    @NotNull
    @ApiModelProperty(value = "音频地址", required = true, example = "http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt")
    private String url;

    @ApiModelProperty(value = "作品状态(0=公开，1=私人，2=垃圾箱)", example = "0")
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WorkMeta{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                '}';
    }
}
