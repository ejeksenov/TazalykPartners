package kz.nextstep.data.mapper

import kz.nextstep.data.entity.UserEntity
import kz.nextstep.domain.model.User

object UserMapper {
    fun map(userEntity: UserEntity) = User(
        userName = userEntity.userName,
        total = userEntity.total,
        bonus = userEntity.bonus,
        imageLink = userEntity.imageLink,
        phoneNumber = userEntity.phoneNumber,
        email = userEntity.email,
        userRole = userEntity.userRole,
        city = userEntity.city,
        bio = userEntity.bio,
        status = userEntity.status,
        website = userEntity.website,
        fbPage = userEntity.fbPage,
        instaPage = userEntity.instaPage,
        prev_total = userEntity.prev_total
    )
}