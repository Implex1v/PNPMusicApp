import {Song} from "../../lib/Models";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheckCircle, faPlay} from "@fortawesome/free-solid-svg-icons";
import SongTags from "../song/SongTags";
import {toHHMMSS} from "../../lib/Helper";

export default function SongListItem({song, onPlay, playing}: {song: Song, onPlay: (Song) => void, playing: boolean}) {
    return (
        <div className="row mb-1">
            <div className="col-md-1 d-flex align-items-center">
                {playing ?
                    <FontAwesomeIcon icon={faCheckCircle} onClick={() => onPlay(song)} className="clickable"></FontAwesomeIcon>
                    :
                    <FontAwesomeIcon icon={faPlay} onClick={() => onPlay(song)} className="clickable"></FontAwesomeIcon>
                }

            </div>
            <div className="col-md-7 d-flex align-items-center">
                {song.detail ?
                    <span>{song.detail.title}: {song.detail.artist} ({toHHMMSS(song.detail.seconds)})</span>
                    :
                    <span>{song.name}</span>
                }
            </div>
            <div className="col-md-4  d-flex align-items-center">
                <SongTags tags={song.tags} setTags={() => {}} disabled={true} />
            </div>
        </div>
    )
}
