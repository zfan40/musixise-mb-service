package com.musixise.musixisebox.server.transfter;

import com.musixise.musixisebox.api.web.vo.req.work.WorkMeta;
import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.utils.CommonUtil;
import com.musixise.musixisebox.server.utils.DateUtil;
import com.musixise.musixisebox.server.utils.FileUtil;
import com.musixise.musixisebox.server.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class WorkTransfter {

    public static Work getWork(WorkMeta workMeta) {
        Work work = new Work();
        CommonUtil.copyPropertiesIgnoreNull(workMeta, work);

        return work;
    }

    public static Work getWork(WorkVO workVO) {
        Work work = new Work();
        CommonUtil.copyPropertiesIgnoreNull(workVO, work);

        return work;

    }

    public static List<WorkVO> getWorkVOs(List<Work> workList) {
        List<WorkVO> workVOList = new ArrayList<>();
        for (Work work : workList) {
            workVOList.add(getWorkVO(work));
        }
        return workVOList;
    }


    public static WorkVO getWorkVO(Work work) {
        WorkVO workVO = new WorkVO();
        CommonUtil.copyPropertiesIgnoreNull(work, workVO);
        workVO.setCreatedDate(DateUtil.asDate(work.getCreatedDate()));
        workVO.setLastModifiedDate(DateUtil.asDate(work.getLastModifiedDate()));
        workVO.setUrl(FileUtil.getAudioFullName(work.getUrl()));
        workVO.setFileHash(StringUtil.getMD5(work.getUrl()));
        if (work.getCollectNum() == null) {
            workVO.setCollectNum(0);
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
