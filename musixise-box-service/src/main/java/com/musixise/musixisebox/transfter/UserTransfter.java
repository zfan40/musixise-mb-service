package com.musixise.musixisebox.transfter;

import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.domain.Musixiser;
import com.musixise.musixisebox.domain.User;
import com.musixise.musixisebox.utils.CommonUtil;
import com.musixise.musixisebox.utils.DateUtil;
import com.musixise.musixisebox.utils.FileUtil;

/**
 * Created by zhaowei on 2018/4/3.
 */
public class UserTransfter {

    public static UserVO getUserDetail(Musixiser musixiser, User user) {
        UserVO userVO = new UserVO();
        CommonUtil.copyPropertiesIgnoreNull(musixiser, userVO);
        userVO.setUsername(user.getLogin());
        userVO.setEmail(user.getEmail());
        userVO.setCreatedDate(DateUtil.asDate(musixiser.getCreatedDate()));
        userVO.setSmallAvatar(FileUtil.getImageFullName(userVO.getSmallAvatar()));
        userVO.setLargeAvatar(FileUtil.getImageFullName(userVO.getSmallAvatar()));

        return userVO;
    }
}
