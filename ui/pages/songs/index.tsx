import Layout from "../../components/Layout";
import Head from "next/head";
import {useEffect, useState} from "react";
import {ApiClient} from "../../lib/ApiClient";
import {Song} from "../../lib/Models";
import SongRow from "../../components/song/SongRow";
import {useRouter} from "next/router";
import {buildFilter, buildPageable} from "../../lib/Helper";

export default function Songs() {
    const [songs, setSongs] = useState<Array<Song>>([])
    const [loading, setLoading] = useState(true)
    const router = useRouter()

    useEffect(() => {
        const fetch = async () => {
            if(!router.isReady) {
                return
            }

            const pageable = buildPageable(router.query)
            const filter = buildFilter(router.query)

            const client = new ApiClient()
            const songs = await client.song.getAll(filter, pageable)
            setSongs(songs)
            setLoading(false)
        }

        fetch().then()
    }, [router])

    const data = songs.map( it => {
        return(<SongRow key={it.id} song={it} />)
    })

    if(loading) {
        return(<p>Loading</p>)
    } else {
        return (
            <Layout>
                <Head>
                    <title>Songs</title>
                </Head>
                <div className="m-4">
                    <h3>All Songs</h3>
                    <table className="table text-light">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Tags</th>
                        </tr>
                        </thead>
                        <tbody>
                        {data}
                        </tbody>
                    </table>
                </div>
            </Layout>
        )
    }
}