package kz.nextstep.domain.usecase.partner

import kz.nextstep.domain.repository.UserPartnerRepository

class GetCurrentUserPartnerUseCase(private val userPartnerRepository: UserPartnerRepository) {
    fun execute(): Boolean {
        return userPartnerRepository.getCurrentUserPartner()
    }
}