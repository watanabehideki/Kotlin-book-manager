package com.book.manager.application.service

import com.book.manager.domain.model.Rental
import com.book.manager.domain.repository.BookRepository
import com.book.manager.domain.repository.RentalRepository
import com.book.manager.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

//貸出期間
private const val RENTAL_TERM_DAYS = 14L //const val = 定数名 （コンパイル時定数）

@Service
class RentalService(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val rentalRepository: RentalRepository
) {
    //貸出機能
    @Transactional
    fun startRental(bookId: Long, userId: Long) {
        //ユーザー認証チェック
        userRepository.find(userId) ?: throw IllegalArgumentException("該当するユーザーが存在しません　userId: ${userId}")

        //書籍の存在チェック
        val book = bookRepository.findWithRental(bookId) ?: throw IllegalArgumentException("該当する書籍が存在しません bookId: ${bookId}")

        //貸出中チェック
        if (book.isRental) throw IllegalArgumentException("貸出中の書籍です bookId: ${bookId}")

        val rentalDatetime = LocalDateTime.now()
        val returnDeadline = rentalDatetime.plusDays(RENTAL_TERM_DAYS)
        val rental = Rental(bookId, userId, rentalDatetime, returnDeadline)

        rentalRepository.startRental(rental)
    }

    //返却機能
    @Transactional
    fun endRental(bookId: Long, userId: Long) {

        //ユーザー認証チェック
        userRepository.find(userId) ?: throw  IllegalArgumentException("該当するユーザーが存在しません　userId: ${userId}")

        //書籍の存在チェック
        val book = bookRepository.findWithRental(bookId) ?: throw IllegalArgumentException("該当する書籍が存在しません bookId: ${bookId}")

        //貸出中のチェック
        if (!book.isRental) throw IllegalArgumentException("未貸出の書籍です　bookId: ${bookId}")

        //貸出中のユーザーと一致するかのチェック
        if (book.rental!!.userId != userId) throw IllegalArgumentException("他のユーザーへ貸出中の書籍です bookId: ${bookId}")

        rentalRepository.endRental(bookId)
    }

}