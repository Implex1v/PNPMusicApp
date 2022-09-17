/** @type {import('@sveltejs/kit').HandleClientError} */
// @ts-ignore
export const handleError = ({error, event}) => {
    return {
        message: error.message,
        code: error.status ?? 500,
    }
}