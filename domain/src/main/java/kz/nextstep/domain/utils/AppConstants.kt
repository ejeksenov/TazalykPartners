package kz.nextstep.domain.utils

import kz.nextstep.domain.model.Pin

object AppConstants{
    const val pinTree = "Pin_new"
    const val userTree = "Users_new"
    const val requestsTree = "Requests"
    const val userPartnerTree = "UserPartner"
    const val emptyParam = ""

    const val SUCCESS_PIN_DIRECTOR = "success_pin_director"
    const val SUCCESS_PIN_ADMIN = "success_pin_admin"
    const val SUCCESS_PRODUCT_SPONSOR = "success_product_sponsor"

    const val ERROR_USER_NOT_FOUND = "Пользователь не зарегистрирован"
    const val ERROR_INVALID_PASSWORD = "Неверный пароль. Попробуйте заново"

    const val SUCCESS_MESSAGE_SENT = "Сообщение успешно отправлено. Пожалуйста, проверьте почту"
    const val ERROR_MESSAGE_NOT_SENT = "Не удалось отправить сообщение!"
}