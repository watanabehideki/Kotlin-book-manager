import com.book.manager.domain.model.BookWithRental
import com.book.manager.domain.model.Rental
import java.awt.geom.Line2D
import java.time.LocalDate
import java.time.LocalDateTime

data class GetBookListResponse(val bookList: List<BookInfo>)

data class BookInfo(
    val id: Long,
    val title: String,
    val author: String,
    val isRental: Boolean
) {
    constructor(model: BookWithRental) : this(model.book.id, model.book.title, model.book.author, model.isRental)
}

data class GetBookDetailResponse(
    val id: Long,
    val title: String,
    val author: String,
    val releaseDate: LocalDate,
    val rentalInfo: RentalInfo?
) {
    constructor(model: BookWithRental) : this(
        model.book.id,
        model.book.title,
        model.book.author,
        model.book.releaseDate,
        model.rental?.let { RentalInfo(model.rental) }
    )
}

data class RentalInfo(
    val  userId: Long,
    val rentalDate: LocalDateTime,
    val returnDeadLine: LocalDateTime
) {
    constructor(rental: Rental) : this(rental.userId, rental.rentalDatetime, rental.returnDeadline)
}

data class RegistarBookRequest(
    val id: Long,
    val title: String,
    val author: String,
    val releaseDate: LocalDate
)

data class UpdateBookRequest(
    val id: Long,
    val title: String?,
    val author: String?,
    val releaseDate: LocalDate?
)