package me.toelke.pnpmusicapp.api.song

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import me.toelke.pnpmusicapp.api.uuid
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@Suppress("ReactiveStreamsUnusedPublisher")
internal class SongControllerTest {
    private val uuid = uuid()
    private val service = mockk<SongService>()
    private val controller = SongController(service)
    private val song = Song(id = uuid, name = "foo", tags = listOf("foo", "bar"))

    @Test
    fun `should create song`() {
        every { service.create(any()) } returns Mono.just(song.copy(id = uuid()))

        val actual = controller.create(song).block()

        actual shouldNotBe null
        actual?.id shouldNotBe song.id
    }

    @Test
    fun `should get song`() {
        every { service.get(uuid) } returns Mono.just(song)

        val actual = controller.get(song.id).block()

        actual shouldBe song
    }

    @Test
    fun `should get no song`() {
        every { service.get(uuid) } returns Mono.empty()

        val actual = controller.get(song.id).block()

        actual shouldBe null
    }

    @Test
    fun `should get many songs`() {
        val song2 = song.copy(id = uuid())
        every { service.getAll() } returns listOf(song, song2).toFlux()

        val actual = controller.getAll().collectList().block()

        actual shouldNotBe null
        val notNull = actual!!
        notNull.shouldContain(song)
        notNull.shouldContain(song2)
    }

    @Test
    fun `should get no songs`() {
        every { service.getAll() } returns emptyList<Song>().toFlux()

        val actual = controller.getAll().collectList().block()

        actual shouldNotBe null
        actual.shouldBeEmpty()
    }

    @Test
    fun `should delete song`() {
        every { service.delete(uuid) } returns Mono.empty()

        controller.delete(id = uuid).block()
    }
}