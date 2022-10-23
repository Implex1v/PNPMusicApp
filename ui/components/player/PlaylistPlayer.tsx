import {Playlist, Song} from "../../lib/Models";
import SongListItem from "./SongListItem";
import {useRef, useState} from "react";

export default function PlaylistPlayer({ playlist }: {playlist: Playlist<Song>}) {
    const [currentSong, setCurrentSong] = useState<Song>(playlist.songs[0])
    const player = useRef<HTMLAudioElement>()

    const tracks = playlist.songs.map((song, index) => {
        return <SongListItem key={index} song={song} playing={currentSong == song} onPlay={(song) => onPlay(song)}></SongListItem>
    })

    const sources = playlist.songs.map((song, index) => {
        return <source src={`http://localhost:8080/song/${song.id}/file`} key={index} />
    })

    function onPlay(song: Song) {
        setCurrentSong(song)
        console.log(player.current)
        player.current
            .load()
        player.current
            .play()
            .then(() => {})

        player.current.onended
    }

    return (
        <div className="row">
            <audio id="player" src={`http://localhost:8080/song/${currentSong.id}/file`} controls={true} className="mb-4" ref={player} loop={true}>
            </audio>
            {tracks}
        </div>
    )
}