package com.musixise.musixisebox.shop.rest.web

import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.exception.MusixiseException
import com.musixise.musixisebox.api.result.MusixisePageResponse
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.server.aop.AppMethod
import com.musixise.musixisebox.server.aop.MusixiseContext
import com.musixise.musixisebox.server.utils.CommonUtil
import com.musixise.musixisebox.shop.domain.Address
import com.musixise.musixisebox.shop.domain.QAddress
import com.musixise.musixisebox.shop.repository.AddressRepository
import com.musixise.musixisebox.shop.rest.web.vo.req.AddressVO
import com.musixise.musixisebox.shop.rest.web.vo.resp.AddressRespVO
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/shop/address")
class AddressController {

    @Resource
    private lateinit var addressRepository: AddressRepository

    @PostMapping("/create")
    @AppMethod(isLogin = true)
    fun create(@Valid @RequestBody addressVO: AddressVO) : MusixiseResponse<Long> {

        val address = Address(userId = MusixiseContext.getCurrentUid())
        CommonUtil.copyPropertiesIgnoreNull(addressVO, address)
        val save = addressRepository.save(address)
        return MusixiseResponse<Long>(ExceptionMsg.SUCCESS, save.id)
    }

    @GetMapping("/get/{id}")
    @AppMethod(isLogin = true)
    fun getOne(@PathVariable id: Long) : MusixiseResponse<AddressRespVO> {
        val findById = addressRepository.findById(id);
        return findById.map {

            val addressRespVO = AddressRespVO()
            CommonUtil.copyPropertiesIgnoreNull(it, addressRespVO)
            MusixiseResponse<AddressRespVO>(ExceptionMsg.SUCCESS, addressRespVO)

        } .orElseThrow {
            throw MusixiseException("地址不存在");
        }
    }

    @GetMapping("/getList")
    @AppMethod(isLogin = true)
    fun getAll() : MusixisePageResponse<List<Address>> {

        val address = QAddress.address
        val builder = BooleanBuilder()
        builder.and(address.userId.eq(MusixiseContext.getCurrentUid()))

        val sort = Sort(Sort.Order(Sort.Direction.DESC, "id"))
        val pageable = PageRequest.of(0, 100, sort)

        val addressAll = addressRepository.findAll(builder, pageable)
        return MusixisePageResponse(addressAll.content, addressAll.totalElements, 100, 1)
    }
}