package me.toelke.pnpmusicapp.api.song.mp3

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import me.toelke.pnpmusicapp.api.NoMp3Exception
import me.toelke.pnpmusicapp.api.song.file.FileManager
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.expectError
import reactor.test.StepVerifier
import kotlin.io.path.toPath

internal class Mp3ServiceTest {
    private val manager = mockk<FileManager>()
    private val service = Mp3Service(manager)

    @Test
    fun `parseMp3() should parse mp3 file`() {
        val path = this.javaClass.classLoader.getResource("mp3/tagged.mp3")!!.toURI().toPath()
        every { manager.getPath("abc") } returns path

        val details = service.parseMp3("abc").block()

        details shouldNotBe null
        details?.also {
            it.album shouldBe "YouTube Audio Library"
            it.artist shouldBe "Kevin MacLeod"
            it.title shouldBe "Impact Moderato"
            it.seconds shouldBe 27
        }
    }

    @Test
    fun `parseMp3() should fail on no mp3 file`() {
        val path = this.javaClass.classLoader.getResource("mp3/no.mp3")!!.toURI().toPath()
        every { manager.getPath("abc") } returns path

        val mono = service.parseMp3("abc")
        StepVerifier.create(mono)
            .expectError(NoMp3Exception::class)
            .verify()
    }
}