import {Playlist, Song} from "../../lib/Models";
import SongListItem from "./SongListItem";
import {useRef, useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPause, faPlay, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {toHHMMSS} from "../../lib/Helper";

export default function PlaylistPlayer({ playlist }: {playlist: Playlist<Song>}) {
    const [currentSong, setCurrentSong] = useState<Song>(playlist.songs[0])
    const [isLoop, setLoop] = useState<boolean>(true)
    const [isPlaying, setPlaying] = useState<boolean>(false)
    const [currentTime, setCurrentTime] = useState<number>(0)
    const [shouldPlay, setShouldPlay] = useState<boolean>(false)
    const player = useRef<HTMLAudioElement>()
    const percentPlayed = Math.ceil((currentTime / player.current?.duration ?? 1) * 100)

    const tracks = playlist.songs.map((song, index) => {
        return <SongListItem key={index} song={song} playing={currentSong == song} onPlay={(song) => onPlaylistItemPlay(song)}></SongListItem>
    })

    function onPlaylistItemPlay(song: Song) {
        if(song.id == currentSong.id) {
            return
        }

        setCurrentSong(song)
        console.log(player.current)

        player.current
            .load()

        setShouldPlay(true)
    }

    function onPlay() {
        const playing = !isPlaying
        setPlaying(playing)

        if(playing) {
            player.current.play().then()
        } else {
            player.current.pause()
        }
    }

    function onLoop() {
        setLoop(!isLoop)
    }

    function onProgress() {
        setCurrentTime(Math.floor(player.current.currentTime))
    }

    function onLoad() {
        if(!shouldPlay) {
            return
        }

        setShouldPlay(false)
        player.current.play().then()
    }

    return (
        <div className="player p-2">
            <div className="row">
                <audio id="player" src={`http://localhost:8080/song/${currentSong.id}/file`}
                       ref={player} loop={isLoop} onTimeUpdate={onProgress} preload="auto" onCanPlay={onLoad}
                />
                <div className="row">
                    <div className="col-md-11 offset-1">
                        <span style={{fontWeight: "bold"}}>
                        {currentSong.detail ?
                            <>{currentSong.detail.title} - {currentSong.detail.artist}</>
                            :
                            <>{currentSong.name}</>
                        }
                        </span>
                    </div>
                    <div className="col-md-1 d-flex mt-1">
                        <FontAwesomeIcon icon={isPlaying ? faPause : faPlay} onClick={onPlay} className="clickable"/>
                        <FontAwesomeIcon icon={faRotateRight}
                                         onClick={onLoop}
                                         className={isLoop ? "text-primary clickable" : "clickable"} />
                    </div>
                    <div className="col-md-11 d-flex flex-row">
                        <span className="me-2">{toHHMMSS(currentTime)}</span>
                        <div className="progress flex-fill mt-1">
                            <div className="progress-bar bg-info" role="progressbar"
                                 aria-valuenow={player.current?.currentTime ?? 0} aria-valuemin={0}
                                 aria-valuemax={currentSong.detail?.seconds ?? 1} style={{width: `${percentPlayed}%`}}/>
                        </div>
                        <span className="ms-2">{toHHMMSS(Math.floor(player.current?.duration ?? currentSong.detail?.seconds ?? 0))}</span>
                    </div>
                    <div className="col-md-1">
                    </div>
                </div>
                <div className="mt-4">
                    {tracks}
                </div>
            </div>
        </div>
    )
}