package com.musixise.musixisebox.rest.web;

import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.service.WorkService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
public class HomePageController {

    @Resource WorkService workService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public MusixisePageResponse<List<WorkVO>> getRecommends(  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Page<Work> recommends = workService.getRecommends(page, size);
        List<WorkVO> workList = workService.getWorkList(recommends.getContent());
        return new MusixisePageResponse<>(workList, recommends.getTotalElements(), size, page);
    }

}
