package com.goods.common.vo.business;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageVO<T> {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private long total;
    private List<T> rows=new ArrayList<>();

    public PageVO(long total, List<T> data) {
        this.total = total;
        this.rows = data;
    }
    public  PageVO(){}
}
