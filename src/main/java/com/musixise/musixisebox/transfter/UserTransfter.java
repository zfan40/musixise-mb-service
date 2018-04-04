package com.musixise.musixisebox.transfter;

import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.domain.Musixiser;
import com.musixise.musixisebox.utils.CommonUtil;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class UserTransfter {

    public static UserVO getUserDetail(Musixiser musixiser) {
        UserVO userVO = new UserVO();
        CommonUtil.copyPropertiesIgnoreNull(musixiser, userVO);
        return userVO;
    }
}
