package com.ljc.workprogress.domain.dto;

import java.util.List;

/**
 * @author liangjichao
 * @date 2023/10/17 8:53 PM
 */
public class WpsPageDto {
    /**
     * 当前页
     */
    private Long cpage;
    /**
     * 每页记录个数
     */
    private Integer pageSize;
    /**
     * 总记录
     */
    private Long rows;

    private List<WpsDto> pageData;

    /**
     * 获取总页数
     * @return
     */
    public Long getTotalPage() {
        return (rows+pageSize-1)/pageSize;
    }

    public Long getCpage() {
        return cpage;
    }

    public void setCpage(Long cpage) {
        this.cpage = cpage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public List<WpsDto> getPageData() {
        return pageData;
    }

    public void setPageData(List<WpsDto> pageData) {
        this.pageData = pageData;
    }
}
