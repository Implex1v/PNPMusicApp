export default function config(req, res) {
    res
        .status(200)
        .json({ api: process.env.APP_API_HOST ?? "http://localhost:8080" })
}