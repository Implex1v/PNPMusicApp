import type {Song} from "./Models";
import axios from "axios";

type Fetch = (info: RequestInfo, init?: RequestInit) => Promise<Response>

export class ApiClient {
    public readonly song: SongClient

    constructor(baseUrl: string = "http://localhost:8080") {
        this.song = new SongClient(baseUrl)
    }
}

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

    async getAll(): Promise<Array<Song>> {
        const response = await axios.get(`${this.url}/song`)
        if (response.status < 200 || response.status >= 400) {
            return []
        }
        return response.data as Array<Song>
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