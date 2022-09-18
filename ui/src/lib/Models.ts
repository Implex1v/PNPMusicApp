export type Song = {
    id: string,
    name: string,
    detail: SongDetail | undefined,
    tags: Array<string>,
}

export type SongDetail = {
    title: string,
    album: string | undefined,
    artist: string,
    seconds: number,
}