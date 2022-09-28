package me.toelke.pnpmusicapp.api.song

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.equality.shouldNotBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import me.toelke.pnpmusicapp.api.NotFoundException
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.uuid
import org.junit.jupiter.api.Test
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.test.StepVerifier

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

        val actual = controller.get(song.id)

        StepVerifier
            .create(actual)
            .expectError(NotFoundException::class.java)
            .verify()
    }

    @Test
    fun `should get many songs`() {
        val song2 = song.copy(id = uuid())
        val result = PageableResult(listOf(song, song2).toFlux(), Mono.just(2))
        every { service.getAll(Pageable.unpaged(), SearchFilter(emptyMap())) } returns result

        val actual = controller.getAll(Pageable.unpaged(), SearchFilter(emptyMap()))
        val response = actual.block()

        response shouldNotBe null
        response?.headers?.get(SongController.XTotalCount)!!.first() shouldBe "2"
        val notNull = response.body!!.collectList().block()
        notNull!!.shouldContain(song)
        notNull.shouldContain(song2)
    }

    @Test
    fun `should get no songs`() {
        val result = PageableResult(Flux.empty<Song>(), Mono.just(0))
        every { service.getAll(Pageable.unpaged(), SearchFilter(emptyMap())) } returns result

        val actual = controller.getAll(Pageable.unpaged(), SearchFilter(emptyMap()))
        val response = actual.block()

        response shouldNotBe null
        response?.headers?.get(SongController.XTotalCount)!!.first() shouldBe "0"
        val notNull = response.body!!.collectList().block()
        notNull!!.isEmpty() shouldBe true
    }

    @Test
    fun `should delete song`() {
        every { service.delete(uuid) } returns Mono.empty()

        controller.delete(id = uuid).block()
    }

    @Test
    fun `should create file`() {
        val filePart = Mono.just(mockk<FilePart>())
        every { service.createFile(id = uuid, filePart) } returns Mono.empty()

        val actual = controller.createFile(uuid, filePart)
        StepVerifier
            .create(actual)
            .expectComplete()
            .verify()
    }

    @Test
    fun `should fail on create file`() {
        val filePart = Mono.just(mockk<FilePart>())
        every { service.createFile(id = uuid, filePart) } returns Mono.error(NotFoundException("song"))

        val actual = controller.createFile(uuid, filePart)
        StepVerifier
            .create(actual)
            .expectError(NotFoundException::class.java)
            .verify()
    }

    @Test
    fun `should get file`() {
        val mock = mockk<DataBuffer>()
        val dataBuffer = Flux.just(mock)
        every { service.getFile(id = uuid) } returns dataBuffer

        val actual = controller.getFile(id = uuid)
        StepVerifier
            .create(actual)
            .expectNext(mock)
            .verifyComplete()
    }
}
