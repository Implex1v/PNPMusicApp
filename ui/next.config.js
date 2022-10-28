/** @type {import('next').NextConfig} */
module.exports = {
  output: "standalone",
  reactStrictMode: true,
  swcMinify: true,
  serverRuntimeConfig: {
    apiHost: process.env.APP_API_HOST ?? "http://localhost:8080"
  }
}
