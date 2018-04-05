package com.musixise.musixisebox.transfter;

import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.controller.vo.resp.favorite.FavoriteVO;
import com.musixise.musixisebox.domain.Favorite;
import com.musixise.musixisebox.utils.CommonUtil;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class FavoriteTransfter {

    public static FavoriteVO getFavoriteVO(Favorite favorite) {
        FavoriteVO favoriteVO = new FavoriteVO();
        CommonUtil.copyPropertiesIgnoreNull(favorite, favoriteVO);
        return favoriteVO;
    }

    public static FavoriteVO getFavoriteWithUser(Favorite favorite, UserVO userVO) {
        FavoriteVO favoriteVO = new FavoriteVO();
        CommonUtil.copyPropertiesIgnoreNull(favorite, favoriteVO);
        favoriteVO.setUser(userVO);
        return favoriteVO;
    }

}
