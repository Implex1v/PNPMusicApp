import {Song} from "../../lib/Models";
import Link from "next/link";
import SongTags from "./SongTags";

export default function SongRow({song}: {song: Song}) {
    return (
        <tr>
            <td>
                <Link href={`/songs/${song.id}`}>
                    <a>{song.id}</a>
                </Link>
            </td>
            <td>
                <Link href={`/songs/${song.id}`}>
                    <a>{song.name}</a>
                </Link>
            </td>
            <td>
                <SongTags tags={song.tags} disabled={true} setTags={()=>{}} />
            </td>
        </tr>
    )
}