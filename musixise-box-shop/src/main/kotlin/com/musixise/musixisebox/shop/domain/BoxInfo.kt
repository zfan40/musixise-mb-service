package com.musixise.musixisebox.shop.domain

data class BoxInfo(val wid: Long=0,
                   val product: Product?=null,
                   val title: String?=null,
                   val userId: Long=0,
                   val url: String?=null)


data class MusixDownloadInfo(val wid: Long=0,
                             val product: Product?=null,
                             val title: String?=null,
                             val userId: Long=0,
                             val downloadUrl: String?=null)