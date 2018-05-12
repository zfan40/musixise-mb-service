package com.musixise.musixisebox.rest.admin;

import com.musixise.musixisebox.api.admin.service.MusixiserApi;
import com.musixise.musixisebox.api.admin.vo.common.MusixiserVO;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
import com.musixise.musixisebox.server.transfter.MusixiserTransfter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@RequestMapping("/api/v1/admin/musixisers")
public class MusixiserAdminController implements MusixiserApi {

    @Resource MusixiserRepository musixiserRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    @Override
    public MusixisePageResponse<List<MusixiserVO>> getMusixisers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<Musixiser> all = musixiserRepository.findAll(pageable);
        return new MusixisePageResponse<>(MusixiserTransfter.getMusixiserVOS(all.getContent()), all.getTotalElements(), size, page);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @AppMethod(isAdmin = true)
    @Override
    public MusixiseResponse updateMusixser(@Valid @RequestBody MusixiserVO musixiser) {
        musixiserRepository.save(MusixiserTransfter.getMusixiser(musixiser));
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    @Override
    public MusixiseResponse<MusixiserVO> getMusixiser(@PathVariable Long id) {

        return musixiserRepository.findById(id).map(musixiser -> {
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, MusixiserTransfter.getMusixiserVO(musixiser));
        }).orElse(new MusixiseResponse<>(ExceptionMsg.FAILED));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @AppMethod(isAdmin = true)
    @Override
    public MusixiseResponse delMusixiser(@PathVariable Long id) {
        musixiserRepository.deleteById(id);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }


}
