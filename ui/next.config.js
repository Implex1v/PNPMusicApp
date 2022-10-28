/** @type {import('next').NextConfig} */
module.exports = {
  output: "standalone",
  reactStrictMode: true,
  swcMinify: true,
  publicRuntimeConfig: {
    NEXT_API_HOST: process.env.APP_API_HOST ?? "http://localhost:8080"
  }
}
