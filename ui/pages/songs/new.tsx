import Layout from "../../components/Layout";
import Head from "next/head";
import {useForm} from "react-hook-form";
import {ApiClient} from "../../lib/ApiClient";
import {Song} from "../../lib/Models";
import {useRouter} from "next/router";
import {useState} from "react";
import SongTags from "../../components/input/SongTags";

export default function Songs() {
    const { register, handleSubmit } = useForm();
    const router = useRouter()
    const [tags, setTags] = useState([])

    const onSubmit = async (data) => {
        try {
            const client = new ApiClient()
            const payload: Song = {
                name: data.name,
                tags: tags,
            }
            const song = await client.song.create(payload)
            await client.song.createSong(song.id, data.file[0])
            await router.push(`/songs/${song.id}`)
        } catch (e) {
            console.log(e)
        }
    }

    return (
        <Layout>
            <Head>
                <title>Song - New</title>
            </Head>
            <div>
                <h3>Create a new song</h3>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="col-md-6">
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Name</label>
                    <input type="text" name="name" {...register("name")} className="form-control" />
                </div>
                <div className="mb-3">
                    <label htmlFor="tags" className="form-label">Tags (e.g. horror, town)</label>
                    <SongTags setTags={setTags} tags={tags} disabled={false} />
                </div>
                <div className="mb-3">
                    <label htmlFor="file" className="form-label">File (*.mp3)</label>
                    <input type="file" name="file" accept=".mp3" {...register("file")} className="form-control" />
                </div>
                <input type="submit" value="Create" className="btn btn-primary col-md-12" />
            </form>
        </Layout>
    )
}