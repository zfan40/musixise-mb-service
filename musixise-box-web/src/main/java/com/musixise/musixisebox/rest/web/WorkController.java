package com.musixise.musixisebox.rest.web;

import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.WorkApi;
import com.musixise.musixisebox.api.web.vo.req.work.WorkMeta;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.aop.MusixiseContext;
import com.musixise.musixisebox.server.domain.MidiFile;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.manager.WorkManager;
import com.musixise.musixisebox.server.repository.MidiFileRepository;
import com.musixise.musixisebox.server.repository.WorkRepository;
import com.musixise.musixisebox.server.service.MusixiseService;
import com.musixise.musixisebox.server.service.WorkService;
import com.musixise.musixisebox.server.transfter.WorkTransfter;
import com.musixise.musixisebox.server.utils.CommonUtil;
import com.musixise.musixisebox.server.utils.MidiUtil;
import com.musixise.musixisebox.server.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/5.
 */

@RestController

@RequestMapping("/api/v1/work")
public class WorkController implements WorkApi {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource WorkRepository workRepository;

    @Resource MusixiseService musixiseService;

    @Resource WorkManager workManager;

    @Resource WorkService workService;

    @Resource MidiFileRepository midiFileRepository;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse<Long> create(Long uid, @Valid @RequestBody WorkMeta workMeta) {

        uid = MusixiseContext.getCurrentUid();
        Work work = WorkTransfter.getWork(workMeta);
        work.setUserId(uid);

        //get machine num
        Optional<MidiFile> midiFile = midiFileRepository.findByMd5(StringUtil.getMD5(work.getUrl()));
        Integer MatchineNum = midiFile.map(MidiFile::getMachineNum).orElse(0);
        work.setMachineNum(MatchineNum);

        workRepository.save(work);
        musixiseService.updateWorkCount(uid);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, work.getId());
    }

    @RequestMapping(value = "/getListByUid/{uid}", method = RequestMethod.GET)
    @AppMethod
    @Override
    public MusixisePageResponse<List<WorkVO>> getListByUid(@PathVariable Long uid,
                                                           @RequestParam(value = "title", required = false) String title,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Work> workList = null;
        if (title == null) {
            workList = workRepository.findAllByUserIdOrderByIdDesc(uid, pageable);
        } else {
            workList = workRepository.findAllByUserIdAndTitleLikeOrderByIdDesc(uid, "%"+title+"%", pageable);
        }

        List<WorkVO> workVOList = workService.getWorkList(workList.getContent());

        return new MusixisePageResponse<>(workVOList, workList.getTotalElements(), size, page);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @AppMethod
    @Override
    public MusixiseResponse<WorkVO> getDetail(@PathVariable Long id) {
        Optional<Work> work = workRepository.findById(id);
        return work.map(w -> {
            WorkVO workVO = workManager.getWorkVO(MusixiseContext.getCurrentUid(), w);
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, workVO);
        }).orElse( new MusixiseResponse<>(ExceptionMsg.NOT_EXIST));
    }

    @Override
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @AppMethod(isLogin = true)
    public MusixiseResponse<Boolean> delete(Long id) {
        Optional<Work> work = workRepository.findById(id);
        return work.map( t -> {

            if (t.getUserId().equals(MusixiseContext.getCurrentUid())) {
                workRepository.deleteById(id);
                return new MusixiseResponse<>(ExceptionMsg.SUCCESS, true);
            } else {
                //无权限
                return new MusixiseResponse<>(ExceptionMsg.FAILED, false);
            }

        }).orElse(new MusixiseResponse<>(ExceptionMsg.NOT_EXIST));
    }

    @RequestMapping(value = "/updateWork/{id}", method = RequestMethod.PUT)
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse<?> update(Long uid, @PathVariable Long id, @RequestBody WorkMeta workMeta) {
        uid = MusixiseContext.getCurrentUid();
        return workRepository.findById(id).map(work -> {
            CommonUtil.copyPropertiesIgnoreNull(workMeta, work);
            workRepository.save(work);
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS);
        }).orElse(new MusixiseResponse<>(ExceptionMsg.FAILED));
    }

    @RequestMapping(value = "/recountMachineNum", method = RequestMethod.POST)
    public MusixiseResponse<?> recountMachineNum() {
        List<Work> all = workRepository.findAll();

        all.forEach( work -> {
            try {
                String url = work.getUrl();
                if (url.indexOf("mid") != -1) {
                    URL u = new URL(work.getUrl().replace("//", "https://"));
                    List<MidiUtil.MidiTrack> tracks = MidiUtil.getTracks(u);
                    MidiFile midiFile = new MidiFile();
                    midiFile.setFile(url);
                    midiFile.setMd5(StringUtil.getMD5(url));
                    midiFile.setMachineNum(tracks.size());
                    midiFileRepository.save(midiFile);
                    work.setMachineNum(tracks.size());
                    workRepository.save(work);
                    logger.info("success", work.getUrl(), work.getMachineNum());
                } else {
                    logger.warn("not valid midi url", work.getUrl());
                }

            } catch (Exception e) {
                logger.error("recountMachineNum fail", work.getId() , e);
            }

        });

        return new MusixiseResponse<>(ExceptionMsg.SUCCESS);
    }

}
