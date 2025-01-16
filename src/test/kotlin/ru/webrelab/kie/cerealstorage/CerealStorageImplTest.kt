package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    companion object {
        private const val FLOAT_DELTA = 0.01f
    }

    //private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f, 5f)
        }
    }

    @Test
    fun `should throw if amount in addCereal is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            val storage = CerealStorageImpl(5f, 10f)
            storage.addCereal(Cereal.PEAS, -2f)
        }
    }

    @Test
    fun `should throw if storage is full containers`() {
        assertThrows(IllegalStateException::class.java) {
            val storage = CerealStorageImpl(5f, 10f)
            storage.addCereal(Cereal.PEAS, 5f)
            storage.addCereal(Cereal.MILLET, 4f)
            storage.addCereal(Cereal.BULGUR, 3f)
        }
    }

    @Test
    fun `add enough cereal`() {
        val storage = CerealStorageImpl(5f, 10f)
        assertEquals(0.0f, storage.addCereal(Cereal.PEAS, 4f), FLOAT_DELTA, "Должно остаться доступным еще 1f")
        assertEquals(0.0f, storage.addCereal(Cereal.PEAS, 1f), FLOAT_DELTA, "Заполнен полностью")
    }

    @Test
    fun `add excess cereal`() {
        val storage = CerealStorageImpl(5f, 10f)
        println(storage.toString())
        assertEquals(0.0f, storage.addCereal(Cereal.PEAS, 4f), FLOAT_DELTA, "Должно остаться еще 1f")
        assertEquals(2.0f, storage.addCereal(Cereal.PEAS, 3f), FLOAT_DELTA, "Заполнен полностью, не смогли положить 2f")
    }


    @Test
    fun `should throw if amount in getCereal is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            val storage = CerealStorageImpl(5f, 10f)
            storage.addCereal(Cereal.PEAS, 2f)
            storage.getCereal(Cereal.PEAS, -2f)
        }
    }

    @Test
    fun `get cereal test`() {
        val storage = CerealStorageImpl(5f, 10f)
        assertEquals(0.0f, storage.getCereal(Cereal.PEAS, 4f), FLOAT_DELTA, "В хранилище пусто")
        storage.addCereal(Cereal.PEAS, 4f)
        assertEquals(3.0f, storage.getCereal(Cereal.PEAS, 3f), FLOAT_DELTA, "Забрали 3f")
        assertEquals(1.0f, storage.getCereal(Cereal.PEAS, 2f), FLOAT_DELTA, "Забрали все что было")
    }

    @Test
    fun `remove empty container`() {
        val storage = CerealStorageImpl(5f, 10f)
        storage.addCereal(Cereal.PEAS, 4f)
        storage.getCereal(Cereal.PEAS, 4f)
        assertEquals(true, storage.removeContainer(Cereal.PEAS), "Уничтожили пустой контейнер, должно вернуть true")
    }

    @Test
    fun `remove not empty container`() {
        val storage = CerealStorageImpl(5f, 10f)
        storage.addCereal(Cereal.PEAS, 4f)
        assertEquals(false, storage.removeContainer(Cereal.PEAS), "Контейнер не пуст, его нельзя удалить")
    }
}