package kz.nextstep.data.mapper

import kz.nextstep.data.entity.HistoryPinEntity
import kz.nextstep.domain.model.HistoryPin
import kz.nextstep.domain.utils.ChangeDateFormat

object HistoryPinMapper {
    fun map(historyPinEntity: HistoryPinEntity) = HistoryPin(
        pinId = historyPinEntity.pinId,
        userId = historyPinEntity.userId,
        time = ChangeDateFormat.onChangeDateFormat(historyPinEntity.time),
        total = historyPinEntity.total
    )
}