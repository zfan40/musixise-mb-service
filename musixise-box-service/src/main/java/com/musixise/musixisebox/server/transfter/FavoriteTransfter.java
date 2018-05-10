package com.musixise.musixisebox.server.transfter;

import com.musixise.musixisebox.api.web.vo.resp.favorite.FavoriteVO;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.Favorite;
import com.musixise.musixisebox.server.utils.CommonUtil;
import com.musixise.musixisebox.server.utils.DateUtil;

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
