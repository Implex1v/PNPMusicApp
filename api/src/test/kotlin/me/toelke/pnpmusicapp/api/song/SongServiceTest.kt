package me.toelke.pnpmusicapp.api.song

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.song.file.FileManager
import me.toelke.pnpmusicapp.api.song.mp3.Mp3Service
import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.uuid
import org.junit.jupiter.api.Test
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

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
        runBlocking {
            coEvery { repository.insert(any<Song>()) } returns Mono.just(song.copy(id = uuid()))

            val actual = service.create(song)

            actual shouldNotBe null
            actual.id shouldNotBe song.id
        }
    }

    @Test
    fun `should get song`() {
        runBlocking {
            coEvery { repository.findById(uuid) } returns Mono.just(song)

            val actual = service.get(song.id)

            actual shouldBe song
        }
    }

    @Test
    fun `should get no song`() {
        runBlocking {
            coEvery { repository.findById(uuid) } returns Mono.empty()
            val actual = service.get(song.id)
            actual shouldBe null
        }
    }

    @Test
    fun `should get many songs`() {
        runBlocking {
            val song2 = song.copy(id = uuid())
            val result = PageableResult(listOf(song, song2), 2)
            coEvery { repository.find(Pageable.unpaged(), SearchFilter(emptyMap())) } returns result

            val actual = service.getAll(Pageable.unpaged(), SearchFilter(emptyMap()))

            actual shouldNotBe null
            actual.total shouldBe 2
            val items = actual.items
            items shouldContain song
            items shouldContain song2
        }
    }

    @Test
    fun `should get no songs`() {
        runBlocking {
            val result = PageableResult(emptyList<Song>(), 0)
            coEvery { repository.find(Pageable.unpaged(), SearchFilter(emptyMap())) } returns result

            val actual = service.getAll(Pageable.unpaged(), SearchFilter(emptyMap()))

            actual.total shouldBe 0
            actual.items shouldHaveSize 0
        }
    }

    @Test
    fun `should delete song`() {
        runBlocking {
            coEvery { repository.deleteById(uuid) } returns Mono.empty()
            service.delete(id = uuid)
        }
    }

    @Test
    fun `should get file`() {
        runBlocking {
            val fileName = "abc"
            val dataFlux = mockk<DataBuffer>()
            coEvery { manager.readFile("$fileName.mp3") } returns listOf(dataFlux)

            val actual = service.getFile(fileName)
            actual shouldHaveSize 1
        }
    }

    @Test
    fun `should error on file`() {
        runBlocking {
            val fileName = "abc"
            coEvery { manager.readFile("$fileName.mp3") } throws (ResponseStatusException(HttpStatus.NOT_FOUND))

            shouldThrow<ResponseStatusException> {
                service.getFile(fileName)
            }.also {
                it.status shouldBe HttpStatus.NOT_FOUND
            }
        }
    }

    @Test
    fun `should create file`() {
        runBlocking {
            val filePart = mockk<FilePart>()
            coJustRun { manager.writeFile("$uuid.mp3", filePart) }
            coEvery { repository.findById(uuid) } returns Mono.just(song)
            coEvery { mp3Service.parseMp3("$uuid.mp3") } returns detail
            coEvery { repository.save(song.copy(detail = detail)) } returns Mono.just(song)

            shouldNotThrow<Exception> {
                service.createFile(id = uuid, filePart)
            }
        }
    }

    @Test
    fun `should fail on song not found`() {
        runBlocking {
            val filePart = mockk<FilePart>()
            val fileName = "$uuid.mp3"

            coJustRun { manager.writeFile(fileName, filePart) }
            coJustRun { manager.deleteFile(fileName) }
            coEvery { repository.findById(uuid) } returns Mono.empty()
            coEvery { mp3Service.parseMp3(fileName) } returns detail

            shouldThrow<ResponseStatusException> {
                service.createFile(id = uuid, filePart)
            }.also {
                it.status shouldBe HttpStatus.NOT_FOUND
            }
        }
    }
}