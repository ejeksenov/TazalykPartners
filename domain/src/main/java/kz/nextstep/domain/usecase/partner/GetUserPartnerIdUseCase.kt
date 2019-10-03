package kz.nextstep.domain.usecase.partner

import kz.nextstep.domain.repository.UserPartnerRepository

class GetUserPartnerIdUseCase(val userPartnerRepository: UserPartnerRepository) {
    fun execute(): String {
        return userPartnerRepository.getCurrentUserPartnerId()
    }
}