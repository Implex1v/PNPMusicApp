import {Playlist} from "../../lib/Models";
import Link from "next/link";
import SongTags from "../song/SongTags";

export default function PlaylistRow({playlist}: {playlist: Playlist<string>}) {
    return <tr>
        <td>
            <Link href={`/playlists/${playlist.id}`}>
                <a>{playlist.name}</a>
            </Link>
        </td>
        <td>
            <span>
                {playlist.songs.length}
            </span>
        </td>
        <td>
            <SongTags tags={playlist.tags} setTags={() => {}} disabled={true} />
        </td>
    </tr>
}