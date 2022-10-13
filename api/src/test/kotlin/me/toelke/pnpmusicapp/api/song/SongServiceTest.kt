package me.toelke.pnpmusicapp.api.song

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import me.toelke.pnpmusicapp.api.NotFoundException
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.song.file.FileManager
import me.toelke.pnpmusicapp.api.song.mp3.Mp3Service
import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.uuid
import org.junit.jupiter.api.Test
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.test.expectError
import reactor.test.StepVerifier

@Suppress("ReactiveStreamsUnusedPublisher")
internal class SongServiceTest {
    private val uuid = uuid()
    private val repository = mockk<SongRepository>()
    private val manager = mockk<FileManager>()
    private val mp3Service = mockk<Mp3Service>()
    private val service = SongService(songRepository = repository, fileManager = manager, mp3Service = mp3Service)
    private val song = Song(id = uuid, name = "foo", tags = listOf("foo", "bar"))
    private val detail = SongDetail("foo", "bar", 42, "baz")

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
        val result = PageableResult(listOf(song, song2).toFlux(), Mono.just(2))
        every { repository.find(Pageable.unpaged(), SearchFilter(emptyMap())) } returns Flux.just(result)

        val actual = service.getAll(Pageable.unpaged(), SearchFilter(emptyMap())).blockFirst()!!

        actual shouldNotBe null
        actual.total.block() shouldBe 2
        val items = actual.items.collectList().block()!!
        items shouldContain song
        items shouldContain song2
    }

    @Test
    fun `should get no songs`() {
        val result = PageableResult(Flux.empty<Song>(), Mono.just(0))
        every { repository.find(Pageable.unpaged(), SearchFilter(emptyMap())) } returns Flux.just(result)

        val actual = service.getAll(Pageable.unpaged(), SearchFilter(emptyMap())).blockFirst()!!

        actual.total.block() shouldBe 0
        actual.items.collectList().block()!!.isEmpty() shouldBe true
    }

    @Test
    fun `should delete song`() {
        every { repository.deleteById(uuid) } returns Mono.empty()

        service.delete(id = uuid).block()
    }

    @Test
    fun `should get file`() {
        val fileName = "abc"
        val dataFlux = mockk<DataBuffer>()
        val flux = Flux.just(dataFlux)
        every { manager.readFile("$fileName.mp3") } returns flux

        val actual = service.getFile(fileName)

        StepVerifier
            .create(actual)
            .expectNext(dataFlux)
            .verifyComplete()
    }

    @Test
    fun `should error on file`() {
        val fileName = "abc"
        every { manager.readFile("$fileName.mp3") } returns Flux.error(NotFoundException("file"))

        val actual = service.getFile(fileName)

        StepVerifier
            .create(actual)
            .expectError(NotFoundException::class)
            .verify()
    }

    @Test
    fun `should create file`() {
        val filePart = mockk<FilePart>()
        val filePartMono = Mono.just(filePart)
        every { manager.writeFile("$uuid.mp3", filePartMono) } returns Mono.empty()
        every { repository.findById(uuid) } returns Mono.just(song)
        every { mp3Service.parseMp3("$uuid.mp3") } returns Mono.just(detail)
        every { repository.save(song.copy(detail = detail)) } returns Mono.empty()

        val actual = service.createFile(id = uuid, filePartMono)

        StepVerifier
            .create(actual)
            .verifyComplete()
    }

    @Test
    fun `should fail on song not found`() {
        val filePart = mockk<FilePart>()
        val filePartMono = Mono.just(filePart)
        val fileName = "$uuid.mp3"
        every { manager.writeFile(fileName, filePartMono) } returns Mono.empty()
        every { repository.findById(uuid) } returns Mono.empty()
        every { mp3Service.parseMp3(fileName) } returns Mono.just(detail)
        every { manager.deleteFile(fileName) } returns Mono.empty()

        val actual = service.createFile(id = uuid, filePartMono)

        StepVerifier
            .create(actual)
            .expectError(NotFoundException::class)
            .verify()
    }
}