package ru.webrelab.kie.cerealstorage

import ru.webrelab.kie.cerealstorage.extensions.deltaEquals
import kotlin.math.min

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    private val maxCountContainers = (storageCapacity / containerCapacity).toInt()

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0.0f) {
            "Количество добавляемой крупы не может быть отрицательной"
        }

        if (storage.containsKey(cereal)) {
            return addCerealInternal(cereal, amount)
        }

        check (hasSpaceForNewContainer()) {
            "Хранилище не позволяет разместить ещё один контейнер для новой крупы"
        }

        return addCerealInternal(cereal, amount)
    }

    private fun addCerealInternal(cereal: Cereal, amount: Float): Float {
        val container = storage[cereal] ?: 0.0f
        val allCerealAmount = container + amount


        return if (allCerealAmount <= containerCapacity) {
            storage[cereal] = allCerealAmount
            0.0f
        } else {
            storage[cereal] = containerCapacity
            allCerealAmount - containerCapacity
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0.0f) {
            "Количество вынимаемой крупы не может быть отрицательной"
        }

        if (!storage.containsKey(cereal)) {
            return 0.0f
        }

        val currentCerealWeight = storage[cereal] ?: 0.0f
        val cerealWeight = min(currentCerealWeight, amount)
        storage[cereal] = currentCerealWeight - cerealWeight
        return cerealWeight
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        if (!storage.containsKey(cereal)) return true
        val currentCerealWeight = storage[cereal] ?: 0.0f

        return if (currentCerealWeight.deltaEquals(0.0f)) {
            storage.remove(cereal)
            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: 0.0f
    }

    override fun getSpace(cereal: Cereal): Float {
        if (storage.containsKey(cereal)) {
            return containerCapacity - (storage[cereal] ?: 0.0f)
        }

        return if (hasSpaceForNewContainer()) containerCapacity else 0.0f
    }

    override fun toString(): String {
        if (storage.isEmpty()) return "В хранилище нет контейнеров"
        val sb = StringBuilder()
        sb.append("[Состав хранилища:")

        storage.forEach { (cereal, value) ->
            sb.append(" ${cereal.local} - ${"%.2f".format(value)},")
        }
        sb.replace(sb.length - 1, sb.length, "]")
        return sb.toString()
    }

    private fun hasSpaceForNewContainer() = maxCountContainers > storage.size
}

