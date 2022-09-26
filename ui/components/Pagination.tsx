import {Pageable} from "../lib/ApiClient";
import Link from "next/link";

export type PaginationProp = {
    pageable: Pageable,
    baseUri: string,
    children: any,
}

function hasPrevious(pageable: Pageable) {
    return Number(pageable.page) > 1
}

function hasNext(pageable: Pageable) {
    return true
}

function buildNextLink(pageable: Pageable, baseUri: string) {
    return `${baseUri}?page=${Number(pageable.page) + 1}&size=${pageable.size}&sort=${pageable.sort}`
}

function buildPreviousLink(pageable: Pageable, baseUri: string) {
    return `${baseUri}?page=${Number(pageable.page) - 1}&size=${pageable.size}&sort=${pageable.sort}`
}

export default function Pagination({ pageable, baseUri, children }: PaginationProp) {
    const previous = hasPrevious(pageable)
    const next = hasNext(pageable)
    const previousClassName = "page-item" + (previous ? "" : " disabled")
    const nextClassName = "page-item" + (next ? "" : " disabled")

    return (
        <div>
            {children}
            <nav className="d-flex justify-content-center">
                <ul className="pagination">
                    <li className={previousClassName}>
                        {previous ?
                            <Link href={buildPreviousLink(pageable, baseUri)}>
                                <a className="page-link">Previous</a>
                            </Link>
                            :
                            <span className="page-link">Previous</span>
                        }
                    </li>
                    <li className="page-item active">
                        <span className="page-link">{pageable.page}</span>
                    </li>
                    <li className={nextClassName}>
                        {next ?
                            <Link href={buildNextLink(pageable, baseUri)}>
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