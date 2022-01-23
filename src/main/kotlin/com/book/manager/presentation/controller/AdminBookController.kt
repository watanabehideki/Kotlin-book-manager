package com.book.manager.presentation.controller

import RegistarBookRequest
import UpdateBookRequest
import com.book.manager.application.service.AdminBookService
import com.book.manager.domain.model.Book
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("admin/book")
@CrossOrigin
class AdminBookController(
    private val adminBookService: AdminBookService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegistarBookRequest) {
        adminBookService.register(
            Book(
                request.id,
                request.title,
                request.author,
                request.releaseDate
            )
        )
    }

    @PutMapping("/update")
    fun update(@RequestBody request: UpdateBookRequest) {
        adminBookService.update(request.id, request.title, request.author, request.releaseDate)
    }
}