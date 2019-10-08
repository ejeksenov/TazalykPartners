package kz.nextstep.domain.usecase.partner

import kz.nextstep.domain.repository.UserPartnerRepository

class SignOutUseCase(val userPartnerRepository: UserPartnerRepository) {
    fun execute(): Boolean {
        return userPartnerRepository.signOut()
    }
}