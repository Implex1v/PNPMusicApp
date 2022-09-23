import Layout from "../../components/Layout";
import {useEffect, useState} from "react";
import {ApiClient} from "../../lib/ApiClient";
import {useRouter} from "next/router";
import {Song} from "../../lib/Models";
import SongTags from "../../components/song/SongTags";
import Head from "next/head";
import SongDetails from "../../components/song/SongDetails";

export default function GetSong() {
    const router = useRouter()
    const id = router.query.id as string
    const [ song, setSong ] = useState<Song>()
    const [ error, setError ] = useState<Error>(undefined)

    useEffect(() => {
        const data = async() => {
            const client = new ApiClient()
            const song = await client.song.get(id)
            setSong(song)
        }

        data().catch(it => setError(it))
    })

    return (
        <Layout>
            <Head>
                <title>Song - {id}</title>
            </Head>
            <h2 className="mt-5 mb-5">
                Song {id}
            </h2>
            {song &&
                <div>
                    <table className="table text-light">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Value</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Name</td>
                                <td>{song.name}</td>
                            </tr>
                            <tr>
                                <td>ID</td>
                                <td>{song.id}</td>
                            </tr>
                            <tr>
                                <td>Name</td>
                                <td>
                                    <SongTags tags={song.tags} setTags={() => {}} disabled={true} />
                                </td>
                            </tr>
                            <tr>
                                <td>Details</td>
                                <td>
                                    <SongDetails detail={song.detail} />
                                </td>
                            </tr>
                            <tr>
                                <td>Song</td>
                                <td>
                                    <audio src={`http://localhost:8080/song/${id}/file`} controls={true}>
                                    </audio>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            }
        </Layout>
    )
}