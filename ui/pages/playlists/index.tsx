import {useEffect, useState} from "react";
import {getConfiguredApiClient, PageableResult} from "../../lib/ApiClient";
import {Playlist} from "../../lib/Models";
import {useRouter} from "next/router";
import Layout from "../../components/Layout";
import Head from "next/head";
import Pagination from "../../components/Pagination";
import {buildFilter, buildPageable, buildQueryFromText} from "../../lib/Helper";
import PlaylistRow from "../../components/playlist/PlaylistRow";
import SearchInput from "../../components/SearchInput";

export default function Playlists() {
    const [playlists, setPlaylists] = useState<PageableResult<Playlist<string>>>(undefined)
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

            const client = await getConfiguredApiClient()
            const playlists = await client.playlist.getAll(filter, pageable)
            setPlaylists(playlists)
            setLoading(false)
        }

        fetch().then()
    })

    const doSearch = async() => {
        if(searchText.length == 0) {
            await router.push("/playlists")
            return
        }

        const query = buildQueryFromText(searchText)
        await router.push(`/playlists?${query}`)
    }

    if(loading) {
        return <p>Loading</p>
    } else {
        const data = playlists.items.map(it => {
            return <PlaylistRow playlist={it} key={it.id} />
        })

        return <Layout>
            <Head>
                <title>Playlists</title>
            </Head>
            <div className="m-4">
                <h3>All Playlists</h3>
                <SearchInput search={searchText} setSearch={setSearchText} submit={doSearch} />
                <Pagination<Playlist<string>> result={playlists} baseUri={"/playlists"}>
                    <table className="table text-light">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>#Songs</th>
                                <th>Tags</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data}
                        </tbody>
                    </table>
                </Pagination>
            </div>
        </Layout>
    }
}