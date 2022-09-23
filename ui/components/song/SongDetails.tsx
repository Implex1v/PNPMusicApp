import {SongDetail} from "../../lib/Models";

type SongDetailProps = {
    detail?: SongDetail,
}

export default function SongDetails({detail}: SongDetailProps) {
    if(detail) {
        return (<table className="table text-light">
            <tbody>
            <tr>
                <td>Title</td>
                <td>{detail.title}</td>
            </tr>
            <tr>
                <td>Artist</td>
                <td>{detail.artist}</td>
            </tr>
            <tr>
                <td>Album</td>
                <td>{detail.album}</td>
            </tr>
            <tr>
                <td>Duration</td>
                <td>{detail.seconds}s</td>
            </tr>
            </tbody>
        </table>)
    } else {
        return(
            <span>No song details</span>
        )
    }
}