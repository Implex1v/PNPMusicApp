import {Song} from "../../lib/Models";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPause, faPlay} from "@fortawesome/free-solid-svg-icons";
import SongTags from "../song/SongTags";
import {toHHMMSS} from "../../lib/Helper";

export default function SongListItem({song, onPlay, playing}: {song: Song, onPlay: (Song) => void, playing: boolean}) {
    return (
        <div className="row mb-1">
            <div className="col-md-1">
                {playing ?
                    <FontAwesomeIcon icon={faPause} onClick={() => onPlay(song)} className="clickable"></FontAwesomeIcon>
                    :
                    <FontAwesomeIcon icon={faPlay} onClick={() => onPlay(song)} className="clickable"></FontAwesomeIcon>
                }

            </div>
            <div className="col-md-8">
                {song.detail ?
                    <span>{song.detail.title}: {song.detail.artist} ({toHHMMSS(song.detail.seconds)})</span>
                    :
                    <span>{song.name}</span>
                }
            </div>
            <div className="col-md-3">
                <SongTags tags={song.tags} setTags={() => {}} disabled={true} />
            </div>
        </div>
    )
}
