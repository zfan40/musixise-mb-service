package com.musixise.musixisebox.server.service;

import com.musixise.musixisebox.api.web.vo.resp.wechat.WCPayRequestVO;

import javax.servlet.http.HttpServletRequest;

public interface PayService {
    WCPayRequestVO getPayInfo(Long productId) throws Exception;
    String getPrepayId(Long productId) throws Exception;

    String getPayNotify(HttpServletRequest request);
}
