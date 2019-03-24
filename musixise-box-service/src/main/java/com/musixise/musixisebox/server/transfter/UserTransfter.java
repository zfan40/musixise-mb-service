package com.musixise.musixisebox.server.transfter;

import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.domain.User;
import com.musixise.musixisebox.server.utils.CommonUtil;
import com.musixise.musixisebox.server.utils.DateUtil;
import com.musixise.musixisebox.server.utils.FileUtil;

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

    public static UserVO getUserVO(Musixiser musixiser) {
        UserVO userVO = new UserVO();
        if (musixiser != null) {
            CommonUtil.copyPropertiesIgnoreNull(musixiser, userVO);
            userVO.setRealname(musixiser.getRealname());
            userVO.setUserId(musixiser.getUserId());
            userVO.setCreatedDate(DateUtil.asDate(musixiser.getCreatedDate()));
            userVO.setSmallAvatar(FileUtil.getImageFullName(musixiser.getSmallAvatar()));
            userVO.setLargeAvatar(FileUtil.getImageFullName(musixiser.getSmallAvatar()));
        }

        return userVO;
    }
}
