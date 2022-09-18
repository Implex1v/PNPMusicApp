package me.toelke.pnpmusicapp.api.song

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import me.toelke.pnpmusicapp.api.song.file.FileManager
import me.toelke.pnpmusicapp.api.song.mp3.Mp3Service
import me.toelke.pnpmusicapp.api.uuid
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@Suppress("ReactiveStreamsUnusedPublisher")
internal class SongServiceTest {
    private val uuid = uuid()
    private val repository = mockk<SongRepository>()
    private val manager = mockk<FileManager>()
    private val mp3Service = mockk<Mp3Service>()
    private val service = SongService(songRepository = repository, fileManager = manager, mp3Service = mp3Service)
    private val song = Song(id = uuid, name = "foo", tags = listOf("foo", "bar"))

    @Test
    fun `should create song`() {
        every { repository.insert(any<Song>()) } returns Mono.just(song.copy(id = uuid()))

        val actual = service.create(song).block()

        actual shouldNotBe null
        actual?.id shouldNotBe song.id
    }

    @Test
    fun `should get song`() {
        every { repository.findById(uuid) } returns Mono.just(song)

        val actual = service.get(song.id).block()

        actual shouldBe song
    }

    @Test
    fun `should get no song`() {
        every { repository.findById(uuid) } returns Mono.empty()

        val actual = service.get(song.id).block()

        actual shouldBe null
    }

    @Test
    fun `should get many songs`() {
        val song2 = song.copy(id = uuid())
        every { repository.findAll() } returns listOf(song, song2).toFlux()

        val actual = service.getAll().collectList().block()

        actual shouldNotBe null
        val notNull = actual!!
        notNull.shouldContain(song)
        notNull.shouldContain(song2)
    }

    @Test
    fun `should get no songs`() {
        every { repository.findAll() } returns emptyList<Song>().toFlux()

        val actual = service.getAll().collectList().block()

        actual shouldNotBe null
        actual.shouldBeEmpty()
    }

    @Test
    fun `should delete song`() {
        every { repository.deleteById(uuid) } returns Mono.empty()

        service.delete(id = uuid).block()
    }
}