import {Playlist, Song} from "../../lib/Models";

export default function PlaylistPlayer({ playlist }: {playlist: Playlist<Song>}) {
    const tracks = playlist.songs.map((song, index) => {
        return {
            id: index,
            sources: {
                mp3: `http://localhost:8080/song/${song.id}/file`,
            },
            title: song.detail?.title ?? 'unknown',
            artist: song.detail?.artist ?? 'unknown',
        }
    })

    return (<></>)
}