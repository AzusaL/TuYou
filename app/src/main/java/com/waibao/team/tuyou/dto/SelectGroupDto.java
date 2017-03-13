package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/6/7.
 */
public class SelectGroupDto {

    private boolean isCheck;
    private GroupDto dto;

    public SelectGroupDto(boolean isCheck, GroupDto dto) {
        this.isCheck = isCheck;
        this.dto = dto;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public GroupDto getDto() {
        return dto;
    }

    public void setDto(GroupDto dto) {
        this.dto = dto;
    }
}
