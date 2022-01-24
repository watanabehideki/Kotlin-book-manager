package com.book.manager.presentation.aop

import com.book.manager.application.service.security.BookManagerUserDetails
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.IllegalArgumentException

//Javaのログライブラリ、SLF4Jを使用してロガーを生成
private val logger = LoggerFactory.getLogger(LoggingAdvice::class.java)

@Aspect
@Component
class LoggingAdvice {

    //引数でクラスを指定
    //前処理
    @Before("execution(* com.book.manager.presentation.controller..*.*(..))")
    fun beforeLog(joinPoint: JoinPoint) {
        val user = SecurityContextHolder.getContext().authentication.principal as BookManagerUserDetails
        logger.info("Start: ${joinPoint.signature} userId=${user.id}") //joinPoint.signatureには関数名が入る
        logger.info("Class: ${joinPoint.target.javaClass}")
        logger.info("Session: ${(RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request.session.id}")
    }

    //後処理
    @After("execution(* com.book.manager.presentation.controller..*.*(..))")
    fun afterLog(joinPoint: JoinPoint) {
        val user = SecurityContextHolder.getContext().authentication.principal as BookManagerUserDetails
        logger.info("End: ${joinPoint.signature} userId=${user.id}")
    }

    //前後共通処理の差し込み
    @Around("execution(* com.book.manager.presentation.controller..*.*(..))")
    fun aroundLog(joinPoint: ProceedingJoinPoint): Any? {
        //前処理
        val user = SecurityContextHolder.getContext().authentication.principal as BookManagerUserDetails
        logger.info("Around　Start Proceed: ${joinPoint.signature} userId=${user.id}")

        //本処理
        val result = joinPoint.proceed()

        //後処理
        logger.info("Around End Proceed: ${joinPoint.signature} userId=${user.id}")

        //本処理の結果の返却
        return  result
    }

    //戻り値に応じた後処理
    @AfterReturning("execution(* com.book.manager.presentation.controller..*.*(..))", returning = "returnValue")
    fun afterReturningLog(joinPoint: JoinPoint, returnValue: Any?) {
        logger.info("AfterReturning End: ${joinPoint.signature} returnValue=${returnValue}") //returnValueで戻り値を出力
    }

    //例外に応じた後処理
    @AfterThrowing("execution(* com.book.manager.presentation.controller..*.*(..))", throwing = "e")
    fun afterThrowingLog(joinPoint: JoinPoint, e: Throwable) { //第二引数にIllegalArgumentExceptionを指定することも可
        logger.error("Expection: ${e.javaClass} signature=${joinPoint.signature} message=${e.message}")
    }
}