// @ts-ignore
import {PageLoad} from "../../../../.svelte-kit/types/src/routes/songs/[id]/$types";
import {error} from "@sveltejs/kit";
import {ApiClient} from "../../lib/ApiClient";

// @ts-ignore
export const load: PageLoad = async({ fetch }) => {
    const client = new ApiClient(fetch);
    const songs = await client.song.getAll()

    if(!songs) {
        throw error(500, `There are no songs.`)
    }

    return { songs: songs }
}