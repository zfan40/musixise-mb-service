package com.musixise.musixisebox.server.transfter;

import com.musixise.musixisebox.api.admin.vo.common.MusixiserVO;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.utils.CommonUtil;
import com.musixise.musixisebox.server.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowei on 2018/5/11.
 */
public class MusixiserTransfter {

    public static Musixiser getMusixiser(MusixiserVO musixiserVO) {
        Musixiser musixiser = new Musixiser();
        CommonUtil.copyPropertiesIgnoreNull(musixiserVO, musixiser);
        return musixiser;
    }

    public static MusixiserVO getMusixiserVO(Musixiser musixiser) {

        MusixiserVO musixiserVO = new MusixiserVO();
        CommonUtil.copyPropertiesIgnoreNull(musixiser, musixiserVO);
        musixiserVO.setCreatedDate(DateUtil.asDate(musixiser.getCreatedDate()));
        return musixiserVO;
    }

    public static List<MusixiserVO> getMusixiserVOS(List<Musixiser> musixiserList) {
        List<MusixiserVO> musixiserVOList = new ArrayList<>();
        for (Musixiser musixiser : musixiserList) {
            MusixiserVO musixiserVO = getMusixiserVO(musixiser);
            musixiserVOList.add(musixiserVO);
        }

        return musixiserVOList;
    }
}
