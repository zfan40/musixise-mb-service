package com.musixise.musixisebox.rest.admin;

import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
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

@RequestMapping("/api/v1/admin/")
public class MusixiserController {

    @Resource MusixiserRepository musixiserRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/musixisers", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    public MusixisePageResponse<List<Musixiser>> getMusixisers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<Musixiser> all = musixiserRepository.findAll(pageable);
        return new MusixisePageResponse<>(all.getContent(), all.getTotalElements(), size, page);
    }

    @RequestMapping(value = "/musixisers", method = RequestMethod.PUT)
    @AppMethod(isAdmin = true)
    public MusixiseResponse updateMusixser(@Valid @RequestBody Musixiser musixiser) {
        musixiserRepository.save(musixiser);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/musixisers/{id}", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    public MusixiseResponse<Musixiser> getMusixiser(@PathVariable Long id) {

        return musixiserRepository.findById(id).map(musixiser -> {
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, musixiser);
        }).orElse(new MusixiseResponse(ExceptionMsg.FAILED));
    }

    @RequestMapping(value = "/musixisers/{id}", method = RequestMethod.DELETE)
    @AppMethod(isAdmin = true)
    public MusixiseResponse delMusixiser(@PathVariable Long id) {
        musixiserRepository.deleteById(id);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }


}
