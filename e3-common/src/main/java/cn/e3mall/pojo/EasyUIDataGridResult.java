package cn.e3mall.pojo;
//要通过网络传输对象，所以已实现序列化接口

import java.io.Serializable;
import java.util.List;

public class EasyUIDataGridResult implements Serializable {
    private long total;
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
