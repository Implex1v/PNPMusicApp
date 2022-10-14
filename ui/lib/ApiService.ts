import {ApiClient} from "./ApiClient";
import {Playlist, Song} from "./Models";

export class ApiService {
    private readonly client: ApiClient

    constructor(client: ApiClient) {
        this.client = client
    }

    async getFullPlaylist(playlistId: string): Promise<Playlist<Song>> {
        const playlist = await this.client.playlist.get(playlistId)
        const songs = await Promise.all(playlist.songs.map((value) => {
            return this.client.song.get(value)
        }))

        return {
            id: playlist.id,
            name: playlist.name,
            tags: playlist.tags,
            songs: songs,
        }
    }
}