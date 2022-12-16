package me.toelke.pnpmusicapp.api.song

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.toelke.pnpmusicapp.api.Helper
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.uuid
import org.junit.jupiter.api.Test
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@Suppress("ReactiveStreamsUnusedPublisher")
internal class SongControllerTest {
    private val uuid = uuid()
    private val service = mockk<SongService>()
    private val controller = SongController(service)
    private val song = Song(id = uuid, name = "foo", tags = listOf("foo", "bar"))

    @Test
    fun `should create song`() {
        runBlocking {
            coEvery { service.create(any()) } returns song.copy(id = uuid())

            val actual = controller.create(song)

            actual shouldNotBe null
            actual?.id shouldNotBe song.id
        }
    }

    @Test
    fun `should get song`() {
        runBlocking {
            coEvery { service.get(uuid) } returns song

            val actual = controller.get(song.id)

            actual shouldBe song
        }
    }

    @Test
    fun `should get no song`() {
        runBlocking {
            coEvery { service.get(uuid) } returns null

            shouldThrow<ResponseStatusException> {
                controller.get(song.id)
            }.also {
                it.status shouldBe HttpStatus.NOT_FOUND
            }
        }
    }

    @Test
    fun `should get many songs`() {
        runBlocking {
            val song2 = song.copy(id = uuid())
            val result = PageableResult(listOf(song, song2), 2)
            coEvery { service.getAll(Pageable.unpaged(), SearchFilter(emptyMap())) } returns result

            val actual = controller.getAll(Pageable.unpaged(), SearchFilter(emptyMap()))

            actual shouldNotBe null
            actual.headers[Helper.XTotalCount]!!.first() shouldBe "2"
            val notNull = actual.body
            notNull!!.shouldContain(song)
            notNull.shouldContain(song2)
        }
    }

    @Test
    fun `should get no songs`() {
        runBlocking {
            val result = PageableResult(emptyList<Song>(), 0)
            coEvery { service.getAll(Pageable.unpaged(), SearchFilter(emptyMap())) } returns result

            val actual = controller.getAll(Pageable.unpaged(), SearchFilter(emptyMap()))

            actual shouldNotBe null
            actual.headers[Helper.XTotalCount]!!.first() shouldBe "0"
            val body = actual.body
            body!!.isEmpty() shouldBe true
        }
    }

    @Test
    fun `should delete song`() {
        runBlocking {
            coJustRun { service.delete(uuid) }

            controller.delete(id = uuid)
        }
    }

    @Test
    fun `should create file`() {
        runBlocking {
            val filePart = mockk<FilePart>()
            coJustRun { service.createFile(id = uuid, filePart) }
            controller.createFile(uuid, Mono.just(filePart))
        }
    }

    @Test
    fun `should fail on create file`() {
        runBlocking {
            val filePart = mockk<FilePart>()
            coEvery { service.createFile(id = uuid, filePart) } throws ResponseStatusException(HttpStatus.NOT_FOUND)

            shouldThrow<ResponseStatusException> {
                controller.createFile(uuid, Mono.just(filePart))
            }.also {
                it.status shouldBe HttpStatus.NOT_FOUND
            }
        }
    }

    @Test
    fun `should get file`() {
        runBlocking {
            val dataBuffer = listOf(mockk<DataBuffer>())
            coEvery { service.getFile(id = uuid) } returns dataBuffer

            val actual = controller.getFile(id = uuid)
            actual shouldHaveSize 1
            actual.first() shouldBe dataBuffer.first()
        }
    }
}
