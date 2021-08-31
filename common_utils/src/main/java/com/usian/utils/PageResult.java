package com.usian.utils;

import java.util.List;

/**
 * @Title: PageResult
 * @Description: 某页的结果集
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/6 14:52
 */
public class PageResult<T> {

    private Integer pageIndex;
    private Long totalPage;
    private List<T> result;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
