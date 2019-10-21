package kz.nextstep.data.mapper

import kz.nextstep.data.entity.MarkingEntity
import kz.nextstep.domain.model.Marking

object MarkingMapper {
    fun map(markingEntity: MarkingEntity) = Marking(
        markingDescription = markingEntity.markingDescription,
        markingImageUrl = markingEntity.markingImageUrl,
        markingLogoUrl = markingEntity.markingLogoUrl,
        markingName = markingEntity.markingName,
        markingNumberId = markingEntity.markingNumberId,
        markingType = markingEntity.markingType,
        shortMarkingDescription = markingEntity.shortMarkingDescription
    )
}