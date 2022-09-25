import Layout from "../../components/Layout";
import Head from "next/head";
import {useEffect, useState} from "react";
import {ApiClient} from "../../lib/ApiClient";
import {Song} from "../../lib/Models";
import SongRow from "../../components/song/SongRow";
import {useRouter} from "next/router";
import {buildFilter, buildPageable, buildQueryFromText} from "../../lib/Helper";

export default function Songs() {
    const [songs, setSongs] = useState<Array<Song>>([])
    const [loading, setLoading] = useState(true)
    const [searchText, setSearchText] = useState("")
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

    const search = async() => {
        const query = buildQueryFromText(searchText)
        await router.push(`/songs?${query}`)
    }

    const handleSetSearch = async(event) => {
        setSearchText(event.target.value)
    }

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
                    <div className="d-flex mt-3 mb-3">
                        <input type="text" className="form-control" placeholder="name=foo or tags=city" value={searchText} onChange={handleSetSearch} />
                        <button className="btn btn-primary ms-3" onClick={search}>Search</button>
                    </div>
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