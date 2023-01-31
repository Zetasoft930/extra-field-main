package com.fieldright.fr.util.pagination;

import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.PageRequest;

import static org.springframework.data.domain.PageRequest.of;

public class Paginacao {

    @ApiParam(
            value = "[paginação] número da página",
            defaultValue = "0",
            example = "3"
    )
    private int page = 0;
    @ApiParam(
            value = "[paginação] quantidade máxima de registros por página",
            defaultValue = "20",
            example = "50"
    )
    private int size = 20;

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public PageRequest simplePageable() {
        return of(page, size);
    }
}
