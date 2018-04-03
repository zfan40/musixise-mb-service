package com.musixise.musixisebox.transfter;

import com.musixise.musixisebox.controller.vo.resp.UserDetail;
import com.musixise.musixisebox.domain.Musixiser;
import com.musixise.musixisebox.utils.CommonUtil;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class UserTransfter {

    public static UserDetail getUserDetail(Musixiser musixiser) {
        UserDetail userDetail = new UserDetail();
        CommonUtil.copyPropertiesIgnoreNull(musixiser, userDetail);
        return userDetail;
    }
}
