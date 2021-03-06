package com.goshop.manager.pojo;

import java.io.Serializable;

public class GoodsType  implements Serializable {
    private Integer typeId;

    private String typeName;

    private Boolean typeSort;

    private Integer classId;

    private String className;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Boolean getTypeSort() {
        return typeSort;
    }

    public void setTypeSort(Boolean typeSort) {
        this.typeSort = typeSort;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }
}