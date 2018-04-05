package com.musixise.musixisebox.transfter;

import com.musixise.musixisebox.controller.vo.req.user.CreateWork;
import com.musixise.musixisebox.controller.vo.resp.work.WorkVO;
import com.musixise.musixisebox.domain.Work;
import com.musixise.musixisebox.utils.CommonUtil;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class WorkTransfter {

    public static Work getWord(CreateWork createWork) {
        Work work = new Work();
        CommonUtil.copyPropertiesIgnoreNull(createWork, work);
        return work;
    }

    public static WorkVO getWorkVO(Work work) {
        WorkVO workVO = new WorkVO();
        CommonUtil.copyPropertiesIgnoreNull(work, workVO);
        return workVO;
    }
}
