package com.lin.bulter.wxplatement.common.dto;

import com.lin.bulter.wxplatement.common.dto.common.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserParam implements Serializable {

    private String username;         // 操作人Id
    private PageParam page;

}
