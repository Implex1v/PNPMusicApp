export type Song = {
    id?: string,
    name: string,
    detail?: SongDetail | null,
    tags: Array<string>,
}

export type SongDetail = {
    title: string,
    album: string | undefined,
    artist: string,
    seconds: number,
}

export type Playlist<T> = {
    id?: string,
    name: string,
    tags: Array<String>,
    songs: Array<T>,
}
