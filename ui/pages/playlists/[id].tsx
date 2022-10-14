import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {Playlist, Song} from "../../lib/Models";
import Layout from "../../components/Layout";
import {ApiClient} from "../../lib/ApiClient";

export default function GetPlaylist() {
    const router = useRouter()
    const id = router.query.id as string
    const [ playlist, setPlaylist ] = useState<Playlist>()
    const [ ready, setReady ] = useState(false)
    const [ error, setError ] = useState<Error>(undefined)

    useEffect(() => {
        const data = async() => {
            if(!router.isReady) {
                return
            }

            const client = new ApiClient()
            const playlist = await client.playlist.get(id)
            
        }
    }, [id, router])
    return (<Layout></Layout>)
}