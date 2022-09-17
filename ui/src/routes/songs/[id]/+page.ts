// @ts-ignore
import {PageLoad} from "../../../../.svelte-kit/types/src/routes/songs/[id]/$types";
import {ApiClient} from "../../../lib/ApiClient";
import {error} from "@sveltejs/kit";

export const load: PageLoad = async({ params, fetch }) => {
    const id = params.id;

    const client = new ApiClient(fetch);
    const song = await client.song.get(id)

    if(!song) {
        throw error(404, {message: `Song ${id} not found`})
    }

    return song
}