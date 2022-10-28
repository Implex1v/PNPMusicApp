import getConfig from "next/config";

export default function config(req, res) {
    const { serverRuntimeConfig } = getConfig()
    res
        .status(200)
        .json({ api: serverRuntimeConfig.apiHost })
}