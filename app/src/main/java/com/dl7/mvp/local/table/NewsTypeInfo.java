package com.dl7.mvp.local.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by long on 2016/8/31.
 * 新闻类型
 */
@Entity
public class NewsTypeInfo {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String typeId;


    @Generated(hash = 1707873593)
    public NewsTypeInfo(Long id, String name, String typeId) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
    }

    @Generated(hash = 215923915)
    public NewsTypeInfo() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "NewsTypeBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeId='" + typeId + '\'' +
                '}';
    }
}
