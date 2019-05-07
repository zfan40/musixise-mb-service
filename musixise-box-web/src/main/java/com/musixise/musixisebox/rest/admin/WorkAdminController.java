package com.musixise.musixisebox.rest.admin;

import com.musixise.musixisebox.api.admin.service.WorkAdminApi;
import com.musixise.musixisebox.api.admin.vo.common.AddWorkVO;
import com.musixise.musixisebox.api.enums.CategoryEnum;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.domain.MidiFile;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.repository.MidiFileRepository;
import com.musixise.musixisebox.server.repository.WorkRepository;
import com.musixise.musixisebox.server.transfter.WorkTransfter;
import com.musixise.musixisebox.server.utils.CommonUtil;
import com.musixise.musixisebox.server.utils.EventUtil;
import com.musixise.musixisebox.server.utils.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/5/12.
 */
@RestController
@RequestMapping("/api/v1/admin/works")
public class WorkAdminController implements WorkAdminApi {

    @Resource WorkRepository workRepository;

    @Resource MidiFileRepository midiFileRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    @Override
    public MusixisePageResponse<List<WorkVO>> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<Work> all = workRepository.findAll(pageable);
        return new MusixisePageResponse<>(WorkTransfter.getWorkVOs(all.getContent()), all.getTotalElements(), size, page);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @AppMethod(isAdmin = true)
    @Override
    public MusixiseResponse update(@Valid @RequestBody WorkVO workVO) {

        //母情节活动
        if (EventUtil.isMotherEvent(workVO.getContent())) {
            workVO.setCategory(CategoryEnum.MOTHER_EVENT.getVale());
        }

        workRepository.save(WorkTransfter.getWork(workVO));
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @AppMethod(isAdmin = true)
    @Override
    public MusixiseResponse create(@Valid @RequestBody AddWorkVO addWorkVO) {
        Work work = new Work();
        CommonUtil.copyPropertiesIgnoreNull(addWorkVO, work);

        //get machine num
        Optional<MidiFile> midiFile = midiFileRepository.findByMd5(StringUtil.getMD5(work.getUrl()));
        Integer MatchineNum = midiFile.map(MidiFile::getMachineNum).orElse(0);
        work.setMachineNum(MatchineNum);

        //母情节活动
        if (EventUtil.isMotherEvent(addWorkVO.getContent())) {
            work.setCategory(CategoryEnum.MOTHER_EVENT.getVale());
        }

        workRepository.save(work);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    @Override
    public MusixiseResponse<WorkVO> get(@PathVariable Long id) {

        return workRepository.findById(id).map(work -> {
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, WorkTransfter.getWorkVO(work));
        }).orElse(new MusixiseResponse<>(ExceptionMsg.FAILED));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @AppMethod(isAdmin = true)
    @Override
    public MusixiseResponse deleteById(@PathVariable Long id) {
        workRepository.deleteById(id);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }
}
