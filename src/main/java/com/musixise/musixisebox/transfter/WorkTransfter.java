package com.musixise.musixisebox.transfter;

import com.musixise.musixisebox.controller.vo.req.user.CreateWork;
import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.controller.vo.resp.work.WorkVO;
import com.musixise.musixisebox.domain.Work;
import com.musixise.musixisebox.utils.CommonUtil;
import com.musixise.musixisebox.utils.DateUtil;
import com.musixise.musixisebox.utils.FileUtil;
import com.musixise.musixisebox.utils.StringUtil;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class WorkTransfter {

    public static Work getWork(CreateWork createWork) {
        Work work = new Work();
        CommonUtil.copyPropertiesIgnoreNull(createWork, work);

        return work;
    }

    public static WorkVO getWorkVO(Work work) {
        WorkVO workVO = new WorkVO();
        CommonUtil.copyPropertiesIgnoreNull(work, workVO);
        workVO.setCreatedDate(DateUtil.asDate(work.getCreatedDate()));
        workVO.setLastModifiedDate(DateUtil.asDate(work.getLastModifiedDate()));
        workVO.setUrl(FileUtil.getAudioFullName(work.getUrl()));
        workVO.setFileHash(StringUtil.getMD5(work.getUrl()));
        if (work.getCollectNum() == null) {
            work.setCollectNum(0);
        }
        return workVO;
    }

    public static WorkVO getWorkVO(Work work, UserVO userVO) {
        WorkVO workVO = getWorkVO(work);
        workVO.setUserVO(userVO);
        return workVO;
    }

    public static WorkVO getWorkVO(Work work, UserVO userVO, Boolean isFavorite) {
        WorkVO workVO  = getWorkVO(work, userVO);
        workVO.setFavStatus(isFavorite ? 1 : 0);
        workVO.setCreatedDate(DateUtil.asDate(work.getCreatedDate()));
        workVO.setLastModifiedDate(DateUtil.asDate(work.getLastModifiedDate()));
        workVO.setUrl(FileUtil.getAudioFullName(work.getUrl()));
        return workVO;
    }
}
