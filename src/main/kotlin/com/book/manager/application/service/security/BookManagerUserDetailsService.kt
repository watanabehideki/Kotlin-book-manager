package com.book.manager.application.service.security


import com.book.manager.application.service.AuthenticationService
import com.book.manager.domain.enum.RoleType
import com.book.manager.domain.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

//以下認証処理
class BookManagerUserDetailsService(
    private val authenticationService: AuthenticationService
) : UserDetailsService {
    //　ユーザー名（email）からユーザー情報を取得
    override fun loadUserByUsername(username: String): UserDetails? {
        val user = authenticationService.findUser(username)
        return user?.let { BookManagerUserDetails(user) }
    }
}

//ログイン時に入力された値からユーザーデータを取得、格納し、認証処理で使用するデータクラス
data class BookManagerUserDetails(
    val id: Long,
    val email: String,
    val pass: String,
    val roleType: RoleType
) : UserDetails {
    constructor(user: User) : this(user.id, user.email, user.password, user.roleType)

    //権限を取得する関数
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return AuthorityUtils.createAuthorityList(this.roleType.toString())
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return this.email
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return this.pass
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}