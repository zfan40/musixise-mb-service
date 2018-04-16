package com.musixise.musixisebox.transfter;

import com.musixise.musixisebox.controller.vo.resp.favorite.FavoriteVO;
import com.musixise.musixisebox.controller.vo.resp.work.WorkVO;
import com.musixise.musixisebox.domain.Favorite;
import com.musixise.musixisebox.utils.CommonUtil;
import com.musixise.musixisebox.utils.DateUtil;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class FavoriteTransfter {

    public static FavoriteVO getFavoriteVO(Favorite favorite) {
        FavoriteVO favoriteVO = new FavoriteVO();
        CommonUtil.copyPropertiesIgnoreNull(favorite, favoriteVO);
        favoriteVO.setCreatedDate(DateUtil.asDate(favorite.getCreatedDate()));
        return favoriteVO;
    }

    public static FavoriteVO getFavoriteWithUser(WorkVO workVO) {
        FavoriteVO favoriteVO = new FavoriteVO();
        CommonUtil.copyPropertiesIgnoreNull(workVO, favoriteVO);
        favoriteVO.setUser(workVO.getUserVO());
        return favoriteVO;
    }

}
