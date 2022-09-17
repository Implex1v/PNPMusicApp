import type {Song} from "./Models";

type Fetch = (info: RequestInfo, init?: RequestInit) => Promise<Response>
const baseUrl = "http://localhost:8080"

export class ApiClient {
    public readonly song: SongClient
    private readonly fetch: Fetch

    constructor(fetch: Fetch) {
        this.fetch = fetch
        this.song = new SongClient(this.fetch)
    }


}

class SongClient {
    private readonly fetch: Fetch

    constructor(fetch: Fetch) {
        this.fetch = fetch
    }

    async get(id: string): Promise<Song|undefined> {
        const response = await this.fetch(`${baseUrl}/song/${id}`)
        if (!response.ok) {
            return undefined
        }
        return await response.json()
    }

    async getAll(): Promise<Array<Song>> {
        const response = await this.fetch(`${baseUrl}/song`)
        if (!response.ok) {
            return []
        }
        return response.json()
    }

    async delete(id: string): Promise<void> {
        await this.fetch(`${baseUrl}/song/${id}`, { method: "DELETE" })
    }

    async create(song: Song): Promise<Song|undefined> {
        const response = await this.fetch(`${baseUrl}/song`, { method: "POST" })
        if (!response.ok) {
            return undefined
        }
        return response.json()
    }

    async update(song: Song): Promise<Song|undefined> {
        const response = await this.fetch(`${baseUrl}/song/${song.id}`, { method: "PUT" })
        if (!response.ok) {
            return undefined
        }
        return response.json()
    }
}