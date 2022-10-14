import type {Playlist, Song} from "./Models";
import axios from "axios";

export class ApiClient {
    public readonly song: SongClient
    public readonly playlist: PlaylistClient

    constructor(baseUrl: string = "http://localhost:8080") {
        this.song = new SongClient(baseUrl)
        this.playlist = new PlaylistClient(baseUrl)
    }
}

export type Pageable = {
    page: string,
    size: string,
    sort: string,
}

export type PageableResult<T> = {
    items: Array<T>,
    total: number,
    page: number,
    size: number,
    sort: string,
}

export type Filter = Map<String, Array<String>>

class SongClient {
    private readonly url: string

    constructor(baseUrl: string) {
        this.url = baseUrl
    }

    async get(id: string): Promise<Song|undefined> {
        const response = await axios.get(`${this.url}/song/${id}`)
        if (response.status < 200 || response.status >= 400) {
            return undefined
        }

        return await response.data as Song
    }

    async getAll(filter: Filter | undefined = undefined, pageable: Pageable | undefined = undefined): Promise<PageableResult<Song>> {
        const query = buildQuery(filter, pageable)
        const response = await axios.get(`${this.url}/song?${query}`)
        if (response.status < 200 || response.status >= 400) {
            return {
                items: [],
                total: 0,
                page: 0,
                size: 0,
                sort: "",
            }
        }

        const data = response.data
        return {
            items: data as Array<Song>,
            total: Number(response.headers["x-total-count"] ?? `${data.length}`),
            page: Number(pageable.page),
            size: Number(pageable.size),
            sort: pageable.sort,
        }
    }

    async delete(id: string): Promise<void> {
        await axios.delete(`${this.url}/song/${id}`)
    }

    async create(song: Song): Promise<Song> {
        const response = await axios.post(`${this.url}/song`, song)
        if (response.status < 200 || response.status >= 400) {
            throw new Error("Failed to create song: " + response.data)
        }
        return response.data as Song
    }

    async update(song: Song): Promise<Song> {
        const response = await axios.put(`${this.url}/song/${song.id}`, song)
        if (response.status < 200 || response.status >= 400) {
            throw new Error("Failed to update song: " + response.data)
        }
        return response.data as Song
    }

    async createSong(id: string, file: File) {
        const form = new FormData()
        form.set("file", file, "file.mp3")

        const response = await axios.post(`${this.url}/song/${id}/file`, form)
        if (response.status < 200 || response.status >= 400) {
            throw new Error("Failed to update song: " + response.data)
        }
    }
}

class PlaylistClient {
    private readonly url: string

    constructor(baseUrl: string) {
        this.url = baseUrl
    }

    async get(id: string): Promise<Playlist<string>> {
        const response = await axios.get(`${this.url}/playlist/${id}`)
        if (response.status < 200 || response.status >= 400) {
            throw new Error(`Response was ${response.status}`)
        }

        return await response.data as Playlist<string>
    }

    async getAll(filter: Filter | undefined = undefined, pageable: Pageable | undefined = undefined): Promise<PageableResult<Playlist<string>>> {
        const query = buildQuery(filter, pageable)
        const response = await axios.get(`${this.url}/playlist?${query}`)

        if (response.status < 200 || response.status >= 400) {
            throw new Error(`Response was ${response.status}`)
        }

        const data = response.data
        return {
            items: data as Array<Playlist<string>>,
            total: Number(response.headers["x-total-count"] ?? `${data.length}`),
            page: Number(pageable.page),
            size: Number(pageable.size),
            sort: pageable.sort,
        }
    }
}

function buildQuery(filter: Filter | undefined = undefined, pageable: Pageable | undefined = undefined): string {
    const queryParameters = []

    if(filter) {
        filter.forEach( (values, key) => {
            const trueValues = Array.isArray(values) ? values : [values]
            trueValues.forEach((value) => {
                queryParameters.push(
                    `${encodeURIComponent(key.toString())}=${encodeURIComponent(value.toString())}`
                )
            })
        })
    }

    if(pageable) {
        Object
            .keys(pageable)
            .forEach( (key) => {
                queryParameters.push(`${key}=${pageable[key]}`)
            })
    }

    return queryParameters.join("&")
}
