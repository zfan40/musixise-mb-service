package com.musixise.musixisebox.shop.enums

enum class OrderEnum(val status: Long) {

    /**
     * 未支付
     */
    UNPAY(0L),
    /**
     * 支付中
     */
    PEDDING_PAY(1L),
    /**
     * 等待发货
     */
    WAIT_DELIVER_GOODS(2L),
    /**
     * 等待收货
     */
    WAIT_RECEIVING_GOODS(3L),
    /**
     * 交易完成
     */
    DONE(4L)



}