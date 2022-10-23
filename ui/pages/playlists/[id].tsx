import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {Playlist, Song} from "../../lib/Models";
import Layout from "../../components/Layout";
import {ApiClient} from "../../lib/ApiClient";
import {ApiService} from "../../lib/ApiService";
import Head from "next/head";
import SongTags from "../../components/song/SongTags";
import PlaylistPlayer from "../../components/player/PlaylistPlayer";

export default function GetPlaylist() {
    const router = useRouter()
    const id = router.query.id as string
    const [ playlist, setPlaylist ] = useState<Playlist<Song>>()
    const [ ready, setReady ] = useState(false)
    const [ error, setError ] = useState<Error>(undefined)

    useEffect(() => {
        const data = async() => {
            if(!router.isReady) {
                return
            }

            const client = new ApiClient()
            const service = new ApiService(client)
            const playlist = await service.getFullPlaylist(id)
            setPlaylist(playlist)
            setReady(true)
        }
        data().catch((error) => setError(error))
    }, [id, router])

    if(!ready) {
        return (<p>Loading</p>)
    } else {
        return (
            <Layout>
                <Head>
                    <title>Playlist - {playlist.name} </title>
                </Head>
                <h2 className="mt-5 mb-5">
                    Playlist {playlist.name}
                </h2>
                {playlist &&
                    <div>
                        <table className="table text-white">
                            <tbody>
                                <tr>
                                    <td>Id</td>
                                    <td>{playlist.id}</td>
                                </tr>
                                <tr>
                                    <td>Name</td>
                                    <td>{playlist.name}</td>
                                </tr>
                                <tr>
                                    <td>Tags</td>
                                    <td>
                                        <SongTags disabled={true} tags={playlist.tags} setTags={() => {}} />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <div className="col-md-6">
                            <PlaylistPlayer playlist={playlist} />
                        </div>
                    </div>
                }
            </Layout>
        )
    }
}