package me.toelke.pnpmusicapp.api.song.mp3

import com.mpatric.mp3agic.Mp3File
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class Mp3HelperKtTest {
    @Test
    fun `artist() should return v2Tag artist`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns true
        every { mp3.id3v2Tag.artist } returns "foo"

        mp3.artist() shouldBe "foo"
    }

    @Test
    fun `artist() should return v1Tag artist`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns false
        every { mp3.hasId3v1Tag() } returns true
        every { mp3.id3v1Tag.artist } returns "foo"

        mp3.artist() shouldBe "foo"
    }

    @Test
    fun `artist() should return unknown on no tags`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns false
        every { mp3.hasId3v1Tag() } returns false

        mp3.artist() shouldBe "unknown"
    }

    @Test
    fun `title() should return v2Tag artist`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns true
        every { mp3.id3v2Tag.title } returns "foo"

        mp3.title() shouldBe "foo"
    }

    @Test
    fun `title() should return v1Tag artist`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns false
        every { mp3.hasId3v1Tag() } returns true
        every { mp3.id3v1Tag.title } returns "foo"

        mp3.title() shouldBe "foo"
    }

    @Test
    fun `title() should return unknown on no tags`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns false
        every { mp3.hasId3v1Tag() } returns false

        mp3.title() shouldBe "unknown"
    }

    @Test
    fun `album() should return v2Tag artist`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns true
        every { mp3.id3v2Tag.album } returns "foo"

        mp3.album() shouldBe "foo"
    }

    @Test
    fun `album() should return v1Tag artist`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns false
        every { mp3.hasId3v1Tag() } returns true
        every { mp3.id3v1Tag.album } returns "foo"

        mp3.album() shouldBe "foo"
    }

    @Test
    fun `album() should return unknown on no tags`() {
        val mp3 = mockk<Mp3File>()

        every { mp3.hasId3v2Tag() } returns false
        every { mp3.hasId3v1Tag() } returns false

        mp3.album() shouldBe null
    }
}