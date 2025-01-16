package ru.webrelab.kie.cerealstorage

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

        check (maxCountContainers > storage.size) {
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
        TODO("Not yet implemented")
    }

    override fun getAmount(cereal: Cereal): Float {
        TODO("Not yet implemented")
    }

    override fun getSpace(cereal: Cereal): Float {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return storage.toString()
    }
}
