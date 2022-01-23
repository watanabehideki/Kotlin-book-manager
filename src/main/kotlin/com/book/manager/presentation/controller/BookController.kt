package com.book.manager.presentation.controller

import BookInfo
import GetBookDetailResponse
import GetBookListResponse
import com.book.manager.application.service.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("book") //ルートパス
@CrossOrigin
class BookController(
    private val bookService: BookService
) {
    @GetMapping("/list")
    fun getList(): GetBookListResponse {
        val bookList = bookService.getList().map {
            BookInfo(it)
        }
        return GetBookListResponse(bookList)
    }

    @GetMapping("/detail/{book_id}")
    fun getDetail(@PathVariable("book_id") bookId: Long): GetBookDetailResponse {
        val book = bookService.getDetail(bookId)
        return  GetBookDetailResponse(book)
    }
}