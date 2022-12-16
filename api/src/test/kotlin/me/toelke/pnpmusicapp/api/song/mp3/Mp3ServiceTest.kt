package me.toelke.pnpmusicapp.api.song.mp3

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.toelke.pnpmusicapp.api.NoMp3Exception
import me.toelke.pnpmusicapp.api.song.file.FileManager
import org.junit.jupiter.api.Test
import kotlin.io.path.toPath

internal class Mp3ServiceTest {
    private val manager = mockk<FileManager>()
    private val service = Mp3Service(manager)

    @Test
    fun `parseMp3() should parse mp3 file`() {
        runBlocking {
            val path = this.javaClass.classLoader.getResource("mp3/tagged.mp3")!!.toURI().toPath()
            coEvery { manager.getPath("abc") } returns path

            val details = service.parseMp3("abc")

            details shouldNotBe null
            details.run {
                album shouldBe "YouTube Audio Library"
                artist shouldBe "Kevin MacLeod"
                title shouldBe "Impact Moderato"
                seconds shouldBe 27
            }
        }
    }

    @Test
    fun `parseMp3() should fail on no mp3 file`() {
        runBlocking {
            val path = this.javaClass.classLoader.getResource("mp3/no.mp3")!!.toURI().toPath()
            coEvery { manager.getPath("abc") } returns path

            shouldThrow<NoMp3Exception> {
                service.parseMp3("abc")
            }
        }
    }
}