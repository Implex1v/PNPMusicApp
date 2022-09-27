import {PageableResult} from "../lib/ApiClient";
import Link from "next/link";
import type {Song} from "../lib/Models";

export type PaginationProp = {
    result: PageableResult<Song>,
    baseUri: string,
    children: any,
}

function hasPrevious(result: PageableResult<Song>) {
    return result.page > 0
}

function hasNext(result: PageableResult<Song>) {
    return ((result.page + 1) * result.size) < result.total
}

function buildNextLink(result: PageableResult<Song>, baseUri: string) {
    return `${baseUri}?page=${result.page + 1}&size=${result.size}&sort=${result.sort}`
}

function buildPreviousLink(result: PageableResult<Song>, baseUri: string) {
    return `${baseUri}?page=${result.page - 1}&size=${result.size}&sort=${result.sort}`
}

export default function Pagination({ result, baseUri, children }: PaginationProp) {
    const previous = hasPrevious(result)
    const next = hasNext(result)
    const previousClassName = "page-item" + (previous ? "" : " disabled")
    const nextClassName = "page-item" + (next ? "" : " disabled")

    return (
        <div>
            {children}
            <nav className="d-flex justify-content-center">
                <ul className="pagination">
                    <li className={previousClassName}>
                        {previous ?
                            <Link href={buildPreviousLink(result, baseUri)}>
                                <a className="page-link">Previous</a>
                            </Link>
                            :
                            <span className="page-link">Previous</span>
                        }
                    </li>
                    <li className="page-item active">
                        <span className="page-link">{result.page}</span>
                    </li>
                    <li className={nextClassName}>
                        {next ?
                            <Link href={buildNextLink(result, baseUri)}>
                                <a className="page-link">Next</a>
                            </Link>
                            :
                            <span className="page-link">Next</span>
                        }
                    </li>
                </ul>
            </nav>
        </div>
    )
}