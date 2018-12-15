package com.musixise.musixisebox.shop.enums

enum class OrderEnum(val status: Long) {

    UNPAY(0L),
    PEDDING_PAY(1L),
    WAIT_DELIVER_GOODS(2L),
    WAIT_RECEIVING_GOODS(3L),
    DONE(4L)

}